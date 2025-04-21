<script setup>
import {onMounted, ref} from 'vue';
import {useRouter} from "vue-router";
import {listOfficialAgent} from "../../../services/apiService.js";
import KTDatatable from "../../../components/tables/KTDatatable.vue";

const router = useRouter();
const from = ref('official-agent');
const officialAgents = ref([]);
const countPage = ref(10);
const isLoading = ref(true); // Tambahkan state loading
const columns = ref([
  {key: 'no', label: 'No'},
  {key: 'agentName', label: 'Official Agent'},
  {key: 'subholdingGroupAffiliate', label: 'Subholding'},
  {key: 'address', label: 'Address'},
  {key: 'stock_amount_gas', label: 'Gas Stock Amount'},
]);

function setOfficialAgents(data) {
  officialAgents.value = data
      .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
      .map((item, index) => ({
        no: index + 1,
        id: item.id,
        agentName: item.agentName,
        address: item.address,
        stock_amount_gas: item.stock_amount_gas,
        subholdingGroupAffiliate: item.subholdingGroupAffiliate,
        createdAt: item.createdAt,
        updatedAt: item.updatedAt
      }));
}

async function fetchOfficialAgents() {
  try {
    const response = await listOfficialAgent();
    if (response.status === 200) {
      setOfficialAgents(response.data.officialAgent);
    }
  } catch (error) {
    console.error('Error fetching stakeholder data:', error);
  } finally {
    isLoading.value = false; // Hentikan loading setelah data diambil
  }
}

onMounted(async () => {
  await fetchOfficialAgents();
});

async function goTo(id) {
  await router.push({name: 'offagent-detail', params: {id: id}});
}

</script>

<template>
  <div class="container py-5">
    <header class="mb-1 border-bottom">
      <div>
        <span class="fs-4">Official Agents</span>
      </div>
    </header>

    <!-- Tampilkan KTDatatable -->
    <KTDatatable :columns="columns" :data="officialAgents" :perPage="countPage" :loading="isLoading" :from="from">
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