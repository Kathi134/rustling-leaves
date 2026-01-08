import { useState, useEffect } from "react";
import { fetchLobby } from "../shared/apiServices/lobbyService";
import { useParams } from "react-router-dom";
import { useLocalStorage } from "../shared/hooks/useLocalStorage";
import PlayerComponent from "./PlayerComponent";
import DiceComponent from "./DiceComponent";

export default function GamePage() {
    const { lobbyId } = useParams();
    
    const [gameId, setGameId] = useState();
    const [playerId] = useLocalStorage(`lobby:${lobbyId}:playerId`, undefined);
    const [playerName, setPlayerName] = useState();
    const [diceResults, setDiceResults] = useState([]);

    // const [cards, setCards] = useState([]);
    // const [scores, setScores] = useState([]);


    useEffect(() => {
        if (!lobbyId)
            return
        fetchLobby(lobbyId)
            .then(data => {
                setGameId(data.gameId)
                setPlayerName(data.members.find(m => m.playerId === playerId)?.name)
            })
            .catch(console.error);
    }, [lobbyId, playerId]);

    if(playerId === undefined) {
        return <span className="error">Du bist keine Teilnehmer*in dieser Lobby!</span>
    }

    return(<>
        <h2>GAME {gameId}</h2>
        <DiceComponent gameId={gameId} onDiceRolled={setDiceResults} />
        <PlayerComponent playerId={playerId} playerName={playerName} gameId={gameId} diceResults={diceResults} />
    </>
    )
}