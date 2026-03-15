import axios from "axios";

const BASE_URL = "http://localhost:8080/api/"; //better moved to .env file

const Entity = {
	User: "users",
	Location: "locations",
	Category: "categories",
	Discovery: "discoveries",
};

async function post(entity, body, log = false) {
	const res = await axios.post(BASE_URL + entity, body);
	if (log) {
		console.log("POST", BASE_URL + entity, body);
	}
	return res.data;
}

async function get(entity, log = false) {
	const res = await axios.get(BASE_URL + entity);
	if (log) {
		console.log("GET", BASE_URL + entity);
	}
	return res.data;
}

async function getById(entity, id, log = false) {
	const res = await axios.get(BASE_URL + entity + "/" + id);
	if (log) {
		console.log("GET", BASE_URL + entity + "/" + id);
	}
	return res.data;
}

async function patch(entity, id, body, log = false) {
	const res = await axios.patch(BASE_URL + entity + "/" + id, body);
	if (log) {
		console.log("PATCH", BASE_URL + entity + "/" + id, body);
	}
	return res.data;
}

async function deleteById(entity, id, log = false) {
	const res = await axios.delete(BASE_URL + entity + "/" + id);
	if (log) {
		console.log("DELETE", BASE_URL + entity + "/" + id);
	}
	return res.data;
}

async function getUserByUsername(username, log = false) {
	try {
		const res = await axios.get(BASE_URL + Entity.User + "/username/" + encodeURIComponent(username));
		if (log) {
			console.log("GET", BASE_URL + Entity.User + "/username/" + encodeURIComponent(username));
		}
		return res.data;
	} catch (error) {
		if (axios.isAxiosError(error) && error.response?.status === 404) {
			return null;
		}

		throw error;
	}
}

export { Entity };
export default { post, get, getById, patch, deleteById, getUserByUsername };
