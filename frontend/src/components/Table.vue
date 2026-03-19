<script setup>
import { ref, onMounted } from "vue";
import { authState } from "@/state/auth.ts";
import Button from "primevue/button";
import api, {Entity} from "@/api.js";

const userId = authState.value.id;
let discoveries = ref();

onMounted(async () => {
  const data = await api.getUserDiscoveries(userId);
  console.info(data)
  discoveries.value = data;
})

const expandedRows = ref(new Set());

const toggleRow = index => {
	expandedRows.value.has(index) ? expandedRows.value.delete(index) : expandedRows.value.add(index);
};

const isExpanded = index => expandedRows.value.has(index);
</script>

<template class="template">
	<div class="tableContainer">
		<table>
			<thead>
				<tr>
					<th>Name</th>
					<th>Category</th>
					<th>Location</th>
<!--					<th>Review</th>-->
				</tr>
			</thead>

			<tbody>
				<template v-for="(item, index) in discoveries" :key="index">
					<tr>
						<td>{{ item.name }}</td>
						<td>{{ item.category.name }}</td>
						<td>
              {{ item.location.placename }}, {{ item.location.county }}
							<Button link>
								<a
									v-if="item.location.mapsUrl"
									:href="`${item.location.mapsUrl}`"
									target="_blank"
									rel="noopener"
								>
									Google Maps
								</a>
								<a
									v-else
									:href="`https://maps.google.com/?q=${item.location.coordinates.lat},${item.location.coordinates.lon}`"
									target="_blank"
									rel="noopener"
								>
									Google Maps
								</a>
							</Button>
						</td>

<!--						<td class="review-cell">-->
<!--							<div class="stars">-->
<!--								<span v-for="starIndex in 7">-->
<!--									<span v-if="starIndex <= item.rating">★</span>-->
<!--									<span v-else>☆</span>-->
<!--								</span>-->
<!--							</div>-->
<!--						</td>-->
<!--						<td>-->
<!--							<Button text @click="toggleRow(index)">-->
<!--								{{ isExpanded(index) ? "Close" : "More info" }}-->
<!--							</Button>-->
<!--						</td>-->
					</tr>

<!--					&lt;!&ndash; Expanded dropdown row &ndash;&gt;-->
<!--					<tr v-if="isExpanded(index)" class="expanded-row">-->
<!--						<td colspan="5">-->
<!--							<p class="review-text">{{ item.description }}</p>-->
<!--							<p class="review-price"><strong>Price:</strong> {{ item.priceCategory }}</p>-->
<!--						</td>-->
<!--					</tr>-->
				</template>
			</tbody>
		</table>
	</div>
</template>

<style scoped>
.tableContainer {
	height: 100%;
}
table {
	width: 100%;
	border-collapse: collapse;
}

th,
td {
	border: 1px solid #ddd;
	padding: 0.5rem;
	text-align: left;
}

th {
	background: #f5f5f5;
}

.review-price {
	color: var(--c-grey);
}
</style>
