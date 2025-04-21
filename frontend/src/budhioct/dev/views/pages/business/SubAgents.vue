<script setup>
import {onMounted, ref} from 'vue';
import {listSubAgent} from '../../../services/apiService.js';
import KTDatatable from "../../../components/tables/KTDatatable.vue";
import {useRouter} from "vue-router";

const router = useRouter();
const from = ref('sub-agent');
const subAgents = ref([]);
const countPage = ref(10);
const isLoading = ref(true); // Tambahkan state loading
const columns = ref([
  {key: 'no', label: 'No'},
  {key: 'subAgentName', label: 'Sub Agent'},
  {key: 'officialAgent', label: 'Official Agent'},
  {key: 'address', label: 'Address'},
  {key: 'stock_amount_gas', label: 'Gas Stock Amount'},
]);

function setSubAgents(data) {
  subAgents.value = data
      .sort((a, b) => b.createdAt - a.createdAt)
      .map((item, index) => ({
        no: index + 1,
        id: item.id,
        subAgentName: item.subAgentName,
        address: item.address,
        stock_amount_gas: item.stock_amount_gas,
        officialAgent: item.officialAgent,
        createdAt: item.createdAt,
        updatedAt: item.updatedAt
      }));
}

async function fetchSubAgents() {
  try {
    const response = await listSubAgent();
    if (response.status === 200) {
      setSubAgents(response.data.subAgent);
    }
  } catch (error) {
    console.error('Error fetching stakeholder data:', error);
  } finally {
    isLoading.value = false; // Hentikan loading setelah data diambil
  }
}

onMounted(async () => {
  await fetchSubAgents();
});

async function goTo(id) {
  await router.push({name: 'subagent-detail', params: {id: id}});
}

</script>

<template>
  <div class="container py-5">
    <header class="mb-1 border-bottom">
      <div>
        <span class="fs-4">Sub Agents</span>
      </div>
    </header>

    <!-- Tampilkan KTDatatable -->
    <KTDatatable :columns="columns" :data="subAgents" :perPage="countPage" :loading="isLoading" :from="from">
      <template #actions="{ row }">
        <button class="btn btn-outline-primary btn-sm" @click="goTo(row.id)">Detail</button>
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