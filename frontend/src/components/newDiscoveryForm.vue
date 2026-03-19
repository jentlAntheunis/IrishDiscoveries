<script setup>
import { onMounted, reactive, ref } from "vue";

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
import { router } from "@/router";

const form = reactive({
	userId: authState.value.id,
	name: "",
	category: "",
	location: {},
	discoveredOn: null,
	rating: null,
	description: "",
	priceCategory: "",
});
const errors = reactive({});

const locationSuggestions = ref([]);
const categories = ref([]);

onMounted(async () => {
	const data = await api.get(Entity.Category);
	categories.value = data.map(c => ({ label: c.name, value: c.id }));
});

const searchLocation = async event => {
	const res = await fetch(`https://nominatim.openstreetmap.org/search?format=jsonv2&addressdetails=1&q=${event.query}`);
	const data = await res.json();
	console.log("Location search results:", data);
	locationSuggestions.value = data.map(p => ({
		label: p.display_name,
		coordinates: {
			lat: p.lat,
			lon: p.lon,
		},
		placename: p.address.city || p.address.town || p.address.village || p.address.civil_parish || "",
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
	if (!form.location) errors.location = "Location is required";
	if (!form.discoveredOn) errors.discoveredOn = "Discovery date is required";

	if (form.rating === null || form.rating < 0 || form.rating > 7) {
		errors.rating = "rating must be between 0 and 7";
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
		// router.push("/dashboard");
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
	try {
		const response = await api.post(Entity.Location, form.location);
		console.log("Location added successfully with ID:", response.id);
		return response.id;
	} catch (err) {
		console.error("Error adding location:", err);
		alert("Failed to add location. Please try again.");
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
		await api.post(Entity.Discovery, discoveryData);
		alert("Discovery added successfully!");
	} catch (err) {
		console.error("Error adding discovery:", err);
		alert("Failed to add discovery. Please try again.");
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
					<FloatLabel variant="on">
						<AutoComplete
							id="location"
							class="w-full"
							inputStyle="width: 100%"
							v-model="form.location"
							:suggestions="locationSuggestions"
							optionLabel="label"
							@complete="searchLocation"
						/>
						<label for="location">Location</label>
					</FloatLabel>
					<Message v-if="errors.location" severity="error">{{ errors.location }}</Message>
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
						<Rating id="rating" v-model="form.rating" :stars="7" />
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
				<Button type="submit" label="Submit" />
			</div>
		</form>
	</div>
</template>

<style scoped>
.form {
	margin-top: 2rem;
}

.form-row {
	display: flex;
	margin-top: 1rem;
	margin-bottom: 2rem;
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
</style>
