<script setup>
import { onMounted, ref } from "vue";
import { RouterLink, useRoute } from "vue-router";
import Button from "primevue/button";

import api, { Entity } from "@/api";
import DiscoveryForm from "@/components/DiscoveryForm.vue";

const route = useRoute();

const discovery = ref(null);
const isLoading = ref(true);
const loadError = ref("");

onMounted(async () => {
	try {
		discovery.value = await api.getById(Entity.Discovery, route.params.id);
	} catch (error) {
		console.error("Failed to load discovery:", error);
		loadError.value = "Unable to load discovery details.";
	} finally {
		isLoading.value = false;
	}
});
</script>

<template>
	<div class="edit-discovery">
		<Button link>
			<RouterLink to="/dashboard">Go back</RouterLink>
		</Button>

		<h2>Edit discovery</h2>

		<p v-if="isLoading">Loading discovery...</p>
		<p v-else-if="loadError" class="error-text">{{ loadError }}</p>
		<DiscoveryForm v-else :discovery-id="route.params.id" :initial-discovery="discovery" submit-label="Update" />
	</div>
</template>

<style scoped>
.error-text {
	color: #c0392b;
}
</style>
