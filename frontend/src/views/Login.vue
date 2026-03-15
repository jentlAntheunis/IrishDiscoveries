<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

import Form from "@/components/Form.vue";
import api from "@/api.js";
import Button from "primevue/button";
import { authState } from "@/state/auth.ts";

const router = useRouter();

const login = ref({
	username: "",
	password: "",
});

const errors = ref({});

const loginForm = {
	ref: login,
	fields: [
		{ name: "username", label: "Username", type: "text" },
		{ name: "password", label: "Password", type: "password" },
	],
	// Optional row grouping: rows: [["username", "password"]],
};

const handleLogin = async () => {
	errors.value = {};

	const username = login.value.username.trim();
	const password = login.value.password;

	if (!username) errors.value.username = "Username is required";
	if (!password) errors.value.password = "Password is required";

	if (Object.keys(errors.value).length > 0) {
		return;
	}

	const matchingUser = await api.getUserByUsername(username);

	if (!matchingUser || matchingUser.password !== password) {
		errors.value.password = "Invalid username or password";
		return;
	}

	authState.value = {
		isLoggedIn: true,
		username: matchingUser.username,
		id: String(matchingUser.id),
	};

	router.push("/dashboard");
};
</script>

<template>
	<div class="login">
		<Form :config="loginForm" :errors="errors" @submit="handleLogin">
			<template #actions>
				<div class="login__actions">
					<Button type="submit">Login</Button>
					<RouterLink to="/register">Register</RouterLink>
				</div>
			</template>
		</Form>
	</div>
</template>

<style scoped>
.login {
	max-width: 400px;
	margin: 2rem auto;
}

.login__actions {
	display: flex;
	align-items: center;
	gap: 0.75rem;
}
</style>
