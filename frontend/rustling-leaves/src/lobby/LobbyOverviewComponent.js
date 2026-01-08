import { LOBBY_CHOICES } from "./LobbyChoices";

export default function LobbyOverviewComponent({choiceCallback}) {
    return (<div className="vertical-container gap-1">
        <span>Um zu spielen, musst du Teil einer Lobby sein.</span>
        
        <div>
            Du kannst: Lobby...
            <div className="horizontal-container center gap-1 top-margin">
                <button onClick={() => choiceCallback(LOBBY_CHOICES.create)}>erstellen</button>
                <button onClick={() => choiceCallback(LOBBY_CHOICES.join)}>beitreten</button>
            </div>
        </div>
    </div>)
}