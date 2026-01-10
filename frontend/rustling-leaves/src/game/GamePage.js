import { useState, useEffect, useCallback } from "react";
import { fetchLobby } from "../shared/apiServices/lobbyService";
import { useParams } from "react-router-dom";
import { useLocalStorage } from "../shared/hooks/useLocalStorage";
import PlayerComponent from "./PlayerComponent";
import DiceComponent from "./DiceComponent";
import { storePlayerMove, getPlayerCard } from "../shared/apiServices/gameService";

export default function GamePage() {
    const { lobbyId } = useParams();
    
    const [gameId, setGameId] = useState();
    const [playerId] = useLocalStorage(`lobby:${lobbyId}:playerId`, undefined);
    const [playerName, setPlayerName] = useState();
    const [playerCard, setPlayerCard] = useState();
    
    const [diceResults, setDiceResults] = useState([]);

    const [round, setRound] = useState(0); // todo: potentially in game object retrievbar
 
    useEffect(() => {
        if(gameId && playerId)
            getPlayerCard(gameId, playerId).then(setPlayerCard)
    }, [gameId, playerId])

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
    }, [lobbyId, playerId]);


    const handleSaveClick = useCallback((area, type) => {
        storePlayerMove(gameId, playerId, round, area.topLeft, area.bottomRight, type)
            .then(setPlayerCard)
            .then(() => setRound(r => r+1))
    }, [gameId, playerId, round])



    if(playerId === undefined) {
        return <span className="error">Du bist keine Teilnehmer*in dieser Lobby!</span>
    }

    return(<>
        <h2>GAME {gameId}</h2>
        <DiceComponent gameId={gameId} onDiceRolled={setDiceResults} roundId={round} />
        <PlayerComponent playerId={playerId} playerName={playerName} gameId={gameId}
            card={playerCard} diceResults={diceResults} onSave={handleSaveClick} />
    </>
    )
}