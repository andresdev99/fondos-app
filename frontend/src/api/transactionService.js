import axios from 'axios';

const transactionApi = axios.create({
    baseURL: 'http://localhost:8080/api/transaction',
});

// Get transactions: GET /api/transaction/transactions
export const getTransactions = () => transactionApi.get('/transactions');

// Subscribe to a fund: POST /api/transaction/subscribe
export const subscribeFund = (fondoId, email) =>
    transactionApi.post('/subscribe', { fondoId, email });

export const cancelFund = (fondoId, email) =>
    transactionApi.post('/cancel', { fondoId, email });

export default transactionApi;
