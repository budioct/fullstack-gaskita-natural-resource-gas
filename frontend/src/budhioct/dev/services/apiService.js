import {urlApi} from "../constants/urlApi.js";
import axios from 'axios';
import {useAuthStore} from "../stores/authStore.js";

const register = async (dataRegister) => {
    try {
        return await axios.post(urlApi.auth.register, dataRegister, {
            withCredentials: true
        });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const login = async (dataLogin) => {
    try {
        return await axios.post(urlApi.auth.login, dataLogin, {
            withCredentials: true
        });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const logout = async () => {
    try {
        return await axios.post(urlApi.auth.logout, {}, {
            withCredentials: true,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${useAuthStore().token}`,
            },
        });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const listStakeholder = async () => {
    try {
        return await axios.get(urlApi.stakeholder.list,
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${useAuthStore().token}`,
                },
            });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const detailStakeholder = async (id) => {
    try {
        return await axios.get(urlApi.stakeholder.detail(id),
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${useAuthStore().token}`,
                },
            });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const productionStakeholder = async (id) => {
    try {
        return await axios.post(urlApi.stakeholder.production(id), {}, {
            withCredentials: true,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${useAuthStore().token}`,
            },
        });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const listOfficialAgent = async () => {
    try {
        return await axios.get(urlApi.officialagent.list,
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${useAuthStore().token}`,
                },
            });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const detailOfficialAgent = async (id) => {
    try {
        return await axios.get(urlApi.officialagent.detail(id),
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${useAuthStore().token}`,
                },
            });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const listSubAgent = async () => {
    try {
        return await axios.get(urlApi.subagent.list,
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${useAuthStore().token}`,
                },
            });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const detailSubAgent = async (id) => {
    try {
        return await axios.get(urlApi.subagent.detail(id),
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${useAuthStore().token}`,
                },
            });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const detailFolksy = async (id) => {
    try {
        return await axios.get(urlApi.folksy.detail(id),
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${useAuthStore().token}`,
                },
            });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const listTransaction = async () => {
    try {
        return await axios.get(urlApi.transaction.list,
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${useAuthStore().token}`,
                },
            });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const prosesTransaction = async (sourceSubAgentId, targetFolksyId, amountGas, pricePerUnit) => {
    try {
        return await axios.post(urlApi.transaction.proses(sourceSubAgentId, targetFolksyId, amountGas, pricePerUnit), {}, {
            withCredentials: true,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${useAuthStore().token}`,
            },
        });
    } catch (error) {
        console.error(error);
        throw error
    }
};

const distribute = async (sourceStockId, targetStockId, amount) => {
    try {
        return await axios.post(urlApi.distribute.order(sourceStockId, targetStockId, amount), {}, {
            withCredentials: true,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${useAuthStore().token}`,
            },
        });
    } catch (error) {
        console.error(error);
        throw error
    }
};

export {
    register,
    login,
    logout,
    listStakeholder,
    detailStakeholder,
    productionStakeholder,
    listOfficialAgent,
    detailOfficialAgent,
    listSubAgent,
    detailSubAgent,
    listTransaction,
    prosesTransaction,
    distribute,
    detailFolksy,
}