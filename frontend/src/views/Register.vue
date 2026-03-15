<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

import Form from "@/components/Form.vue";
import api, { Entity } from "@/api.js";
import Button from "primevue/button";
import { authState } from "@/state/auth.ts";

const router = useRouter();

const register = ref({
	username: "",
	password: "",
});

const errors = ref({});

const registerForm = {
	ref: register,
	fields: [
		{ name: "username", label: "Username", type: "text" },
		{ name: "password", label: "Password", type: "password" },
	],
	// Optional row grouping: rows: [["username", "password"]],
};

const handleRegister = async () => {
	errors.value = {};

	const username = register.value.username.trim();
	const password = register.value.password;

	if (!username) errors.value.username = "Username is required";
	if (!password) errors.value.password = "Password is required";

	if (Object.keys(errors.value).length > 0) {
		return;
	}

	const existingUser = await api.getUserByUsername(username, true);
	if (existingUser) {
		errors.value.username = "Username is already taken";
		return;
	}

	const newUser = await api.post(
		Entity.User,
		{
			username,
			password,
		},
		true,
	);
	authState.value = {
		isLoggedIn: true,
		username: newUser.username,
		id: String(newUser.id),
	};

	router.push("/dashboard");
};
</script>

<template>
	<div class="register">
		<Form :config="registerForm" :errors="errors" @submit="handleRegister">
			<template #actions>
				<div class="register__actions">
					<Button type="submit">Register</Button>
					<RouterLink to="/login">Login</RouterLink>
				</div>
			</template>
		</Form>
	</div>
</template>

<style scoped>
.register {
	max-width: 400px;
	margin: 2rem auto;
}

.register__actions {
	display: flex;
	align-items: center;
	gap: 0.75rem;
}
</style>
