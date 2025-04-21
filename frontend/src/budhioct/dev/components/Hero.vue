<script setup>

</script>

<template>
  <div class="container py-4">
    <!-- Hero Banner -->
    <div class="bg-dark text-white rounded-4 p-5 mb-4 shadow-lg" style="background: url('/images/bg-banner.png') center/cover no-repeat;">
      <div class="container-fluid py-5">
        <h1 class="display-5 fw-bold" style="border-bottom: 2px dashed gray">GAS KITA</h1>
        <p class="col-md-8 fs-5">
          Aplikasi ini adalah solusi digital untuk manajemen distribusi gas yang terintegrasi, efisien, dan mudah digunakan.
          Dibuat untuk mempermudah pemantauan, pendataan agen, serta mengoptimalkan pelayanan masyarakat.
        </p>
        <!--<a href="#tentang-kami" class="btn btn-outline-light btn-lg mt-3">Pelajari Selengkapnya</a>-->
      </div>
    </div>

    <!-- Tentang Kami -->
    <section id="tentang-kami" class="my-5">
      <div class="row">
        <div class="col-md-6 text-center">
          <img src="/images/logo-profile.png" alt="Logo Aplikasi" class="img-fluid mb-3" />
        </div>
        <div class="col-md-6">
          <h3 class="fw-bold">Tentang Kami</h3>
          <p class="text-justify">
            Kami adalah <strong class="dejum">GASKITA</strong>, sebuah aplikasi <em>fiktif</em> yang dirancang untuk <strong>nge-gas</strong> membantu proses distribusi dan layanan gas rumah tangga.
            Mulai dari Elpiji 3 kg bersubsidi hingga Bright Gas, kami siap memberikan pelayanan prima dan kemudahan integrasi sistem.
          </p>
        </div>
      </div>
    </section>

    <!-- Statistik / At a Glance -->
    <section class="my-5">
      <h3 class="fw-bold text-center mb-4">Sekilas Angka</h3>
      <div class="row text-center">
        <div class="col-md-4 mb-3">
          <h1 class="fw-bold text-primary">75+</h1>
          <p class="text-muted">Agen Resmi Terdaftar</p>
        </div>
        <div class="col-md-4 mb-3">
          <h1 class="fw-bold text-success">125+</h1>
          <p class="text-muted">Sub Agen Aktif</p>
        </div>
        <div class="col-md-4 mb-3">
          <h1 class="fw-bold text-danger">10K+</h1>
          <p class="text-muted">Distribusi Harian</p>
        </div>
      </div>
    </section>

    <!-- Visi Misi -->
    <section class="my-5">
      <h3 class="fw-bold">Visi & Misi</h3>
      <ol class="ps-3">
        <li>Menyediakan gas bersubsidi secara efisien dan terjangkau.</li>
        <li>Meningkatkan aksesibilitas melalui platform digital yang terintegrasi.</li>
        <li>Membangun kepercayaan dan kolaborasi dengan masyarakat dan stakeholder.</li>
      </ol>
    </section>

  </div>
</template>

<style scoped>
.text-justify {
  text-align: justify;
}

.bg-dark {
  background-color: #001e3c !important;
}
</style>