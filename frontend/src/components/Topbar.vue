<script setup>
import Button from "primevue/button";
import { authState } from "@/state/auth.ts";
import { useRouter } from "vue-router";

const router = useRouter();

const logOut = () => {
	authState.value = null;
	localStorage.removeItem("auth");
	router.push("/login");
};
</script>

<template>
	<div class="topbar__container">
		<div class="topbar">
			<div class="topbar__content">
				<h1>Irish Discoveries</h1>
				<Button v-if="authState" @click="logOut">Log out</Button>
			</div>
		</div>
	</div>
</template>

<style scoped>
.topbar__container {
	height: 100%;
	display: flex;
	align-items: center;
	background-color: var(--c-green);
}

.topbar {
	max-width: 1000px;
	margin: 0 auto;
	flex-grow: 1;
}

.topbar__content {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

h1 {
	font-family: Avenir, Helvetica, Arial, sans-serif;
}
</style>
