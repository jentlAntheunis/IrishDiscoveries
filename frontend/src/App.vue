<script setup>
import LoginForm from "@/components/LoginForm.vue";
import Topbar from "@/components/Topbar.vue";
</script>

<template>
  <div class="app__container">
    <header>
      <Topbar />
    </header>
    <main>
      <RouterView />
    </main>
    <footer>
      <LoginForm/>
    </footer>
  </div>
</template>

<style scoped>
.app__container {
  min-height: 100vh;
  display: grid;
  grid-template-rows: 80px 1fr 60px;
}

.app__container > * > * {
  max-width: 1000px;
  margin: .5rem auto;
}

header {
  background-color: var(--c-green);
}

footer {
  background-color: var(--c-orange);
}
</style>