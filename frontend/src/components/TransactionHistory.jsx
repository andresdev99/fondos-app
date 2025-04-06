import '../styles/Transaction.css'; // New CSS file for transaction cards

const TransactionHistory = ({transactions, fundsMap, transError}) => {

    return (
        <div className="transaction-history">
            <h2>Transaction History</h2>
            {transError && <p className="error">{transError}</p>}
            {transactions.length > 0 ? (
                <div className="transactions">
                    {transactions.map((txn) => (
                        <div className="transaction-card" key={txn.transaccionId}>
                            <div className="transaction-row">
                                <span className="label">ID:</span>
                                <span>{txn.transaccionId}</span>
                            </div>
                            <div className="transaction-row">
                                <span className="label">Tipo:</span>
                                <span>{txn.tipo}</span>
                            </div>
                            <div className="transaction-row">
                                <span className="label">Fecha:</span>
                                <span>{new Date(txn.fecha).toLocaleString()}</span>
                            </div>
                            <div className="transaction-row">
                                <span className="label">Nombre de fondo:</span>
                                <span>{fundsMap[txn.fondoId] || 'Unknown'}</span>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <p>No transactions found.</p>
            )}
        </div>
    );
};

export default TransactionHistory;
