import axios from "axios";

const BASE_URL = "http://localhost:8080/api/"; //better moved to .env file

const Entity = {
	User: "users",
	Location: "locations",
	Category: "categories",
	Discovery: "discoveries",
};

async function post(entity, body) {
	const res = await axios.post(BASE_URL + entity, body);
	return res.data;
}

async function get(entity) {
	const res = await axios.get(BASE_URL + entity);
	return res.data;
}

async function getById(entity, id) {
	const res = await axios.get(BASE_URL + entity + "/" + id);
	return res.data;
}

async function patch(entity, id, body) {
	const res = await axios.patch(BASE_URL + entity + "/" + id, body);
	return res.data;
}

async function deleteById(entity, id) {
	const res = await axios.delete(BASE_URL + entity + "/" + id);
	return res.data;
}

async function getUserByUsername(username) {
	const res = await axios.get(BASE_URL + Entity.User + "/username/" + username);
	return res.data;
}

export { Entity };
export default { post, get, getById, patch, deleteById, getUserByUsername };
