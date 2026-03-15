import { ref, watch } from "vue";

export function useLocalStorage<T>(
    key: string,
    initialValue: T,
    options: { deep?: boolean } = {},
) {
    const { deep = false } = options;

    // Load initial value (with JSON parse + fallback)
    let valueFromStorage: T = initialValue;
    try {
        const raw = localStorage.getItem(key);
        if (raw != null) {
            valueFromStorage = JSON.parse(raw);
            console.log("Data fetched from localStorage", key, raw);
        }
    } catch (e) {
        console.warn(`Failed to parse localStorage key "${key}"`, e);
    }

    const state = ref<T>(valueFromStorage) as { value: T };

    // Persist on change
    watch(
        state,
        newValue => {
            try {
                localStorage.setItem(key, JSON.stringify(newValue));
            } catch (e) {
                console.warn(`Failed to save localStorage key "${key}"`, e);
            }
        },
        { deep },
    );

    return state;
}
