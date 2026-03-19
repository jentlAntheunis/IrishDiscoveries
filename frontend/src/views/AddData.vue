<script setup>
import NewDiscoveryForm from "@/components/newDiscoveryForm.vue";
import Button from "primevue/button";
</script>

<template>
	<div class="add-data">
		<Button link>
			<RouterLink to="/">Go back</RouterLink>
		</Button>
		<NewDiscoveryForm />
	</div>
</template>

<style scoped></style>
