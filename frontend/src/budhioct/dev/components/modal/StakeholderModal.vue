<script setup>
import { ref, computed, watch } from "vue";
import * as yup from "yup";
import { useField, useForm } from "vee-validate";

const props = defineProps({
  isVisible: Boolean,
  mode: String,
  stakeholderData: Object,
  subholdingStock: Number,
});

const emit = defineEmits(["close", "save"]);

const form = ref({
  id: null,
  agentName: "",
  address: "",
  stock_amount_gas: null
});

watch(() => props.subholdingStock, (newValue) =>
    {
      //console.log("Updated subholdingStock:", newValue);
    },
    {immediate: true}
);

watch(() => props.stakeholderData, (newVal) => {
      if (newVal) {
        form.value = {...newVal};
      }
    },
    {immediate: true}
);

const subHoldingStock = computed(() => props.subholdingStock); // Contoh stok SubHolding yang dimiliki

// Definisikan skema validasi menggunakan yup
const schema = computed(() => yup.object({
  stock_amount_gas: yup
      .number()
      .typeError("*Stock amount must be a number")
      .required("*Stock amount is required")
      .positive("*Stock amount must be greater than 0")
      .max(subHoldingStock.value, `*Stock amount cannot exceed ${subHoldingStock.value} Subholding stock`),
}));

// Setup form validation dengan vee-validate
const { handleSubmit, errors , resetForm} = useForm({
  validationSchema: schema,
  validateOnBlur: true,
  validateOnChange: true,
  validateOnSubmit: true,
});

const { value: stock_amount_gas } = useField("stock_amount_gas");
const submit = handleSubmit((values, { resetForm }) => {
  const formattedStakeholder = {
    targetStockId: props.stakeholderData.stock_owner_id,
    amount: values.stock_amount_gas
  };
  emit("save", formattedStakeholder);
  resetForm();
});

// Fungsi untuk reset form saat modal ditutup
const closeModal = () => {
  resetForm(); // Reset form saat modal ditutup
  emit("close");
};

</script>

<template>
  <div v-if="isVisible" class="modal-overlay">
    <div class="modal-container">
      <h3 v-if="mode === 'distribute'">Distribute</h3>

      <form @submit.prevent="submit">
        <div class="form-group">
          <label>Agent Name</label>
          <input type="text" v-model="form.agentName" disabled />
        </div>

        <div class="form-group">
          <label>Address</label>
          <textarea v-model="form.address" disabled></textarea>
        </div>

        <div class="form-group">
          <label>Stock Amount</label>
          <input
              type="number"
              v-model="stock_amount_gas"
              class="form-control"
              :class="{ 'is-invalid': errors.stock_amount_gas }"
          />
          <small><span v-if="errors.stock_amount_gas" class="text-danger">{{ errors.stock_amount_gas }}</span></small>
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