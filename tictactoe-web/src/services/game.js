import axios from "axios";

const newAxios = axios.create({
  baseURL: "http://localhost:9091",
});

const createGame = (data) => {
  return newAxios.post(`/game`, data);
};

const move = (id, data) => {
  return newAxios.post(`game/${id}/move`, data);
};

const getGame = (id) => {
  return newAxios.get(`/game/${id}`);
};

const getNextMove = (id, player) => {
  return newAxios.get(`/game/${id}/move?player=${player}`);
};

export { createGame, move, getGame, getNextMove };
