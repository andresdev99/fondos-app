import React from 'react';
import { updateFund } from '../api/clientService';
import { cancelFund, subscribeFund } from '../api/transactionService';
import '../styles/FundCard.css';

const UpdateFundForm = ({ client, fund, isActive, notificationType, onUpdate }) => {
    const buttonClass = isActive ? 'unsub' : 'sub';

    const handleToggle = () => {
        const action = isActive ? "desinscribirse" : "inscribirse";
        const confirmationMessage = `¿Desea ${action}? Le notificaremos también vía ${notificationType}.`;
        if (window.confirm(confirmationMessage)) {
            const actionPromise = isActive
                ? cancelFund(fund.fondoId, client.email)
                : subscribeFund(fund.fondoId, client.email);

            actionPromise
                .then(() => updateFund(client.clienteId, fund.fondoId, !isActive))
                .then(() => onUpdate())
                .catch((err) => console.error(`Error updating ${fund.fondoId}:`, err));
        }
    };


    return (
        <div className="fund-card">
            <div className="fund-info">
        <span className="fund-category">
          {fund.categoria === 'FPV'
              ? 'Fondo Voluntario de Pensión'
              : 'Fondo de Inversión Colectiva'}
        </span>
                <h4 className="fund-name">{fund.nombre}</h4>
                <p className="fund-minimum">
                    Monto mínimo: <span className="money-amount">COP {fund.montoMinimo}</span>
                </p>
            </div>
            <button className={buttonClass} onClick={handleToggle}>
                {isActive ? 'Desinscribirse' : 'Inscribirse'}
            </button>
        </div>
    );
};

export default UpdateFundForm;
