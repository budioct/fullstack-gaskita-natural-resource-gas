<script setup>
import { ref, computed, watch } from "vue";
import * as yup from "yup";
import { useField, useForm } from "vee-validate";
import {detailFolksy} from '../../services/apiService.js'
import {useNotification} from "../../constants/notifications.js";

const props = defineProps({
  isVisible: Boolean,
  mode: String,
  subAgentsData: Object,
  subAgentsStock: Number,
});
const folksy = ref({});

const emit = defineEmits(["close", "save"]);

const form = ref({
  id: null,
  subAgentName: "",
  address: "",
  stock_amount_gas: null
});

function setFolksyDetail(data) {
  folksy.value = data;
}

async function fetchFolksyDetail() {
  try {
    const randomId = Math.floor(Math.random() * 100) + 1;
    const response = await detailFolksy(randomId);
    if (response.status === 200) {
      setFolksyDetail(response.data.data);
    } else {
      useNotification.warning("Not Found", `Data Folksy tidak ditemukan.`);
    }
  } catch (error) {
    console.error('Error fetching folksy data:', error);
    useNotification.error("Error", "Terjadi kesalahan dalam mengambil data.");
  }
}

watch(() => props.isVisible, (newValue) => {
  if (newValue) {
    fetchFolksyDetail();
  }
});

watch(() => props.subAgentsStock, (newValue) =>
    {
      //console.log("Updated subAgentsStock:", newValue);
    },
    {immediate: true}
);

watch(() => props.subAgentsData, (newVal) => {
      if (newVal) {
        form.value = {...newVal};
      }
    },
    {immediate: true}
);

const subAgentsStock = computed(() => props.subAgentsStock);

const schema = computed(() => yup.object({
  stock_amount_gas: yup
      .number()
      .typeError("*Stock amount must be a number")
      .required("*Stock amount is required")
      .positive("*Stock amount must be greater than 0")
      .max(subAgentsStock.value, `*Stock amount cannot exceed ${subAgentsStock.value} Sub Agents stock`),
}));

const { handleSubmit, errors , resetForm} = useForm({
  validationSchema: schema,
  validateOnBlur: true,
  validateOnChange: true,
  validateOnSubmit: true,
});

const { value: stock_amount_gas } = useField("stock_amount_gas");
const submit = handleSubmit((values, { resetForm }) => {
  const formattedTransaction = {
    sourceSubAgentId: props.subAgentsData.id,
    targetFolksyId: folksy.value.id,
    amountGas: values.stock_amount_gas,
    pricePerUnit: 22000,
  };
  emit("save", formattedTransaction);
  resetForm();
});

const paymentAmount = computed(() => stock_amount_gas.value * 22000);

const closeModal = () => {
  resetForm();
  emit("close");
};
</script>

<template>
  <div v-if="isVisible" class="modal-overlay">
    <div class="modal-container">
      <h3 v-if="mode === 'transaction'">Transaction</h3>

      <form @submit.prevent="submit">

        <div class="form-group mb-2" style="border-bottom: 2px dashed gray;">
          <label class="mb-2">Buyer</label>:&nbsp; {{folksy.name}}
        </div>

        <div class="form-group">
          <label>Price Per-Unit</label>:&nbsp; Rp. 22000
        </div>

        <div class="form-group">
          <label>Amount GAS</label>
          <input
              type="number"
              v-model="stock_amount_gas"
              class="form-control"
              :class="{ 'is-invalid': errors.stock_amount_gas }"
          />
          <small><span v-if="errors.stock_amount_gas" class="text-danger">{{ errors.stock_amount_gas }}</span></small>
        </div>

        <div class="form-group mt-2" style="border-top: 2px dashed gray;">
          <label class="mt-2">Payment Amount</label>:&nbsp;
          <span style="color: #6CB52D; font-weight: bold; text-decoration: underline; text-underline-offset: 0.4rem;">
            Rp. {{paymentAmount ? paymentAmount : '-'}}
          </span>
        </div>

        <div class="modal-actions">
          <button type="button" class="btn btn-sm btn-secondary" @click="closeModal">Cancel</button>
          <button type="submit" class="btn btn-sm btn-primary">Save</button>
        </div>
      </form>

    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 500px;
}

.form-group {
  margin-bottom: 10px;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 5px;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
}
</style>