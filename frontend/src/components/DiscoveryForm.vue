<script setup>
import { onMounted, reactive, ref, watch, nextTick } from "vue";
import L from "leaflet";
import "leaflet/dist/leaflet.css";

import InputText from "primevue/inputtext";
import Button from "primevue/button";
import Message from "primevue/message";
import DatePicker from "primevue/datepicker";
import FloatLabel from "primevue/floatlabel";
import Select from "primevue/select";
import Textarea from "primevue/textarea";
import Rating from "primevue/rating";

import AutoComplete from "primevue/autocomplete";

import { authState } from "@/state/auth.ts";
import api, { Entity } from "@/api";
import { useRouter } from "vue-router";

const router = useRouter();

// Leaflet map configuration
const mapContainer = ref(null);
let mapInstance = null;
let markerInstance = null;

const props = defineProps({
	discoveryId: {
		type: String,
		default: null,
	},
	initialDiscovery: {
		type: Object,
		default: null,
	},
	submitLabel: {
		type: String,
		default: "Submit",
	},
});

const form = reactive({
	userId: authState.value.id,
	name: "",
	category: "",
	location: {},
	discoveredOn: null,
	rating: 0,
	description: "",
	priceCategory: "",
});
const errors = reactive({});

const locationSuggestions = ref([]);
const categories = ref([]);

onMounted(async () => {
	const data = await api.get(Entity.Category);
	categories.value = data.map(c => ({ label: c.name, value: c.id }));

	// Fix missing Leaflet icon issue
	delete L.Icon.Default.prototype._getIconUrl;
	L.Icon.Default.mergeOptions({
		iconRetinaUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon-2x.png",
		iconUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon.png",
		shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png",
	});
});

const initializeMap = async (lat, lon) => {
	await nextTick();

	if (!mapContainer.value) return;

	// Initialize map if not already done
	if (!mapInstance) {
		mapInstance = L.map(mapContainer.value).setView([lat, lon], 11);
		L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
			attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
			maxZoom: 19,
		}).addTo(mapInstance);
	} else {
		mapInstance.setView([lat, lon], 11);
	}

	// Remove old marker if exists
	if (markerInstance) {
		mapInstance.removeLayer(markerInstance);
	}

	// Add draggable marker
	markerInstance = L.marker([lat, lon], { draggable: true }).addTo(mapInstance);

	// Handle marker drag
	markerInstance.on("dragend", function () {
		const pos = markerInstance.getLatLng();
		form.location.coordinates = {
			lat: parseFloat(pos.lat.toFixed(6)),
			lon: parseFloat(pos.lng.toFixed(6)),
		};
	});
};

watch(
	() => form.location,
	value => {
		if (value?.coordinates?.lat && value?.coordinates?.lon) {
			initializeMap(value.coordinates.lat, value.coordinates.lon);
		}
	},
	{ deep: true },
);

watch(
	() => props.initialDiscovery,
	value => {
		if (!value) {
			return;
		}

		form.userId = value.userId ?? value.user?.id ?? authState.value.id;
		form.name = value.name ?? "";
		form.category = value.category?.id ?? value.categoryId ?? "";
		form.location = value.location ?? {};
		form.discoveredOn = value.discoveredOn ? new Date(value.discoveredOn) : null;
		form.rating = value.rating ?? 0;
		form.description = value.description ?? "";
		form.priceCategory = value.priceCategory ?? "";

		// Initialize map if location with coordinates is present
		if (form.location?.coordinates?.lat && form.location?.coordinates?.lon) {
			initializeMap(form.location.coordinates.lat, form.location.coordinates.lon);
		}
	},
	{ immediate: true },
);

const searchLocation = async event => {
	const res = await fetch(`https://nominatim.openstreetmap.org/search?format=jsonv2&addressdetails=1&q=${event.query}`);
	const data = await res.json();
	console.log("Location search results:", data);
	locationSuggestions.value = data.map(p => ({
		label: p.display_name,
		coordinates: {
			lat: parseFloat(p.lat),
			lon: parseFloat(p.lon),
		},
		placename:
			p.address.city ||
			p.address.town ||
			p.address.village ||
			p.address.civil_parish ||
			p.city_district ||
			p.display_name.split(",")[1]?.trim() ||
			label,
		county: p.address.county || "",
		postcode: p.address.postcode || "",
		road: p.address.road || "",
		boundingBox: {
			north: p.boundingbox[1],
			south: p.boundingbox[0],
			east: p.boundingbox[3],
			west: p.boundingbox[2],
		},
	}));
};

const validate = () => {
	Object.keys(errors).forEach(key => delete errors[key]);

	if (!form.name) errors.name = "Name is required";
	if (!form.category) errors.category = "Category is required";
	if (!form.location || Object.keys(form.location).length === 0) errors.location = "Location is required";
	if (!form.discoveredOn) errors.discoveredOn = "Discovery date is required";

	if (form.rating === null || form.rating < 0 || form.rating > 5) {
		errors.rating = "rating must be between 0 and 5";
	}
	if (!form.priceCategory) errors.priceCategory = "Price category is required";

	return Object.keys(errors).length === 0;
};

const handleSubmit = async () => {
	console.log(form);
	if (!validate()) {
		console.log("form invalid", errors);
		return;
	}
	console.log("Valid form:", form);

	try {
		const categoryId = await handleCategory();
		const locationId = await handleLocation();
		await handleDiscovery(categoryId, locationId);
		router.push("/dashboard");
	} catch (err) {
		console.error("Error during submission:", err);
	}
};

const handleCategory = async () => {
	// Check if form.category is an existing category ID
	const existingCategory = categories.value.find(c => c.value === form.category);
	if (existingCategory) {
		return form.category; // Already an ID of existing category
	}

	// It's a new category name entered in the editable dropdown
	try {
		const response = await api.post(Entity.Category, { name: form.category });
		categories.value.push({ label: form.category, value: response.id });
		return response.id;
	} catch (err) {
		console.error("Error adding category:", err);
		alert("Failed to add category. Please try again.");
		throw err;
	}
};

const handleLocation = async () => {
	const existingLocationId = props.initialDiscovery?.location?.id ?? null;
	const payload = {
		placename: form.location?.placename ?? "",
		county: form.location?.county ?? "",
		postcode: form.location?.postcode ?? "",
		road: form.location?.road ?? "",
		coordinates: {
			lat: form.location?.coordinates?.lat,
			lon: form.location?.coordinates?.lon,
		},
		boundingBox: form.location?.boundingBox ?? null,
	};

	try {
		if (props.discoveryId && existingLocationId) {
			await api.patch(Entity.Location, existingLocationId, payload);
			console.log("Location updated successfully with ID:", existingLocationId);
			return existingLocationId;
		}

		if (form.location?.id) {
			await api.patch(Entity.Location, form.location.id, payload);
			console.log("Location updated successfully with ID:", form.location.id);
			return form.location.id;
		}

		const response = await api.post(Entity.Location, payload);
		console.log("Location added successfully with ID:", response.id);
		return response.id;
	} catch (err) {
		console.error("Error saving location:", err);
		alert("Failed to save location. Please try again.");
		throw err;
	}
};

const handleDiscovery = async (categoryId, locationId) => {
	const discoveryData = {
		userId: form.userId,
		name: form.name,
		categoryId,
		locationId,
		discoveredOn: form.discoveredOn,
		rating: form.rating,
		description: form.description,
		priceCategory: form.priceCategory,
	};
	console.log("Submitting discovery data:", discoveryData);

	try {
		if (props.discoveryId) {
			await api.patch(Entity.Discovery, props.discoveryId, discoveryData);
			alert("Discovery updated successfully!");
			return;
		}

		await api.post(Entity.Discovery, discoveryData);
		alert("Discovery added successfully!");
	} catch (err) {
		console.error("Error saving discovery:", err);
		alert("Failed to save discovery. Please try again.");
		throw err;
	}
};

const priceCategories = ref([
	{ label: "Expensive", value: "expensive" },
	{ label: "Normal", value: "normal" },
	{ label: "Cheap", value: "cheap" },
	{ label: "Good Value", value: "value" },
	{ label: "Free", value: "free" },
	{ label: "N/A", value: "n/a" },
]);

const capitalize = str => str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
</script>

<template>
	<div>
		<form @submit.prevent="handleSubmit" class="form">
			<div class="form-row">
				<div class="field f-grow">
					<FloatLabel variant="on">
						<InputText class="w-full" id="name" v-model="form.name" />
						<label for="name">Name</label>
					</FloatLabel>
					<Message v-if="errors.name" severity="error">{{ errors.name }}</Message>
				</div>

				<div class="field">
					<FloatLabel variant="on">
						<Select
							id="category"
							v-model="form.category"
							:options="categories"
							optionLabel="label"
							optionValue="value"
							editable
						/>
						<label for="category">Category</label>
					</FloatLabel>

					<Message v-if="errors.category" severity="error">
						{{ errors.category }}
					</Message>
				</div>
			</div>

			<div class="form-row">
				<div class="field w-full">
					<label class="search-label">Location</label>
					<FloatLabel variant="on">
						<AutoComplete
							id="locationSearch"
							class="w-full"
							inputStyle="width: 100%"
							:suggestions="locationSuggestions"
							optionLabel="label"
							@item-select="event => (form.location = event.value)"
							placeholder="Search for a location..."
							@complete="searchLocation"
						/>
						<label for="locationSearch">Search</label>
					</FloatLabel>
					<Message v-if="errors.location" severity="error">{{ errors.location }}</Message>
				</div>
			</div>

			<div
				class="form-row location-details-row"
				v-if="form.location?.coordinates?.lat && form.location?.coordinates?.lon"
			>
				<div class="location-map-section">
					<div ref="mapContainer" class="map-container"></div>
				</div>

				<div class="location-fields-section">
					<div class="form-row">
						<FloatLabel variant="on" class="f-grow">
							<InputText id="placename" v-model="form.location.placename" class="w-full" />
							<label for="placename">Place Name</label>
						</FloatLabel>
					</div>
					<div class="form-row">
						<FloatLabel variant="on" class="f-grow">
							<InputText id="county" v-model="form.location.county" class="w-full" />
							<label for="county">County</label>
						</FloatLabel>
					</div>
					<div class="form-row">
						<FloatLabel variant="on" class="f-grow">
							<InputText id="postcode" v-model="form.location.postcode" class="w-full" />
							<label for="postcode">Postcode</label>
						</FloatLabel>
					</div>
					<div class="form-row">
						<FloatLabel variant="on" class="f-grow">
							<InputText id="road" v-model="form.location.road" class="w-full" />
							<label for="road">Road</label>
						</FloatLabel>
					</div>
					<div class="form-row">
						<FloatLabel variant="on" class="w-full">
							<InputText
								id="latitude"
								v-model.number="form.location.coordinates.lat"
								type="number"
								step="0.000001"
								class="w-full"
							/>
							<label for="latitude">Latitude</label>
						</FloatLabel>
						<FloatLabel variant="on" class="w-full">
							<InputText
								id="longitude"
								v-model.number="form.location.coordinates.lon"
								type="number"
								step="0.000001"
								class="w-full"
							/>
							<label for="longitude">Longitude</label>
						</FloatLabel>
					</div>
				</div>
			</div>

			<div class="form-row">
				<div class="field">
					<FloatLabel variant="on">
						<DatePicker v-model="form.discoveredOn" showTime hourFormat="24" fluid id="discoveredOn" showClear />
						<label for="discoveredOn">Discovery date</label>
					</FloatLabel>
					<Message v-if="errors.discoveredOn" severity="error">
						{{ errors.discoveredOn }}
					</Message>
				</div>

				<div class="field">
					<FloatLabel variant="on">
						<Select
							id="priceCategory"
							style="width: 10rem"
							v-model="form.priceCategory"
							:options="priceCategories"
							optionLabel="label"
							optionValue="value"
						/>
						<label for="priceCategory">Price Category</label>
					</FloatLabel>
					<Message v-if="errors.priceCategory" severity="error">
						{{ errors.priceCategory }}
					</Message>
				</div>

				<div class="field">
					<div class="rating-container">
						<label for="rating">Rating</label>
						<Rating id="rating" v-model="form.rating" :stars="5" />
					</div>
					<Message v-if="errors.rating" severity="error">{{ errors.rating }}</Message>
				</div>
			</div>

			<div class="form-row">
				<div class="field w-full">
					<FloatLabel variant="on">
						<Textarea id="description" v-model="form.description" fluid class="r-vertical" />
						<label for="description">Description</label>
					</FloatLabel>
					<Message v-if="errors.description" severity="error">
						{{ errors.description }}
					</Message>
				</div>
			</div>

			<div class="form-row">
				<Button type="submit" :label="submitLabel" />
			</div>
		</form>
	</div>
</template>

<style scoped>
.form-row {
	display: flex;
	margin-bottom: 1rem;
	gap: 1rem;
	width: 100%;
}
.form-row:last-child {
	justify-content: flex-end;
}

.rating-container {
	display: flex;
	flex-direction: column;
}

.f-grow {
	flex-grow: 1;
}
.w-full {
	width: 100%;
}
.r-vertical {
	resize: vertical;
}

.map-container {
	width: 100%;
	height: 350px;
	border: 1px solid #ddd;
	border-radius: 4px;
	margin: 0;
	z-index: 1;
}

.location-details-row {
	gap: 1.5rem;
	align-items: flex-start;
}

.location-map-section {
	flex: 1 1 50%;
	display: flex;
	flex-direction: column;
	min-width: 280px;
}

.location-fields-section {
	flex: 1 1 50%;
	display: flex;
	flex-direction: column;
	min-width: 280px;
}

.search-label {
	display: block;
	font-size: 0.875rem;
	color: #666;
	margin-bottom: 0.5rem;
	font-weight: 500;
}

@media (max-width: 900px) {
	.location-details-row {
		flex-direction: column;
	}

	.location-map-section,
	.location-fields-section {
		flex: 1 1 100%;
	}
}
</style>
