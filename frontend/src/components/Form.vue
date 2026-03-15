<script setup>
import { computed, isRef, unref } from "vue";

import InputText from "primevue/inputtext";
import Button from "primevue/button";
import Message from "primevue/message";
import FloatLabel from "primevue/floatlabel";
import Select from "primevue/select";
import Textarea from "primevue/textarea";

const props = defineProps({
	config: {
		type: Object,
		required: true,
	},
	errors: {
		type: Object,
		default: () => ({}),
	},
	submitLabel: {
		type: String,
		default: "Submit",
	},
	showSubmit: {
		type: Boolean,
		default: true,
	},
	submitDisabled: {
		type: Boolean,
		default: false,
	},
});

const emit = defineEmits(["submit"]);

const fields = computed(() => props.config?.fields ?? []);

const model = computed(() => {
	const source = props.config?.ref;
	if (isRef(source)) {
		return source.value;
	}
	return source ?? {};
});

const layoutRows = computed(() => {
	const configuredRows = props.config?.rows;
	if (!Array.isArray(configuredRows) || configuredRows.length === 0) {
		return fields.value.map(field => [field]);
	}

	const fieldsByName = new Map(fields.value.map(field => [field.name, field]));
	const used = new Set();
	const rows = [];

	for (const rowNames of configuredRows) {
		const row = [];
		for (const fieldName of rowNames ?? []) {
			const field = fieldsByName.get(fieldName);
			if (!field || used.has(fieldName)) {
				continue;
			}
			used.add(fieldName);
			row.push(field);
		}
		if (row.length > 0) {
			rows.push(row);
		}
	}

	for (const field of fields.value) {
		if (!used.has(field.name)) {
			rows.push([field]);
		}
	}

	return rows;
});

const resolveOptions = field => {
	if (!field || field.options == null) {
		return [];
	}
	const value = typeof field.options === "function" ? field.options() : field.options;
	return unref(value) ?? [];
};

const fieldType = field => field?.type || "text";
const fieldId = field => field?.id || field?.name;
</script>

<template>
	<form class="form" @submit.prevent="emit('submit')">
		<div v-for="(row, rowIndex) in layoutRows" :key="`row-${rowIndex}`" class="form__row">
			<div
				v-for="field in row"
				:key="field.name"
				class="form__field"
				:class="[field.class, row.length === 1 ? 'form__field--full' : '']"
			>
				<slot :name="`field-${field.name}`" :field="field" :model="model">
					<FloatLabel variant="on">
						<Select
							v-if="fieldType(field) === 'select'"
							:id="fieldId(field)"
							v-model="model[field.name]"
							:options="resolveOptions(field)"
							:optionLabel="field.optionLabel || 'label'"
							:optionValue="field.optionValue || 'value'"
							:editable="Boolean(field.editable)"
							:placeholder="field.placeholder"
						/>

						<Textarea
							v-else-if="fieldType(field) === 'textarea'"
							:id="fieldId(field)"
							v-model="model[field.name]"
							class="w-full"
							:placeholder="field.placeholder"
						/>

						<InputText
							v-else
							:id="fieldId(field)"
							v-model="model[field.name]"
							class="w-full"
							:type="fieldType(field)"
							:placeholder="field.placeholder"
						/>
						<label :for="fieldId(field)">{{ field.label }}</label>
					</FloatLabel>
				</slot>

				<Message v-if="errors[field.name]" severity="error">
					{{ errors[field.name] }}
				</Message>
			</div>
		</div>

		<div v-if="showSubmit" class="form__actions">
			<slot name="actions">
				<Button type="submit" :disabled="submitDisabled">{{ submitLabel }}</Button>
			</slot>
		</div>
	</form>
</template>

<style scoped>
.form {
	display: flex;
	flex-direction: column;
	gap: 1rem;
	margin-top: 2rem;
	max-width: 100%;
}

.form__row {
	display: flex;
	gap: 1rem;
	width: 100%;
	flex-wrap: wrap;
}

.form__field {
	flex: 1;
	min-width: 220px;
}

.form__field--full {
	flex: 1 1 100%;
	min-width: 0;
}

.form__actions {
	display: flex;
}
</style>
