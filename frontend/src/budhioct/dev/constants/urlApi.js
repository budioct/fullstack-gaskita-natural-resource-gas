const buildQueryParams = (params) => {
    return new URLSearchParams(params).toString();
};

const urlApi = {
    auth: {
        register: 'http://localhost:8080/api/v1/auth/register',
        login: 'http://localhost:8080/api/v1/auth/login',
        logout: 'http://localhost:8080/api/v1/auth/logout',
    },
    stakeholder: {
        list: 'http://localhost:8080/api/v1/stakeholder/fetch',
        detail: (id) => `http://localhost:8080/api/v1/stakeholder/${id}/detail`,
        production: (id) => `http://localhost:8080/api/v1/stakeholder/${id}/re-production`,
    },
    officialagent: {
        list: 'http://localhost:8080/api/v1/official-agent/fetch',
        detail: (id) => `http://localhost:8080/api/v1/official-agent/${id}/detail`,
    },
    subagent: {
        list: 'http://localhost:8080/api/v1/sub-agent/fetch',
        detail: (id) => `http://localhost:8080/api/v1/sub-agent/${id}/detail`,
    },
    folksy: {
        detail: (id) => `http://localhost:8080/api/v1/folksy/${id}/detail`,
    },
    distribute: {
        order: (sourceStockId, targetStockId, amount) => `http://localhost:8080/api/v1/distribution/transfer/stock?${buildQueryParams({ sourceStockId, targetStockId, amount })}`,
    },
    transaction: {
        list: 'http://localhost:8080/api/v1/transaction/fetch',
        proses: (sourceSubAgentId, targetFolksyId, amountGas, pricePerUnit) => `http://localhost:8080/api/v1/transaction/proses?${buildQueryParams({ sourceSubAgentId, targetFolksyId, amountGas, pricePerUnit })}`,
    }
}

export { urlApi }