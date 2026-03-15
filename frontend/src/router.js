import { createMemoryHistory, createRouter } from 'vue-router'

import Table from './components/Table.vue'
import AddData from "@/components/addData.vue";

const routes = [
    { path: '/', redirect: '/table', component: Table },
    { path: '/table', component: Table },
    { path: '/add', component: AddData },
]

export const router = createRouter({
    history: createMemoryHistory(),
    routes,
})