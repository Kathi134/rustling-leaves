import { API_URL } from "../api";

export async function getPlayerCard(gameId, playerId) {
    return fetch(`${API_URL}/games/${gameId}/players/${playerId}/card`, {
            method: "GET",
            headers: { "Accept": "application/json" }
        })
        .then(r => r.json())
}

export async function rollDiceForRound(gameId, roundId) {
    return fetch(`${API_URL}/games/${gameId}/rounds/${roundId}/dice`, {
            method: "PUT",
            headers: { "Accept": "application/json" }
        })
        .then(r => r.json());
}


export async function isAreaValid(gameId, playerId, topLeft, bottomRight) {
    const body = {
        topLeft,
        bottomRight
    };

    return fetch(`${API_URL}/games/${gameId}/players/${playerId}/areas/valid`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(body)
        })
        .then(r => r.json());
}

export async function getTickableFieldTypes(gameId, playerId, topLeft, bottomRight) {
    const body = {
        topLeft,
        bottomRight
    };

    return fetch(`${API_URL}/games/${gameId}/players/${playerId}/areas/tickable-types`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(body)
        })
        .then(r => r.json());
}

export async function getPointsOfTypeInArea(gameId, playerId, rectangle, type) {
    const body = {
        "type": {
            "enumName": type
        },
        "rectangle": rectangle
    };
    return fetch(`${API_URL}/games/${gameId}/players/${playerId}/areas/points-of-type`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(body)
        })
        .then(r => r.json())
}


export async function storePlayerMove(gameId, playerId, roundId, topLeft, bottomRight, enumName) {
    const body = {
        roundId,
        rectangle: {
            topLeft,
            bottomRight
        },
        type: {
            enumName
        }
    };

    return fetch(`${API_URL}/games/${gameId}/players/${playerId}/moves`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(body)
        })
        .then(r => r.json());
}




export async function quitGame(gameId, playerId, roundId) {
    const body = {
        roundId
    };

    return fetch(`${API_URL}/games/${gameId}/players/${playerId}/stop`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(body)
        })
        .then(r => r.json());
}


