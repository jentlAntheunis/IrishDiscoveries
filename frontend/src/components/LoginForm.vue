<script setup xmlns="http://www.w3.org/1999/html">
import users from "@/data/users.json"
import { authState } from "@/state/auth.ts";
import { ref } from "vue";

import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import FloatLabel from 'primevue/floatlabel'

const username = ref();
const password = ref();
const error = ref();
const confirm = ref();
const submitLabel = ref(authState.value.isLoggedIn ? 'Log out' : 'Log in');

const handleSubmit = () => {
  if(!authState.value.isLoggedIn) {
    login();
  } else {
    logout();
  }
}

const login = () => {
  const user = users.find(u => u.username === username.value && u.password === password.value);
  if (user) {
    authState.value.isLoggedIn = true;
    authState.value.username = user.username;
    authState.value.id = user.id;
    username.value = "";
    password.value = "";
    error.value = null;
    confirm.value = "Login successful!";
    submitLabel.value = "Log out"
  } else {
    error.value = "Invalid credentials";
    confirm.value = null;
  }

  setTimeout(() => { confirm.value = null; error.value = null; }, 1000);
}
const logout = () => {
  authState.value.isLoggedIn = false;
  authState.value.username = "";
  authState.value.id = "";
  error.value = null;
  confirm.value = "Logged out!";
  submitLabel.value = "Log in";
}
</script>

<template>
  <form class="form" @submit.prevent="handleSubmit">
    <div class="form__group">
      <FloatLabel variant="on">
        <InputText
            id="username"
            v-model="username"
            :disabled="authState.isLoggedIn"
        />
        <label for="username">Username</label>
      </FloatLabel>
    </div>

    <div class="form__group">
      <FloatLabel variant="on">
        <Password
            id="password"
            v-model="password"
            :disabled="authState.isLoggedIn"
            toggleMask
            :feedback="false"
        />
        <label for="password">Password</label>
      </FloatLabel>
    </div>

    <div class="form__group">
      <Button
          type="submit"
          :label="submitLabel"
          class="submit__button"
      />
    </div>

    <p v-if="error" class="feedback error">{{ error }}</p>
    <p v-if="confirm" class="feedback confirm">{{ confirm }}</p>

  </form>
</template>


<style scoped>
.form {
  display: flex;
}
.form__group {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-right: 2rem;
}

.submit__button {
  margin-top: auto;
}

.feedback {
  align-self: flex-end;
}
.error {
  color: red;
}
.confirm {
  color: var(--c-green-dark-1);
}
</style>