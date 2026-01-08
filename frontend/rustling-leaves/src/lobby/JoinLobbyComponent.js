import { useCallback, useEffect, useState } from "react"
import { fetchAllLobbies } from "../shared/apiServices/lobbyService"
import { joinLobbyAndNavigate } from "../shared/apiServices/lobbyService"
import { useNavigate } from "react-router-dom"
import SpreadTextInput from "../shared/components/SpreadTextInput"

export default function JoinLobbyComponent({backCallback}) {
    const [lobbies, setLobbies] = useState([])
    const [selected, setSelected] = useState({})
    const [playerName, setPlayerName] = useState("")
    const [passCode, setPassCode] = useState(Array.from({ length: 8 }, () => ""))

    const navigate = useNavigate();

    useEffect(() => {
        fetchAllLobbies().then(setLobbies)
    }, [setLobbies])

    const handleJoinLobbyClick = useCallback(() => {
        joinLobbyAndNavigate(selected.id, passCode.join(""), playerName, navigate)
    }, [playerName, selected.id, passCode, navigate])

    return(<div className="vertical-container">
        <span>Wähle eine existierende Lobby zum Beitreten:</span>

        <div className="horizontal-container center">
            <table className="fixed-container hoverable">
                <tbody className="">
                    {lobbies.map(i => 
                        <tr key={i.id} className={selected?.id === i.id ? "marked" : ""} onClick={() => setSelected(i)}>
                            <td>{i.name}</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>

        <form className="top-margin bottom-margin" onSubmit={(e) => { e.preventDefault(); handleJoinLobbyClick(); }}>
        <div className="vertical-container gap-1 top-margin center">
            <div className="vertical-container">
                <input className="decent-input primary" type="text" required placeholder="Spieler*innen-Name"
                    value={playerName} onChange={(e) => setPlayerName(e.target.value)} />
                <label className="left">Dein Anzeigename</label>
            </div>
            <div className="vertical-container">
                <SpreadTextInput type="text" inputClassName="decent-input primary"
                    values={passCode} onChange={(value, index, values) => setPassCode(values)} />
                <label className="left">Lobby Pass-Code</label>
            </div>
            <input type="hidden" value={selected} />
            <button type="submit">Beitreten</button>
        </div>
        </form>
        {selected.code}

        <div><button className="small" onClick={backCallback}>Zurück</button></div>
    </div>)


}