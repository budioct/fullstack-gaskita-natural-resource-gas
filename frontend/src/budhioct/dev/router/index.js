import {createRouter, createWebHistory} from "vue-router";
import {useAuthStore} from "../stores/authStore.js";
import Pages from "../views/pages/Pages.vue";
import Home from "../views/pages/Home.vue";
import Nprogress from "nprogress";

const routes = [
    {
        path: '/',
        name: 'pages',
        component: Pages,
        children: [
            {
              path: '/',
              redirect: '/home'
            },
            {
              path: '/home',
              name: 'home',
              component: Home,
              meta: {requiresAuth: true}
            },
            {
                path: '/lobby',
                name: 'lobby',
                component: () => import('../views/pages/Lobby.vue'),
                meta: {requiresAuth: true}
            },
            {
                path: '/profil',
                name: 'profil',
                component: () => import('../views/pages/profile/Profil.vue'),
                meta: {requiresAuth: true}
            },
            {
                path: '/stakeholder',
                name: 'stakeholder',
                component: () => import('../views/pages/business/Stakeholder.vue'),
                meta: {requiresAuth: true}
            },
            {
                path: '/stakeholder/:id',
                name: 'stakeholder-detail',
                component: () => import('../views/pages/business/StakeholderDetail.vue'),
                meta: {requiresAuth: true}
            },
            {
                path: '/offagent',
                name: 'offagent',
                component: () => import('../views/pages/business/OfficialAgents.vue'),
                meta: {requiresAuth: true}
            },
            {
                path: '/offagent/:id',
                name: 'offagent-detail',
                component: () => import('../views/pages/business/OfficialAgentsDetail.vue'),
                meta: {requiresAuth: true}
            },
            {
                path: '/subagent',
                name: 'subagent',
                component: () => import('../views/pages/business/SubAgents.vue'),
                meta: {requiresAuth: true}
            },
            {
                path: '/subagent/:id',
                name: 'subagent-detail',
                component: () => import('../views/pages/business/SubAgentsDetail.vue'),
                meta: {requiresAuth: true}
            },
            {
                path: '/warung',
                name: 'warung',
                component: () => import('../views/pages/Warung.vue'),
                meta: {requiresAuth: true}
            },
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/pages/auth/Login.vue'),
        meta: {guest: true},
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('../views/pages/auth/Register.vue'),
        meta: {guest: true},
    },
]

export const router = createRouter({
    history: createWebHistory(),
    routes: routes
});

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();

    if (to.meta.requiresAuth && !authStore.isAuthenticated) {
        //console.info('Access denied: You must be logged in to access this page.');
        return next({name: 'login'});
    }

    if (to.meta.guest && authStore.isAuthenticated) {
        //console.info('Access denied: You are already logged in.');
        return next({name: 'home'});
    }

    Nprogress.start();
    next();

});

router.afterEach(() => {
    Nprogress.done();
});