<script setup>
import {ref, computed, onMounted} from 'vue';
import {useRoute, useRouter} from "vue-router";
import {detailSubAgent, prosesTransaction} from '../../../services/apiService.js';
import KTDatatable from "../../../components/tables/KTDatatable.vue";
import {useNotification} from "../../../constants/notifications.js"
import TransactionModal from "../../../components/modal/TransactionModal.vue";

const router = useRouter();
const route = useRoute();
const subAgentId = route.params.id;
const subAgentDetail = ref({
  id: null,
  subAgentName: '',
  address: '',
  stock: {
    id: null,
    ownership: '',
    stock_amount: null,
  },
  officialAgent: {
    id: null,
    agentName: '',
    address: '',
    stock_owner_id: null,
    stock_amount_gas: null,
    subholdingGroupAffiliate: '',
  },
  transaction: [],
  createdAt: '',
  updatedAt: ''
});
const countPage = ref(10);
const columns = ref([
  {key: 'no', label: 'No'},
  {key: 'transactionDate', label: 'Date'},
  {key: 'transactionStatus', label: 'Status'},
  {key: 'amountGas', label: 'Amount Gas'},
  {key: 'totalPrice', label: 'Total Price'},
]);
const isMobile = computed(() => window.innerWidth <= 768);
const listTotalSales = computed(() => subAgentDetail.value.transaction.length);
const totalStockGasSales = computed(() => subAgentDetail.value.transaction.reduce((total, data) => total + data.amountGas, 0));
const totalAmountOfSalesMoney = computed(() => subAgentDetail.value.transaction.reduce((total, data) => total + data.totalPrice, 0));
const profitOfSales = computed(() => totalStockGasSales.value * 2000);
const isLoading = ref(true);
const isModalOpen = ref(false);
const modalMode = ref("transaction");
const selectedSubAgentDetail = ref(null);
const newTransactionId = ref(null);

function formatDate(date) {
  return date ? new Date(date).toLocaleDateString('id-ID', { day: '2-digit', month: '2-digit', year: 'numeric' }) : null;
}

function setSubAgentDetail(data) {
  subAgentDetail.value = {
    id: data.id,
    subAgentName: data.subAgentName,
    address: data.address,
    stock: {
      id: data.stock.id,
      ownership: data.stock.ownership,
      stock_amount: data.stock.stock_amount
    },
    officialAgent: {
      id: data.officialAgent.id,
      agentName: data.officialAgent.agentName,
      address: data.officialAgent.address,
      stock_owner_id: data.officialAgent.stock_owner_id,
      stock_amount_gas: data.officialAgent.stock_amount_gas,
      subholdingGroupAffiliate: data.officialAgent.subholdingGroupAffiliate,
    },
    transaction: data.transaction
        .sort((a, b) => new Date(b.transactionDate) - new Date(a.transactionDate))
        .map((item, index) => ({
          no: index + 1,
          id: item.id,
          amountGas: item.amountGas,
          totalPrice: item.totalPrice,
          transactionStatus: item.transactionStatus,
          transactionDate: formatDate(item.transactionDate),
        })),
    createdAt: data.createdAt,
    updatedAt: data.updatedAt
  }
}

async function fetchSubAgentDetail() {
  try {
    isLoading.value = true;
    const response = await detailSubAgent(subAgentId);
    if (response.status === 200) {
      setSubAgentDetail(response.data.data);
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

onMounted(async () => {
  await fetchSubAgentDetail();
});

async function goTo(id, name) {
  await router.push({name: 'offagent-detail', params: {id: id}, query: {highlightedSubAgentName: name} });
}

async function openModal(){
  selectedSubAgentDetail.value = subAgentDetail.value ?? null;
  await fetchSubAgentDetail();
  isModalOpen.value = true;
}

async function handleSave(data) {
  try {
    if (modalMode.value === "transaction") {
      const response = await prosesTransaction(data.sourceSubAgentId, data.targetFolksyId, data.amountGas, data.pricePerUnit);
      if (response.status === 200) {
        await fetchSubAgentDetail();

        // Ambil transaksi terbaru (karena data sudah disortir dari terbaru)
        const latestTransaction = subAgentDetail.value.transaction[0];
        newTransactionId.value = latestTransaction?.id;

        // Reset setelah 5 detik
        setTimeout(() => {
          newTransactionId.value = null;
        }, 5000);

        useNotification.success("Success", "Transaksi Berhasil.");
      }
    }
    isModalOpen.value = false;
  } catch (error) {
    console.error('Error saving users:', error);
    useNotification.error("Error", "Terjadi kesalahan saat menyimpan data.");
  }
}

</script>

<template>
  <div class="container py-5">
    <header class="mb-1 border-bottom">
      <div>
        <span class="fs-4">Sub Agents Detail</span>
      </div>
    </header>

    <div v-if="subAgentDetail.stock?.stock_amount === 0" class="mt-2">
      <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>SUB AGENT!</strong> provider out of stock.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
    </div>

    <div class="row align-items-start">
      <div class="col">
        <!-- Info Sub Agents -->
        <div class="card p-1 mb-1 mt-1 bg-light">
          <div class="row">
            <div class="col">
              <h5 class="fw-bold">{{ subAgentDetail.subAgentName }}</h5>
            </div>
            <div class="col">
              <template v-if="subAgentDetail.stock.stock_amount > 0">
                <button class="btn btn-outline-success btn-sm" @click="openModal">Transaction</button>
              </template>
            </div>
          </div>
          <p><strong>Address:</strong> {{ subAgentDetail.address }}</p>
          <div class="row">
            <div class="col">
              <p><strong>Sub Agent:</strong> {{ subAgentDetail.officialAgent.agentName }}</p>
            </div>
            <div class="col" v-if="subAgentDetail.stock?.stock_amount <= 5 || subAgentDetail.stock?.stock_amount <= 13">
              <p><button class="btn btn-outline-primary btn-sm" @click="goTo(subAgentDetail.officialAgent.id, subAgentDetail.subAgentName)">Request Re-Stock</button></p>
            </div>
          </div>
          <template v-if="subAgentDetail.stock?.stock_amount === 0 || subAgentDetail.stock?.stock_amount > 0 && subAgentDetail.stock?.stock_amount <= 8">
            <p><strong>Main Stock:</strong>&nbsp;
              <span class="badge text-bg-danger">{{ subAgentDetail.stock.stock_amount }}</span> ({{ subAgentDetail.stock.ownership ?? 'Unknown' }})
            </p>
          </template>
          <template v-else-if="subAgentDetail.stock?.stock_amount >= 9 && subAgentDetail.stock?.stock_amount <= 13">
            <p><strong>Main Stock:</strong>&nbsp;
              <span class="badge text-bg-warning">{{ subAgentDetail.stock.stock_amount }}</span> ({{ subAgentDetail.stock.ownership ?? 'Unknown' }})
            </p>
          </template>
          <template v-else>
            <p><strong>Main Stock:</strong> {{ subAgentDetail.stock.stock_amount }} ({{ subAgentDetail.stock.ownership ?? 'Unknown' }})</p>
          </template>
        </div>
      </div>
      <div class="col">
        <!-- Info Sales -->
        <div class="card p-1 mb-1 mt-1 bg-light">
          <h5 class="fw-bold">Info Sales</h5>
          <p><strong>List Total Sales:</strong> {{ listTotalSales !== 0 ? listTotalSales : '-' }} </p>
          <p><strong>Total Stock Gas of Sales:</strong> {{ totalStockGasSales !== 0 ? totalStockGasSales : '-' }}</p>
          <p><strong>Total Amount of Sales Money:</strong> Rp. {{ totalAmountOfSalesMoney !== 0 ? totalAmountOfSalesMoney : '-' }}</p>
          <p><strong>Profit of Sales:</strong> Rp. {{ profitOfSales !== 0 ? profitOfSales : '-' }}</p>
        </div>
      </div>
    </div>

    <!-- Loader -->
    <div v-if="isLoading" class="d-flex justify-content-center my-4">
      <div class="spinner-border text-success" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <!-- Tampilkan KTDatatable -->
    <div v-if="subAgentDetail.transaction.length > 0">
      <header class="mb-1 mt-3 border-bottom">
        <div>
          <span class="fs-5">Transactions</span>
        </div>
      </header>
      <KTDatatable :columns="columns" :data="subAgentDetail.transaction" :perPage="countPage" :loading="isLoading" :highlightedProses="newTransactionId">
      </KTDatatable>
    </div>

    <!-- Modal -->
    <TransactionModal
        :isVisible="isModalOpen"
        :mode="modalMode"
        :subAgentsData="selectedSubAgentDetail"
        :subAgentsStock="subAgentDetail.stock.stock_amount"
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