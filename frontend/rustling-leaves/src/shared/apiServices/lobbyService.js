import { API_URL } from "../api"

export async function fetchAllLobbies() {
    return fetch(`${API_URL}/lobbies`, {method: "GET"})
        .then(r => r.json())
}

export async function fetchLobby(lobbyId) {
    return fetch(`${API_URL}/lobbies/${lobbyId}`, {method: "GET"})
        .then(r => r.json())
}

export async function createNewLobby(lobbyName) {
    const body = {
        "content": lobbyName 
    }
    return fetch(`${API_URL}/lobbies`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        })
        .then(r => r.json())
}

export async function joinLobby(lobbyId, lobbyCode, playerName) {
    const body = { 
        "lobbyCode": lobbyCode,
        "playerName": playerName
    }
    return fetch(`${API_URL}/lobbies/${lobbyId}/members`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        })
        .then(r => r.json())
}

export function joinLobbyAndNavigate(lobbyId, lobbyCode, playerName, navigate) {
    joinLobby(lobbyId, lobbyCode, playerName)
        .then(joinLobbyResponse => {
            navigate(`./lobby/${joinLobbyResponse.lobby.id}`, {state: {memberId: joinLobbyResponse.me.id}})
        })
}

export async function startLobby(lobbyId, configs) {
    console.log(configs)
    return fetch(`${API_URL}/lobbies/${lobbyId}/status`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(configs)
        })
        .then(r => r.json())
}

export function startLobbyAndNavigate(lobbyId, configs, memberId, navigate) {
    startLobby(lobbyId, configs)
        .then(lobby => savePlayerIdForMember(lobby, memberId))
        .then(lobby => navigate(`../lobby/${lobby.id}/game/`))
}

export function savePlayerIdForMember(lobby, memberId) {
    const playerId = lobby.members.find(m => m.id === memberId).playerId
    const storageKey = `lobby:${lobby.id}:playerId`;
    localStorage.setItem(storageKey, playerId);
    return lobby;
}