<script setup>
import {ref} from "vue";
import {register} from "../../../services/apiService.js";
import * as yup from "yup";
import {useField, useForm} from "vee-validate";
import {useNotification} from "../../../constants/notifications.js";

const apiError = ref("");
const apiSuccess = ref("");
const data = ref({
  email: "",
  password: "",
  role: "",
});

const schema = yup.object({
  email: yup.string().required("*Email is required").email(),
  password: yup.string().required("*Password is required").min(8),
  role: yup.string().required("*Role is required").oneOf(["USER"]),
});

const {handleSubmit, setErrors, errors, isSubmitting, resetForm} = useForm({
  validationSchema: schema,
  validateOnSubmit: true,
  validateOnBlur: true,
  validateOnChange: true,
});

const {value: email} = useField("email");
const {value: password} = useField("password");
const {value: role} = useField("role");

const submit = handleSubmit(async (values, { resetForm }) => {
  try {
    data.value.email = email.value;
    data.value.password = password.value;
    data.value.role = role.value;
    const response = await register(data.value);
    if (response.data.status_code === 201) {
      apiSuccess.value = response.data.message + "... you can ";
      useNotification.success("Success", "Berhasil melakukan registrasi.");
      resetForm();
    }

  } catch (error) {
    console.error("Failed to register: " + error);
    if (error.response && error.response.data) {
      const { status_code, errors } = error.response.data;
      if (status_code === 400) {
        apiError.value = errors + ". Try email else.";
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

              <h4 class="mb-3 text-center">Sign up</h4>

              <!-- Alert for success from API -->
              <div v-if="apiSuccess" class="alert alert-success alert-dismissible fade show" role="alert">
                {{ apiSuccess }} <router-link :to="{ name: 'login' }" class="text-primary">Log in</router-link>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
              </div>

              <!-- Alert for error from API -->
              <div v-if="apiError" class="alert alert-danger alert-dismissible fade show" role="alert">
                {{ apiError }}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
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

                <div data-mdb-input-init class="form-outline mb-3">
                  <label class="form-label" for="inputState">Role</label>
                  <select
                      v-model="role"
                      id="inputState"
                      class="form-control"
                      :class="{ 'is-invalid': errors.role }"
                  >
                    <option value="" selected>Choose...</option>
                    <option value="USER">USER</option>
                  </select>
                  <small><span v-if="errors.role" class="text-danger">{{ errors.role }}</span></small>
                </div>

                <button data-mdb-button-init data-mdb-ripple-init class="btn btn-primary btn-lg btn-block"
                        type="submit">
                  Sign up
                </button>
              </form>

              <hr class="my-4">

              <div data-mdb-input-init class="form-outline mb-3">
                Have an account?
                <router-link :to="{ name: 'login' }" class="text-primary">Log in</router-link>
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