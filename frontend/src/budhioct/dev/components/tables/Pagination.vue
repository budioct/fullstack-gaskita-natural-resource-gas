<script setup>
import { computed } from 'vue';

const props = defineProps({
  currentPage: Number,
  totalPages: Number
});

const emit = defineEmits(['pageChange']);

const visiblePages = computed(() => {
  const range = 5;
  let start = Math.max(1, props.currentPage - range);
  let end = Math.min(props.totalPages, props.currentPage + range);

  if (props.currentPage <= range) {
    end = Math.min(11, props.totalPages);
  } else if (props.currentPage >= props.totalPages - range) {
    start = Math.max(1, props.totalPages - 10);
  }

  return Array.from({ length: end - start + 1 }, (_, i) => start + i);
});

const goToPage = (page) => {
  if (page >= 1 && page <= props.totalPages) {
    emit('pageChange', page);
  }
};
</script>

<template>
  <ul class="pagination">
    <li>
      <button
          class="btn btn-sm btn-light"
          :disabled="currentPage === 1"
          @click="goToPage(currentPage - 1)"
      >
        « Prev
      </button>
    </li>

    <li v-for="page in visiblePages" :key="page">
      <button
          class="btn btn-sm"
          :class="{ 'btn-primary': currentPage === page, 'btn-light': currentPage !== page }"
          @click="goToPage(page)"
      >
        {{ page }}
      </button>
    </li>

    <li>
      <button
          class="btn btn-sm btn-light"
          :disabled="currentPage === totalPages"
          @click="goToPage(currentPage + 1)"
      >
        Next »
      </button>
    </li>
  </ul>
</template>

<style scoped>
.pagination {
  display: flex;
  gap: 5px;
  align-items: center;
}
</style>