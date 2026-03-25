<script setup>
import { computed, ref } from "vue";

/**
 * columns: Array<string | { label: string, field: string, formatter?: (v,row)=>any, sortKey?: string, defaultSortDirection?: string }>
 * rows: Array<object>
 * keyField: string default 'id'
 */
const props = defineProps({
	columns: { type: Array, required: true },
	rows: { type: Array, required: true },
	keyField: { type: String, default: "id" },
	showHeaders: { type: Boolean, default: true },
});

const sortField = ref(null);
const sortDirection = ref("asc"); // "asc" or "desc"

const parsedColumns = computed(() => {
	return props.columns.map(col => {
		if (typeof col === "string") return { label: col, field: col };
		return {
			label: col.label ?? col.field,
			field: col.field,
			formatter: col.formatter,
			sortKey: col.sortKey,
			defaultSortDirection: col.defaultSortDirection ?? "asc",
		};
	});
});

const getValueForSort = (obj, sortKey) => {
	if (!sortKey) return obj;
	const keys = sortKey.split(".");
	let value = obj;
	for (const key of keys) {
		value = value?.[key];
	}
	return value;
};

const sortedRows = computed(() => {
	if (!sortField.value) return props.rows;

	const column = parsedColumns.value.find(col => col.field === sortField.value);
	const sortKey = column?.sortKey || sortField.value;

	const sorted = [...props.rows].sort((a, b) => {
		const aVal = getValueForSort(a, sortKey);
		const bVal = getValueForSort(b, sortKey);

		if (aVal == null && bVal == null) return 0;
		if (aVal == null) return 1;
		if (bVal == null) return -1;

		if (typeof aVal === "string") {
			const comparison = aVal.localeCompare(bVal);
			return sortDirection.value === "asc" ? comparison : -comparison;
		}

		if (aVal < bVal) return sortDirection.value === "asc" ? -1 : 1;
		if (aVal > bVal) return sortDirection.value === "asc" ? 1 : -1;
		return 0;
	});

	return sorted;
});

const handleSort = field => {
	if (sortField.value === field) {
		sortDirection.value = sortDirection.value === "asc" ? "desc" : "asc";
	} else {
		const column = parsedColumns.value.find(col => col.field === field);
		sortField.value = field;
		sortDirection.value = column?.defaultSortDirection ?? "asc";
	}
};
</script>

<template>
	<table class="data-table">
		<thead v-if="showHeaders">
			<tr>
				<th v-for="col in parsedColumns" :key="col.field" @click="handleSort(col.field)" class="sortable-header">
					<span class="header-content">
						{{ col.label }}
						<span class="sort-indicator" v-if="sortField === col.field">
							{{ sortDirection === "asc" ? "▲" : "▼" }}
						</span>
					</span>
				</th>
				<th class="actions-header"></th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="row in sortedRows" :key="row[props.keyField]">
				<td v-for="col in parsedColumns" :key="col.field">
					<slot name="cell" :row="row" :column="col">
						{{ col.formatter ? col.formatter(row[col.field], row) : row[col.field] }}
					</slot>
				</td>
				<td v-if="$slots.actions" class="actions-cell">
					<slot name="actions" :row="row" />
				</td>
			</tr>
		</tbody>
	</table>
</template>

<style scoped>
.data-table {
	width: 100%;
	border-collapse: separate;
	border-spacing: 0;
	overflow: hidden;
	border-radius: 10px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.data-table th {
	text-align: left;
	padding: 0.5rem 1rem;
	background-color: var(--c-orange);
	color: #ffffff;
	font-weight: 600;
}

.sortable-header {
	cursor: pointer;
	user-select: none;
	transition: background-color 0.2s ease;
}

.sortable-header:hover {
	background-color: var(--c-orange-dark, #cc6600);
}

.header-content {
	display: flex;
	align-items: center;
	gap: 0.4rem;
}

.sort-indicator {
	font-size: 0.9em;
	display: inline-block;
	margin-left: 0.25rem;
	transition: transform 0.2s ease;
}

.data-table td {
	padding: 12px 14px;
}

.data-table tbody tr:nth-child(even) {
	background-color: var(--c-green-lightest);
}

.data-table tbody tr:nth-child(odd) {
	background-color: #ffffff;
}

.data-table tbody tr:hover {
	background-color: #d5f5e4;
}

.actions-header,
.actions-cell {
	text-align: right;
}
</style>
