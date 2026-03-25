<script setup>
import { computed, ref, onMounted } from "vue";
import { authState } from "@/state/auth.ts";
import { RouterLink } from "vue-router";
import api from "@/api.js";
import Table from "@/components/Table.vue";
import Rating from "primevue/rating";

const userId = authState.value.id;
const discoveries = ref([]);

onMounted(async () => {
	const data = await api.getUserDiscoveries(userId);
	discoveries.value = data ?? [];
	console.log("Fetched discoveries:", discoveries.value);
});

const tableConfig = {
	columns: {
		Name: "name",
		Category: {
			field: "category",
			formatter: value => value?.name ?? "-",
		},
		Location: {
			field: "location",
			formatter: value => {
				if (!value) return "-";
				const place = value.placename ?? "";
				const county = value.county ?? "";
				return [place, county].filter(Boolean).join(", ") || "-";
			},
		},
		Rating: {
			field: "rating",
		},
	},
};

const tableColumns = computed(() => {
	return Object.entries(tableConfig.columns).map(([label, value]) => {
		if (typeof value === "string") {
			return { label, field: value };
		}

		return {
			label,
			field: value.field,
			formatter: value.formatter,
		};
	});
});

function formatCell(row, column) {
	return column.formatter ? column.formatter(row[column.field], row) : row[column.field];
}

function getMapsUrl(location) {
	if (!location) return null;
	if (location.mapsUrl) return location.mapsUrl;

	const lat = location.coordinates?.lat;
	const lon = location.coordinates?.lon;
	if (lat == null || lon == null) return null;

	return `https://maps.google.com/?q=${lat},${lon}`;
}
</script>

<template>
	<div class="tableContainer">
		<Table v-if="discoveries.length > 0" :columns="tableColumns" :rows="discoveries" key-field="id">
			<template #cell="{ row, column }">
				<template v-if="column.field === 'location'">
					<span>{{ formatCell(row, column) }}</span>
					<a
						v-if="getMapsUrl(row.location)"
						:href="getMapsUrl(row.location)"
						target="_blank"
						rel="noopener"
						class="maps-link"
					>
						Google Maps
					</a>
				</template>
				<template v-else-if="column.field === 'rating'">
					<Rating v-if="row.rating > 0" :modelValue="row.rating" :stars="5" readonly :cancel="false" />
					<span v-else>-</span>
				</template>
				<template v-else>
					{{ formatCell(row, column) }}
				</template>
			</template>

			<template #actions="{ row }">
				<RouterLink :to="`/edit/${row.id}`" class="action-link">Edit</RouterLink>
			</template>
		</Table>

		<p v-else class="empty-state">
			No discoveries yet.
			<RouterLink to="/add" class="empty-link">Add something new!</RouterLink>
		</p>
	</div>
</template>

<style scoped>
.tableContainer {
	height: 100%;
	padding-top: 1rem;
}

.maps-link {
	margin-left: 0.75rem;
	color: var(--c-green-dark-1);
	font-weight: 500;
}

.action-link {
	display: inline-block;
	padding: 0.25rem 0.65rem;
	border-radius: 999px;
	background-color: #ffffff;
	color: var(--c-orange-dark-1);
	font-weight: 600;
	text-decoration: none;
}

.empty-link {
	margin-left: 0.5rem;
	font-weight: 600;
}

.empty-state {
	padding: 0.5rem;
	color: var(--c-grey);
}

.review-price {
	color: var(--c-grey);
}
</style>
