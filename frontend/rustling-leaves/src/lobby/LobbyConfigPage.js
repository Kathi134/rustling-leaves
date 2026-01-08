import "./lobby-config.css";
import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { fetchLobby, startLobbyAndNavigate, savePlayerIdForMember } from "../shared/apiServices/lobbyService";
import { useLocation } from "react-router-dom";
import { SEASONS } from "../shared/model/seasons";
import { createGameConfigDto } from "../shared/model/gameConfigUtils";
import { useWebSocket } from "../shared/hooks/useWebSocket";

export default function LobbyConfigPage() {
    const { lobbyId } = useParams();
    const { state } = useLocation();
    const [ memberId ] = useState(state?.memberId);
    
    const [lobby, setLobby] = useState()
    const [configuredSeason, setConfiguredSeason] = useState(SEASONS[1])

    const navigate = useNavigate();

    useWebSocket(`/topic/lobby/${lobbyId}`, setLobby);
    
    useEffect(() => {
        fetchLobby(lobbyId).then(setLobby)
    }, [lobbyId])

    useEffect(() => {
        if(lobby?.gameId) {
            savePlayerIdForMember(lobby, memberId);
            navigate(`../lobby/${lobby.id}/game/`);
        }
    },[lobby, memberId, navigate])


    const handleStartClick = useCallback(() => {
        // TODO: achtung! aktuell nur frühling möglich
        console.warn("LobbyConfigPage: currently only spring supported ("+configuredSeason+" ignored.)");
        const configs = lobby?.members.map(m => createGameConfigDto(m.id, SEASONS[1]))
        startLobbyAndNavigate(lobbyId, configs, memberId, navigate)
    }, [lobby, configuredSeason, lobbyId, memberId, navigate])


    return(<div className="center">
        <h2>Lobby-Konfiguration</h2>

        <div className="vertical-container error small">
            <span className="bold">Aktuelle technische Einschränkungen</span>
            <span>Jahreszeit gleich für alle Spieler (unterschiedlich geplant)</span>
            <span>Pro Jahreszeit nur die Default-Konfiguration vom Block (zufällig geplant)</span>
            <span>Startfeld wird ausgelost (wählbar geplant, dafür prefetch des boards von backend notwendig)</span>
            <span>Nur Frühling wählbar (bis in backend configs eingetragen + scoreStrats implementiert)</span>
        </div>

        <div className="section-container gap-1">
            <h3>{lobby?.name}</h3>
            <div className="small">
                Lobby-Passcode: <span className="marked primary"> {lobby?.code} </span> (zum Beitreten Teilen)
            </div>
            <div className="top-margin">
                Spieler*innen:
                <div className="horizontal-container center">
                <table className="no-top-margin fixed-container">
                    <tbody>
                        {lobby?.members.map(m => 
                            <tr key={m.id} className={m.id === memberId ? "marked" : ""}>
                                <td>{m.name}</td>
                            </tr>
                        )}
                    </tbody>
                </table>
                </div>
                {memberId ?? "keine Member ID"}
            </div>
        </div>     

        <div className="section-container">
            <span>Jahreszeit wählen:</span>
            <div className="horizontal-container center">
                <div className="horizontal-container fixed-container gap-1">
                    {SEASONS.map(s => <>
                        <img key={s} src={`${process.env.PUBLIC_URL}/season-thumbnails/${s.toLocaleLowerCase()}.jpg`} alt={configuredSeason.toLocaleLowerCase()} width="25%"
                            className={"hoverable "+ (s===configuredSeason?"marked":"")} onClick={() => setConfiguredSeason(s)}/>
                    </>)}
                </div>
            </div>
        </div>

        <button onClick={handleStartClick}>Spiel starten</button>
    </div>)
}