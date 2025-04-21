<script setup>
import Navbar from "../../components/Navbar.vue";
import Footer from "../../components/Footer.vue";
</script>

<template>
  <!-- page content -->
  <div class="container">
    <navbar></navbar>
    <router-view></router-view>
    <Footer></Footer>
  </div>
  <!-- /page content -->
</template>

<style scoped>

</style>