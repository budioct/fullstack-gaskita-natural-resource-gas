<script setup>
import {ref} from "vue";
import {useRouter} from "vue-router";
import {login} from "../../../services/apiService.js";
import * as yup from "yup";
import {useField, useForm} from "vee-validate";
import {useAuthStore} from "../../../stores/authStore.js";
import {useNotification} from "../../../constants/notifications.js";

const router = useRouter();
const authStore = useAuthStore();
const apiError = ref("");
const data = ref({
  email: "",
  password: "",
});

const schema = yup.object({
  email: yup.string().required("*Email is required").email(),
  password: yup.string().required("*Password is required"),
});

const {handleSubmit, setErrors, errors, isSubmitting} = useForm({
  validationSchema: schema,
  validateOnSubmit: true,
  validateOnBlur: true,
  validateOnChange: true,
});

const {value: email} = useField("email");
const {value: password} = useField("password");

const submit = handleSubmit(async (values) => {
  try {
    data.value.email = email.value;
    data.value.password = password.value;
    const response = await login(data.value);
    // console.log(response);

    if (response.data.status_code === 200) {
      authStore.setToken(response.data.data.access_token);
      await router.push({name: 'lobby'});
      useNotification.success("Success", "Berhasil melakukan login.");
    }
  } catch (error) {
    console.error("Failed to login: " + error);
    if (error.response && error.response.data) {
      const { status_code, errors } = error.response.data;
      if (status_code === 401) {
        apiError.value = errors;
      } else {
        apiError.value = "An unexpected error occurred. Please try again.";
      }
    }
  }
});

</script>

<template>
  <section class="vh-100" style="background-color: #508bfc;">
    <div class="container py-5 h-100">
      <div class="row d-flex justify-content-center align-items-center h-100">
        <div class="col-12 col-md-8 col-lg-6 col-xl-5">
          <div class="card shadow-2-strong" style="border-radius: 1rem;">
            <div class="card-body p-5">

              <div class="text-center">
                <img :src=" `/images/logo-vertical-1.png`" alt="logo">
              </div>

              <h4 class="mb-3 text-center">Log in</h4>

              <!-- Alert for error from API -->
              <div v-if="apiError" class="alert alert-danger" role="alert">
                {{ apiError }}
              </div>

              <form @submit.prevent="submit">
                <div data-mdb-input-init class="form-outline mb-3">
                  <label class="form-label" for="typeEmailX-2">Email</label>
                  <input
                      v-model="email"
                      type="email"
                      id="typeEmailX-2"
                      class="form-control form-control-lg"
                      :class="{ 'is-invalid': errors.email }"
                      :disabled="isSubmitting"
                  />
                  <small><span v-if="errors.email" class="text-danger">{{ errors.email }}</span></small>
                </div>

                <div data-mdb-input-init class="form-outline mb-3">
                  <label class="form-label" for="typePasswordX-2">Password</label>
                  <input
                      v-model="password"
                      type="password"
                      id="typePasswordX-2"
                      class="form-control form-control-lg"
                      :class="{ 'is-invalid': errors.password }"
                      :disabled="isSubmitting"
                  />
                  <small><span v-if="errors.password" class="text-danger">{{ errors.password }}</span></small>
                </div>

                <button data-mdb-button-init data-mdb-ripple-init class="btn btn-primary btn-lg btn-block" type="submit">
                  Log in
                </button>
              </form>

              <hr class="my-4">

              <div data-mdb-input-init class="form-outline mb-3">
                Don't have an account? <router-link :to="{ name: 'register' }" class="text-primary">Sign up</router-link>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
</style>