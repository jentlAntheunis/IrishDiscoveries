import { createWebHistory, createRouter } from "vue-router";

import Table from "./components/Table.vue";
import AddData from "@/components/addData.vue";
import Login from "@/views/Login.vue";
import Register from "@/views/Register.vue";

const routes = [
	{ path: "/", redirect: "/login", component: Login },
	{ path: "/login", component: Login },
	{ path: "/register", component: Register },
	{ path: "/dashboard", component: Table },
	{ path: "/add", component: AddData },
];

export const router = createRouter({
	history: createWebHistory(),
	routes,
});
