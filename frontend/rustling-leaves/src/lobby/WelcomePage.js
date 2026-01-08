import LobbyOverviewComponent from "./LobbyOverviewComponent";
import JoinLobbyComponent from "./JoinLobbyComponent";
import CreateLobbyComponent from "./CreateLobbyComponent";
import { useState } from "react";
import { LOBBY_CHOICES } from "./LobbyChoices";

export default function WelcomePage() {
    const [lobbyChoice, setLobbyChoice] = useState(LOBBY_CHOICES.open)

    return (<div className="center">
        <h1>Blätterrauschen</h1>
        <div className="section-container">
            <h2>Lobby</h2>
            { lobbyChoice === LOBBY_CHOICES.open && <LobbyOverviewComponent choiceCallback={setLobbyChoice}/> }
            { lobbyChoice === LOBBY_CHOICES.join && <JoinLobbyComponent backCallback={() => setLobbyChoice(LOBBY_CHOICES.open)}/> }
            { lobbyChoice === LOBBY_CHOICES.create && <CreateLobbyComponent backCallback={() => setLobbyChoice(LOBBY_CHOICES.open)}/> }
        </div>

        <div className="section-container">
            <h2>Regeln</h2>
            <span>This is Blätterrauschen. Here is how you play. Cool icons bla bla.</span>
        </div>
    </div>)
}