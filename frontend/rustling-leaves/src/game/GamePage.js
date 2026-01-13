import { useState, useEffect, useCallback } from "react";
import { fetchLobby } from "../shared/apiServices/lobbyService";
import { useParams } from "react-router-dom";
import { useLocalStorage } from "../shared/hooks/useLocalStorage";
import PlayerComponent from "./PlayerComponent";
import DiceComponent from "./DiceComponent";
import { storePlayerMove, storePlayerMiss, getPlayerCard, getRound, quitGame } from "../shared/apiServices/gameService";
import StopComponent from "./StopComponent";


export default function GamePage() {
    const { lobbyId } = useParams();
    
    const [gameId, setGameId] = useState();
    const [playerId] = useLocalStorage(`lobby:${lobbyId}:playerId`, undefined);
    const [playerName, setPlayerName] = useState();
    const [playerCard, setPlayerCard] = useState();
    
    const [diceResults, setDiceResults] = useState([]);

    const [round, setRound] = useState(0); // todo: potentially in game object retrievbar
    const [pendingContinueAnswer, setPendingContinueAnswer] = useState(true)
    const [continueGame, setContinueGame] = useState(false);
 
    useEffect(() => {
        if(gameId && playerId) {
            getRound(gameId).then(setRound)
            getPlayerCard(gameId, playerId).then(setPlayerCard)
        }
    }, [gameId, playerId])

    useEffect(() => {
        if (!lobbyId)
            return
        fetchLobby(lobbyId)
            .then(data => {
                setGameId(data.gameId)
                setPlayerName(data.members.find(m => m.playerId === playerId)?.name)
            })
    }, [lobbyId, playerId]);

    useEffect(() => {
        setPendingContinueAnswer(true)
        setContinueGame(false)
    }, [round])

    const handleStopAnswer = useCallback(stop => {
        setPendingContinueAnswer(false)
        setContinueGame(!stop)
        if(stop) {
            quitGame(gameId, playerId, round)
                .then(setPlayerCard)
        } 
    }, [setPendingContinueAnswer, setContinueGame, setPlayerCard, gameId, playerId, round])


    const handleSaveClick = useCallback((area, type) => {
        if(area && type)
            storePlayerMove(gameId, playerId, round, area.topLeft, area.bottomRight, type)
                .then(setPlayerCard)
                .then(() => setRound(r => r+1))
        else
            storePlayerMiss(gameId, playerId, round)
                .then(setPlayerCard)
                .then(() => setRound(r => r+1))
    }, [gameId, playerId, round])



    if(playerId === undefined) {
        return <span className="error">Du bist keine Teilnehmer*in dieser Lobby!</span>
    }

    return(<>
        <h2>GAME {gameId}</h2>
        {playerCard?.hasStopped
            ? <div className="center">Du bist ausgestiegen. Warte auf die anderen für die Wertung.</div>
            : pendingContinueAnswer ?  <StopComponent onAnswer={handleStopAnswer} />
            : continueGame && <DiceComponent gameId={gameId} onDiceRolled={setDiceResults} roundId={round} />
        }
        <PlayerComponent playerId={playerId} playerName={playerName} gameId={gameId} globalRound={round}
            card={playerCard} diceResults={diceResults} onSave={handleSaveClick} clickEventsEnabled={continueGame} />
    </>
    )
}

