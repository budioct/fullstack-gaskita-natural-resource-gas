<script setup>
import {useAuthStore} from "../../stores/authStore.js";

const authStore = useAuthStore();

const isLoggedIn = authStore.isAuthenticated;

</script>

<template>
  <div class="container py-5">
    <h1>Login Status</h1>
    <p v-if="isLoggedIn">You are logged in! 🎉</p>
    <p v-else>You are not logged in.</p>
  </div>
</template>

<style scoped>

</style>