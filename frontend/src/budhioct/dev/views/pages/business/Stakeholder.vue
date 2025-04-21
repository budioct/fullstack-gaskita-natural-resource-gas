<script setup>
import {onMounted, ref} from 'vue';
import {useRouter} from "vue-router";
import {listStakeholder, productionStakeholder} from '../../../services/apiService.js';
import KTDatatable from "../../../components/tables/KTDatatable.vue";
import {useNotification} from "../../../constants/notifications.js";

const router = useRouter();
const from = ref('stackholder');
const stakeholders = ref([]);
const countPage = ref(10);
const isLoading = ref(true); // Tambahkan state loading
const columns = ref([
  {key: 'no', label: 'No'},
  {key: 'subholdingGroupAffiliate', label: 'Subholding'},
  {key: 'address', label: 'Address'},
  {key: 'contact', label: 'Contact'},
  {key: 'stock_amount_gas', label: 'Gas Stock Amount'},
  //{ key: 'officialAgentName', label: 'Agen Resmi' },
  //{ key: 'createdAt', label: 'Tanggal Dibuat' },
  //{ key: 'updatedAt', label: 'Tanggal Diperbarui' }
]);
const newTransactionId = ref(null);

function setStakeholder(data) {
  stakeholders.value = data
      .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
      .map((item, index) => ({
        no: index + 1,
        id: item.id,
        subholdingGroupAffiliate: item.subholdingGroupAffiliate,
        address: item.address,
        contact: item.contact,
        stock_amount_gas: Number(item.stock_amount_gas),
        officialAgentName: item.officialAgentName,
        createdAt: item.createdAt,
        updatedAt: item.updatedAt
      }));
}

async function fetchStakeholder() {
  try {
    isLoading.value = true;
    const response = await listStakeholder();
    if (response.status === 200) {
      setStakeholder(response.data.stakeholders);
    }
  } catch (error) {
    console.error('Error fetching stakeholder data:', error);
  } finally {
    isLoading.value = false; // Hentikan loading setelah data diambil
  }
}

onMounted(async () => {
  await fetchStakeholder();
});

const isValidStock = (stock) => {
  if (stock <= 5000) {
    return Number(stock) <= 5000;
  } else if (stock <= 10000) {
    return Number(stock) <= 10000;
  } else {
    return Number(stock) <= 0;
  }
};

async function goTo(id) {
  await router.push({name: 'stakeholder-detail', params: {id: id}});
}

async function reProduction(id) {
  try {
    const response = await productionStakeholder(id);
    if (response.status === 200) {
      await fetchStakeholder();

      newTransactionId.value = id;
      console.log(newTransactionId.value);

      // Reset setelah 5 detik
      setTimeout(() => {
        newTransactionId.value = null;
      }, 5000);

      useNotification.success("Success", "Re-Production Berhasil.");
    } else {
      useNotification.error("Error", "Re-Production Gagal.");
    }
  } catch (error) {
    console.error('Error fetching stakeholder data:', error);
  }
}

</script>

<template>
  <div class="container py-5">
    <header class="mb-1 border-bottom">
      <div>
        <span class="fs-4">Holding</span>
      </div>
    </header>

      <!-- Tampilkan KTDatatable -->
      <KTDatatable :columns="columns" :data="stakeholders" :perPage="countPage" :loading="isLoading" :from="from" :highlightedProses="newTransactionId">
        <template #actions="{ row }">
          <button class="btn btn-outline-primary btn-sm" @click="goTo(row.id)">Detail</button>&nbsp;
          <button v-if="isValidStock(row.stock_amount_gas)" class="btn btn-outline-success btn-sm" @click="reProduction(row.id)">Re-Production</button>
        </template>
      </KTDatatable>

  </div>
</template>

<style scoped>
.py-5 {
  padding-top: 5px !important;
  padding-bottom: 2px !important;
}
</style>