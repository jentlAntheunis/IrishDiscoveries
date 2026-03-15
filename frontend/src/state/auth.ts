import { ref, watch } from 'vue'

type AuthState = {
    isLoggedIn: boolean
    username: string | null
    id: string | null
}

const stored = localStorage.getItem('auth')

const initialState: AuthState = stored
    ? JSON.parse(stored)
    : { isLoggedIn: false, username: null, id: null }

export const authState = ref<AuthState>(initialState)

watch(
    authState,
    (value) => {
        localStorage.setItem('auth', JSON.stringify(value))
    },
    { deep: true }
)
