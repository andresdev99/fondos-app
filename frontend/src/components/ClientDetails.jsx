import React from 'react';
import '../styles/ClientDetails.css'; // We'll create this CSS file for better styling

const ClientDetails = ({ client }) => {
    if (!client) {
        return <p>Loading client data...</p>;
    }
    return (
        <div className="client-details-card">
            <h2>Detalles de Cliente</h2>
            <div className="client-row">
                <span className="client-label">ID:</span>
                <span>{client.clienteId}</span>
            </div>
            <div className="client-row">
                <span className="client-label">Name:</span>
                <span>{client.nombre}</span>
            </div>
            <div className="client-row">
                <span className="client-label">Balance (Monto):</span>
                <strong>{client.monto}</strong>
            </div>
            <div className="client-row">
                <span className="client-label">Notification Type:</span>
                <span>{client.tipoNotificacion}</span>
            </div>
            <div className="client-row">
                <span className="client-label">Email:</span>
                <span>{client.email}</span>
            </div>
        </div>
    );
};

export default ClientDetails;
