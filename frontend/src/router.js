import { createWebHistory, createRouter } from "vue-router";

import AddData from "@/views/AddDiscovery.vue";
import Login from "@/views/Login.vue";
import Register from "@/views/Register.vue";
import Dashboard from "@/views/Dashboard.vue";
import EditDiscovery from "@/views/EditDiscovery.vue";

import { authState } from "@/state/auth.ts";

const routes = [
	{ path: "/", redirect: "/dashboard" },
	{ path: "/login", component: Login },
	{ path: "/register", component: Register },
	{ path: "/dashboard", component: Dashboard },
	{ path: "/add", component: AddData },
	{ path: "/edit/:id", component: EditDiscovery },
];

export const router = createRouter({
	history: createWebHistory(),
	routes,
});

const PUBLIC_PATHS = new Set(["/login", "/register"]);

router.beforeEach(to => {
	const isAuthenticated = Boolean(authState.value?.isLoggedIn);
	const isPublicRoute = PUBLIC_PATHS.has(to.path);

	if (!isAuthenticated && !isPublicRoute) {
		return {
			path: "/login",
			query: { redirect: to.fullPath },
		};
	}

	if (isAuthenticated && isPublicRoute) {
		return { path: "/dashboard" };
	}

	return true;
});
