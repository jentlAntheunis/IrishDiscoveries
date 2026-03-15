<script setup>
import { reactive, ref } from "vue";

import InputText from "primevue/inputtext";
import Button from "primevue/button";
import Message from "primevue/message";
import DatePicker from "primevue/datepicker";
import FloatLabel from "primevue/floatlabel";
import Select from "primevue/select";
import Textarea from "primevue/textarea";
import Rating from "primevue/rating";

import AutoComplete from "primevue/autocomplete";

import { addDiscovery } from "@/state/discoveries.js";
import { categories, addCategory } from "@/state/categories.js";
import { authState } from "@/state/auth.ts";

const form = reactive({
	userId: authState.value.id,
	name: "",
	category: "",
	location: "",
	discoveredOn: null,
	rating: null,
	description: "",
	priceCategory: "",
});
const errors = reactive({});

const locationSuggestions = ref([]);

const searchLocation = async event => {
	const res = await fetch(`https://nominatim.openstreetmap.org/search?format=jsonv2&addressdetails=1&q=${event.query}`);
	const data = await res.json();
	console.log("Location search results:", data);
	locationSuggestions.value = data.map(p => ({
		label: p.display_name,
		lat: p.lat,
		lon: p.lon,
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

const handleSubmit = () => {
	console.log(form);
	if (!validate()) {
		console.log("form invalid", errors);
		return;
	}
	console.log("Valid form:", form);
	// check if category exists, if not adds a new one
	if (form.category && !categories.value.some(c => c.value.toLowerCase() === form.category.toString().toLowerCase())) {
		addCategory({
			label: capitalize(form.category.toString().toLowerCase()),
			value: form.category.toString().toLowerCase(),
		});
	}

	// handle location
	const mapsUrl = form.location.lat
		? `https://www.google.com/maps?q=${form.location.lat},${form.location.lon}`
		: `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(form.location)}`;
	form.location.mapsUrl = mapsUrl;

	console.log(form);
	addDiscovery({ ...form });
};

const priceCategories = ref([
	{ label: "Expensive", value: "expensive" },
	{ label: "Normal", value: "normal" },
	{ label: "Cheap", value: "cheap" },
	{ label: "Good Value", value: "value" },
	{ label: "Free", value: "free" },
]);

const capitalize = str => str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
</script>

<template>
	<div>
		<Button link>
			<RouterLink to="/">Go back</RouterLink>
		</Button>

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
