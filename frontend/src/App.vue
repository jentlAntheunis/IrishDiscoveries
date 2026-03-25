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

header {
	width: 100%;
	position: sticky;
	top: 0;
	z-index: 100;
}
main {
	width: 1000px;
	margin: 0 auto;
	margin-bottom: 2rem;
}
footer {
	width: 100%;
	position: sticky;
	bottom: 0;
	z-index: 100;
}
</style>
