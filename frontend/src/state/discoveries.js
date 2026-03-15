import initialDiscoveries from "@/data/discoveries.json";
import { useLocalStorage } from "@/composables/useLocalStorage";

const STORAGE_KEY = "discoveries_v1";

export const discoveries = useLocalStorage(STORAGE_KEY, initialDiscoveries || [], {
	deep: true,
});

export const addDiscovery = item => {
	discoveries.value.push(item);
};

export const removeDiscovery = item => {
	discoveries.value.splice(discoveries.value.indexOf(item), 1);
}