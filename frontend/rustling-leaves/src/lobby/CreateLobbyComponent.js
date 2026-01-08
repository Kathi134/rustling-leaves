import { useCallback, useState } from "react"
import { createNewLobby, joinLobbyAndNavigate } from "../shared/apiServices/lobbyService";
import { useNavigate } from "react-router-dom";

export default function CreateLobbyComponent({backCallback}) {
    const navigate = useNavigate()

    const [lobbyName, setLobbyName] = useState("")
    const [playerName, setPlayerName] = useState("")

    const handleCreateLobbyClick = useCallback(() => {
        createNewLobby(lobbyName)
            .then(createdLobby => joinLobbyAndNavigate(createdLobby.id, createdLobby.code, playerName, navigate))
    }, [lobbyName, playerName, navigate]);

    return(<div className="vertical-container">
        <span>Erstelle eine neue Lobby:</span>

        <form className="top-margin bottom-margin" onSubmit={(e) => { e.preventDefault(); handleCreateLobbyClick(); }}>
        <div className="vertical-container gap-1 top-margin center">
            <div className="vertical-container">
                <input className="decent-input primary" type="text" required placeholder="Lobby-Name"
                    value={lobbyName} onChange={(e) => setLobbyName(e.target.value)} />
                <label className="left">Name der Lobby</label>
            </div>
            <div className="vertical-container">
                <input className="decent-input primary" type="text" required placeholder="Spieler*innen-Name"
                    value={playerName} onChange={(e) => setPlayerName(e.target.value)} />
                <label className="left">Dein Anzeigename</label>
            </div>
            <button type="submit">Erstellen</button>
        </div>
        </form>

        <div className="top-margin"><button className="small" onClick={backCallback}>Zurück</button></div>
    </div>)
}