import axios from 'axios';

const fundApi = axios.create({
    baseURL: 'http://localhost:8080/api/fund',
});

export const getFund = (fundId) => fundApi.get(`/${fundId}`);

export const getAllFunds = () => fundApi.get('/funds');
