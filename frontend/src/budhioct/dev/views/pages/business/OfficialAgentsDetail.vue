<script setup>
import {ref, computed, onMounted, onUnmounted} from 'vue';
import {useRoute, useRouter} from "vue-router";
import {detailOfficialAgent, distribute} from '../../../services/apiService.js';
import {useNotification} from "../../../constants/notifications.js";
import OfficialAgentsModal from "../../../components/modal/OfficialAgentsModal.vue"

const router = useRouter();
const route = useRoute();
const officialAgentId = route.params.id;
const highlightedSubAgentName = route.query.highlightedSubAgentName;
const highlightedName = ref(highlightedSubAgentName || null);
const newHighlightedSubAgentDistribute = ref(null);
const officialAgentDetail = ref({
  id: null,
  agentName: '',
  address: '',
  stock: {
    id: null,
    ownership: '',
    stock_amount: null
  },
  stakeholder: {
    id: null,
    subholdingGroupAffiliate: '',
    address: '',
    contact: '',
    stock_owner_id: null,
    stock_amount_gas: null,
    createdAt: '',
    updatedAt: '',
  },
  subAgentName: [],
  createdAt: '',
  updatedAt: ''
});
const isMobile = computed(() => window.innerWidth <= 768);
const totalStock = computed(() =>
    officialAgentDetail.value.subAgentName.reduce((total, data) => total + data.stock_amount_gas, 0)
);
const isLoading = ref(true);
const isModalOpen = ref(false);
const modalMode = ref("distribute");
const selectedOfficialAgentDetail = ref(null);

function setOfficialAgentDetail(data) {
  officialAgentDetail.value = {
    no: data.no,
    id: data.id,
    agentName: data.agentName,
    address: data.address,
    stock: {
      id: data.stock.id,
      ownership: data.stock.ownership,
      stock_amount: data.stock.stock_amount
    },
    stakeholder: {
      id: data.stakeholder.id,
      subholdingGroupAffiliate: data.stakeholder.subholdingGroupAffiliate,
      address: data.stakeholder.address,
      contact: data.stakeholder.contact,
      stock_owner_id: data.stakeholder.stock_owner_id,
      stock_amount_gas: data.stakeholder.stock_amount_gas,
      createdAt: data.stakeholder.createdAt,
      updatedAt: data.stakeholder.updatedAt,
    },
    subAgentName: data.subAgentName.map((item, index) => ({
      no: index + 1,
      id: item.id,
      subAgentName: item.subAgentName,
      address: item.address,
      stock_owner_id: item.stock_owner_id,
      stock_amount_gas: item.stock_amount_gas
    })),
    createdAt: data.createdAt,
    updatedAt: data.updatedAt
  };
}

async function fetchOfficialAgentDetail() {
  try {
    isLoading.value = true;
    const response = await detailOfficialAgent(officialAgentId);
    if (response.status === 200) {
      setOfficialAgentDetail(response.data.data);
    } else {
      useNotification.error("Error", "Gagal mengambil data stakeholder.");
    }
  } catch (error) {
    console.error('Error fetching stakeholder data:', error);
    useNotification.error("Error", "Terjadi kesalahan dalam mengambil data.");
  } finally {
    isLoading.value = false;
  }
}

async function openModal(mode, agent = null) {
  modalMode.value = mode;
  selectedOfficialAgentDetail.value = agent ? { ...agent } : null;
  await fetchOfficialAgentDetail();
  isModalOpen.value = true;
}

async function handleSave(data) {
  try {
    const payload = {
      sourceStockId: officialAgentDetail.value.stock.id,
      targetStockId: data.targetStockId,
      amount: data.amount,
    };
    if (modalMode.value === 'distribute') {
      const response = await distribute(payload.sourceStockId, payload.targetStockId, payload.amount);
      if (response.status === 200) {
        await fetchOfficialAgentDetail();

        newHighlightedSubAgentDistribute.value = selectedOfficialAgentDetail.value.no;

        setTimeout(() => {
          newHighlightedSubAgentDistribute.value = null;
        }, 5000);

        useNotification.success("Success", "Distribusi Berhasil.");
      }
    }
    isModalOpen.value = false;
  } catch (error) {
    console.error('Error saving users:', error);
    useNotification.error("Error", "Terjadi kesalahan saat menyimpan data.");
  }
}

function updateIsMobile() {
  isMobile.value = window.innerWidth <= 768;
}

onMounted(async () => {
  await fetchOfficialAgentDetail();

  if (highlightedName.value) {
    setTimeout(() => {
      highlightedName.value = null;
      router.replace({
        name: route.name,
        params: route.params,
        query: {}
      });
    }, 5000);
  }
});

onUnmounted(() => {
  window.removeEventListener("resize", updateIsMobile);
});

async function goTo(id, name) {
  await router.push({name: 'stakeholder-detail', params: {id: id}, query: {highlightedOfficialAgentName: name}});
}

</script>

<template>
  <div class="container py-5">
    <header class="mb-1 border-bottom">
      <div>
        <span class="fs-4">Official Agents Detail</span>
      </div>
    </header>

    <div v-if="officialAgentDetail.stock?.stock_amount === 0" class="mt-2">
      <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>OFFICIAL AGENT!</strong> provider out of stock.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
    </div>

    <div class="row align-items-start">
      <div class="col">
        <!-- Info Official Agents -->
        <div class="card p-1 mb-1 mt-1 bg-light">
          <h5 class="fw-bold">{{ officialAgentDetail.agentName }}</h5>
          <p><strong>Address:</strong> {{ officialAgentDetail.address }}</p>
          <div class="row">
            <div class="col">
              <p><strong>Subholding:</strong> {{ officialAgentDetail.stakeholder.subholdingGroupAffiliate }}</p>
            </div>
            <div class="col" v-if="officialAgentDetail.stock?.stock_amount <= 300 || officialAgentDetail.stock?.stock_amount <= 600">
              <p><button class="btn btn-outline-primary btn-sm" @click="goTo(officialAgentDetail.stakeholder.id, officialAgentDetail.agentName)">Request Re-Stock</button></p>
            </div>
          </div>
          <template v-if="officialAgentDetail.stock?.stock_amount >= 301 && officialAgentDetail.stock?.stock_amount <= 600">
            <p><strong>Main Stock:</strong>&nbsp;
              <span class="badge text-bg-warning">{{ officialAgentDetail.stock?.stock_amount ?? 0 }}</span>
              ({{ officialAgentDetail.stock?.ownership ?? 'Unknown' }})
            </p>
          </template>
          <template v-else-if="officialAgentDetail.stock?.stock_amount === 0 || officialAgentDetail.stock?.stock_amount > 0 && officialAgentDetail.stock?.stock_amount <= 300">
            <p><strong>Main Stock:</strong>&nbsp;
              <span class="badge text-bg-danger">{{ officialAgentDetail.stock?.stock_amount ?? 0 }}</span>
              ({{ officialAgentDetail.stock?.ownership ?? 'Unknown' }})
            </p>
          </template>
          <template v-else>
            <p><strong>Main Stock:</strong> {{ officialAgentDetail.stock?.stock_amount ?? 0 }} ({{ officialAgentDetail.stock?.ownership ?? 'Unknown' }})</p>
          </template>
        </div>
      </div>
      <div class="col">
        <!-- Info Affiliate -->
        <div class="card p-1 mb-1 mt-1 bg-light">
          <h5 class="fw-bold">Affiliate Official Agents</h5>
          <p><strong>List Total Affiliate:</strong> {{ officialAgentDetail.subAgentName.length }} </p>
          <p><strong>Total Stock Gas Affiliate:</strong> {{ totalStock }}</p>
        </div>
      </div>
    </div>

    <!-- Loader -->
    <div v-if="isLoading" class="d-flex justify-content-center my-4">
      <div class="spinner-border text-success" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <!-- Mode Desktop (Tabel) -->
    <div v-if="!isMobile">
      <header class="mb-1 mt-3 border-bottom">
        <div>
          <span class="fs-5">Sub Agents</span>
        </div>
      </header>
      <div class="table-responsive">
        <table class="table table-hover">
          <thead>
          <tr>
            <th>#</th>
            <th>Agent Name</th>
            <th>Address</th>
            <th>Stock Gas</th>
            <th v-if="officialAgentDetail.stock?.stock_amount !== 0">Action</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="agent in officialAgentDetail.subAgentName" :key="agent.id">
            <td>{{ agent.no }}</td>
            <td>
              {{ agent.subAgentName }}
              <span v-if="highlightedName === agent.subAgentName" class="badge bg-primary ms-2 badge-animated">New Request ReStock</span>
              <span v-else-if="newHighlightedSubAgentDistribute === agent.no" class="badge bg-primary ms-2 badge-animated"> New ReStock </span>
            </td>
            <td>{{ agent.address }}</td>
            <td>{{ agent.stock_amount_gas }}</td>
            <td v-if="officialAgentDetail.stock?.stock_amount !== 0">
              <template v-if="highlightedName === agent.subAgentName">
                <button type="button" class="btn btn-outline-primary btn-sm position-relative" @click="openModal('distribute', agent)">
                  Distribute
                  <span class="position-absolute top-0 start-100 translate-middle p-2 bg-danger border border-light rounded-circle">
                  <span class="visually-hidden">New alerts</span></span>
                </button>
              </template>
              <template v-else>
                <button class="btn btn-outline-primary btn-sm" @click="openModal('distribute', agent)">Distribute</button>
              </template>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Mode Mobile (Card/Grid) -->
    <div v-else>
      <header class="mb-1 mt-3 border-bottom">
        <div>
          <span class="fs-5">Official Agents</span>
        </div>
      </header>
      <div class="row">
        <div class="col-12 col-md-6 mb-3" v-for="agent in officialAgentDetail.subAgentName" :key="agent.id">
          <div class="card p-3 shadow-sm">
            <h6 class="fw-bold">{{ agent.agentName }} <span v-if="highlightedName === agent.subAgentName" class="badge bg-primary ms-2 badge-animated">New Request ReStock</span> <span v-else-if="newHighlightedSubAgentDistribute === agent.no" class="badge bg-primary ms-2 badge-animated"> New ReStock </span> </h6>
            <p><strong>Address:</strong> {{ agent.address }}</p>
            <p><strong>Stock Gas:</strong> {{ agent.stock_amount_gas }}</p>
            <p v-if="officialAgentDetail.stock?.stock_amount !== 0">
              <template v-if="highlightedName === agent.subAgentName">
                <button type="button" class="btn btn-outline-primary btn-sm position-relative" @click="openModal('distribute', agent)">
                  Distribute
                  <span class="position-absolute top-0 start-100 translate-middle p-2 bg-danger border border-light rounded-circle">
                  <span class="visually-hidden">New alerts</span></span>
                </button>
              </template>
              <template v-else>
                <button class="btn btn-outline-primary btn-sm" @click="openModal('distribute', agent)">Distribute</button>
              </template>
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <OfficialAgentsModal
        :isVisible="isModalOpen"
        :mode="modalMode"
        :subAgentsData="selectedOfficialAgentDetail"
        :subAgentsStock="officialAgentDetail.stock.stock_amount"
        @save="handleSave"
        @close="isModalOpen = false"
    />

  </div>
</template>

<style scoped>
.py-5 {
  padding-top: 5px !important;
  padding-bottom: 2px !important;
}

.table-responsive {
  overflow-x: auto;
}

.card {
  border-radius: 8px;
}

p {
  margin-bottom: 0 !important;
}
</style>