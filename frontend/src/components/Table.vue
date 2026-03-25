<script setup>
import { computed } from "vue";

/**
 * columns: Array<string | { label: string, field: string, formatter?: (v,row)=>any }>
 * rows: Array<object>
 * keyField: string default 'id'
 */
const props = defineProps({
	columns: { type: Array, required: true },
	rows: { type: Array, required: true },
	keyField: { type: String, default: "id" },
	showHeaders: { type: Boolean, default: true },
});

const parsedColumns = computed(() => {
	return props.columns.map(col => {
		if (typeof col === "string") return { label: col, field: col };
		return { label: col.label ?? col.field, field: col.field, formatter: col.formatter };
	});
});
</script>

<template>
	<table class="data-table">
		<thead v-if="showHeaders">
			<tr>
				<th v-for="col in parsedColumns" :key="col.field">{{ col.label }}</th>
				<th class="actions-header"></th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="row in rows" :key="row[props.keyField]">
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
