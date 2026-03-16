<script setup>
import Topbar from "@/components/Topbar.vue";
import Footer from "@/components/Footer.vue";
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
			<Footer />
		</footer>
	</div>
</template>

<style scoped>
.app__container {
	min-height: 100vh;
	display: grid;
	grid-template-rows: 80px 1fr 60px;
}
main {
	width: 1000px;
	margin: 0 auto;
}
</style>
