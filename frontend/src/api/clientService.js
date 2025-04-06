import axios from 'axios';

const clientApi = axios.create({
    baseURL: 'http://localhost:8080/api/client',
});

export const getClient = (clientId) => clientApi.get(`/${clientId}`);

export const updateNotificationType = (clientId, tipoNotificacion, email) =>
    clientApi.put(`/${clientId}/notificationType`, { tipoNotificacion, email });

export const updateFund = (clientId, fondoKey, value) =>
    clientApi.put(`/${clientId}/fund/Fondo${fondoKey}`, { value });

export default clientApi;
