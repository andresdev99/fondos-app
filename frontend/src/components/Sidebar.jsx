import React, { useState, useEffect } from 'react';
import { updateNotificationType } from '../api/clientService';
import '../styles/Sidebar.css';

const Sidebar = ({ client, currentNotification, onUpdate }) => {
    // Initialize with currentNotification and client's email (if available)
    const [notificationType, setNotificationType] = useState(currentNotification);
    const [email, setEmail] = useState(client ? client.email : '');

    // When the client prop changes, update the email field
    useEffect(() => {
        if (client) {
            setEmail(client.email);
        }
    }, [client]);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (notificationType.toLowerCase() === 'email') {
            updateNotificationType(client.clientId, notificationType, email)
                .then(() => onUpdate())
                .catch((err) => console.error("Error updating notification type:", err));
        } else {
            updateNotificationType(client.clientId, notificationType)
                .then(() => onUpdate())
                .catch((err) => console.error("Error updating notification type:", err));
        }
    };

    return (
        <div className="sidebar">
            <h3>Settings</h3>
            <form onSubmit={handleSubmit}>
                <label htmlFor="notificationSelect" className="label">
                    Notification Type:
                </label>
                <select
                    id="notificationSelect"
                    value={notificationType}
                    onChange={(e) => setNotificationType(e.target.value)}
                >
                    <option value="email">Email</option>
                    <option value="sms">SMS</option>
                </select>
                {notificationType.toLowerCase() === 'email' ?
                    (
                    <>
                        <label htmlFor="emailInput" className="label">
                            Email:
                        </label>
                        <input
                            type="email"
                            id="emailInput"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </>
                ) :
                    (
                        <p>Los SMS no son soportados</p>
                    )
                }
                <button type="submit" className="update-btn">Update</button>
            </form>
        </div>
    );
};

export default Sidebar;
