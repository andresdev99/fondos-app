import React, {useState, useEffect} from 'react';
import {getClient} from '../api/clientService';
import {getAllFunds} from '../api/fundService';
import Sidebar from './Sidebar';
import UpdateFundForm from './UpdateFundForm';
import TransactionHistory from './TransactionHistory';
import ClientDetails from './ClientDetails'; // <-- Import the new component
import '../styles/App.css';
import {getTransactions} from "../api/transactionService.js";

const Dashboard = () => {
    const clientId = "1";
    const [client, setClient] = useState(null);
    const [funds, setFunds] = useState([]);
    const [error, setError] = useState('');
    const [transactions, setTransactions] = useState([]);
    const [fundsMap, setFundsMap] = useState({});
    const [transError, setTransError] = useState('');

    const fetchClient = () => {
        getClient(clientId)
            .then((res) => setClient(res.data))
            .catch((err) => {
                setError('Error fetching client data');
                console.error(err);
            });
    };

    const fetchClientAndTransactions = () => {
        fetchClient()
        fetchTransactions()
    }

    const fetchTransactions = () => {
        getTransactions()
            .then((response) => {
                // Sort the transactions in descending order by date
                const sortedTransactions = response.data.sort(
                    (a, b) => new Date(b.fecha) - new Date(a.fecha)
                );
                setTransactions(sortedTransactions);

                // Then fetch all funds
                return getAllFunds();
            })
            .then((response) => {
                const map = {};
                response.data.forEach((fund) => {
                    map[fund.fondoId] = fund.nombre;
                });
                setFundsMap(map);
            })
            .catch((err) => {
                setTransError('Error fetching transactions or funds');
                console.error(err);
            });
    };

    const fetchFunds = () => {
        getAllFunds()
            .then((res) => setFunds(res.data))
            .catch((err) => {
                setError('Error fetching funds data');
                console.error(err);
            });
    };

    useEffect(() => {
        fetchClient();
        fetchFunds();
        fetchTransactions()
    }, []);

    return (
        <div className="dashboard">
            {error && <p className="error">{error}</p>}
            {client ? (
                <>
                    <Sidebar
                        client={client}
                        currentNotification={client ? client.tipoNotificacion : 'email'}
                        onUpdate={fetchClient}
                    />
                    <hr/>
                    <div className="dashboard-content">
                        <h1>Dashboard</h1>
                        {error && <p className="error">{error}</p>}

                        <ClientDetails client={client}/>

                        <hr/>
                        <div className="funds-section">
                            <h3>Administrar Fondos</h3>
                            <div>
                                {funds.map((fund) => {
                                    const active = client["fondo" + parseInt(fund.fondoId, 10)];
                                    return (
                                        <UpdateFundForm
                                            key={fund.fondoId}
                                            client={client}
                                            fund={fund}
                                            isActive={active}
                                            notificationType={client.tipoNotificacion}
                                            onUpdate={fetchClientAndTransactions}
                                        />
                                    );
                                })}
                            </div>
                        </div>
                        <hr/>
                        <div className="transactions-section">
                            <TransactionHistory
                                transactions={transactions}
                                fundsMap={fundsMap}
                                transError={transError}
                            />
                        </div>
                    </div>
                </>

            ) : (
                <p>Loading client data...</p>
            )}
        </div>
    );
}

export default Dashboard;
