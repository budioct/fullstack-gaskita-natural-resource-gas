import {defineStore} from "pinia";
import cookies from "vue-cookies";

const getToken = () => {
    return cookies.get('token');
};

const setToken = (token) => {
    cookies.set('token', token, '1d');
};

const clearToken = () => {
    cookies.remove('token');
};

export const useAuthStore = defineStore('auth', {
    state: () => ({
      token: getToken() || null,
        isAuthenticated: !!getToken(),
    }),
    actions: {
        setToken(token) {
            this.token = token;
            this.isAuthenticated = !!token;

            if (token){
                setToken(token)
            } else {
                this.clearToken();
            }
        },
        clearToken() {
            this.token = null;
            this.isAuthenticated = false;

            clearToken();
        },
    }
});