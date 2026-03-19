<script setup>
import Button from "primevue/button";
import { useRouter } from "vue-router";
</script>

<template>
	<div class="footer__container">
		<div class="footer">
			<p>&copy; Jentl Antheunis</p>

			<RouterLink class="addBtnLink" to="/add" v-if="useRouter().currentRoute.value.path != '/add'">
				<Button class="addBtn"> Add data </Button>
			</RouterLink>
		</div>
	</div>
</template>

<style scoped>
.footer__container {
	background-color: var(--c-orange);
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
}

.footer {
	max-width: 1000px;
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.addBtnLink {
	padding: none;
	&:hover {
		background-color: none;
	}
}
.addBtn {
	background-color: var(--c-green);

	&:hover {
		background-color: var(--c-green-dark-1);
	}
}
</style>
