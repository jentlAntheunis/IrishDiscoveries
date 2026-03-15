import initialCategories from "@/data/categories.json";
import { useLocalStorage } from "@/composables/useLocalStorage";

const STORAGE_KEY = "categories_v1";

export const categories = useLocalStorage(STORAGE_KEY, initialCategories || [], {
    deep: true,
});

export const addCategory = item => {
    categories.value.push(item);
};

export const removeCategory = item => {
    categories.value.splice(categories.value.indexOf(item), 1);
}
