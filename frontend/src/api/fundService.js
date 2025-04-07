import axios from 'axios';

const fundApi = axios.create({
    baseURL: 'http://18.204.194.16:8080/api/fund',
});

export const getFund = (fundId) => fundApi.get(`/${fundId}`);

export const getAllFunds = () => fundApi.get('/funds');
