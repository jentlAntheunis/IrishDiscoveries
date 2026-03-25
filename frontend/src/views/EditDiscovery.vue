<script setup>
import { onMounted, ref } from "vue";
import { RouterLink, useRouter, useRoute } from "vue-router";
import Button from "primevue/button";

import api, { Entity } from "@/api";
import DiscoveryForm from "@/components/DiscoveryForm.vue";

const router = useRouter();
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

const deleteDiscovery = async () => {
	if (!confirm("Are you sure you want to delete this discovery? This action cannot be undone.")) {
		return;
	}

	try {
		await api.deleteById(Entity.Discovery, route.params.id);
		router.push("/dashboard");
	} catch (error) {
		console.error("Failed to delete discovery:", error);
		alert("An error occurred while trying to delete the discovery. Please try again.");
	}
};
</script>

<template>
	<div class="edit-discovery">
		<div class="edit-top">
			<Button link>
				<RouterLink to="/dashboard">Go back</RouterLink>
			</Button>
			<Button class="delete-button" label="Delete" @click="deleteDiscovery()" />
		</div>

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

.edit-top {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.delete-button {
	background-color: #c0392b;
	border-color: #c0392b;

	&:hover {
		background-color: #e74c3c !important;
		border-color: #e74c3c !important;
	}
}
</style>
