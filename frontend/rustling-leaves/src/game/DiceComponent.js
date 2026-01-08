import { useEffect, useMemo, useState } from "react"
import { rollDiceForRound } from "../shared/apiServices/gameService";
import { useWebSocket } from "../shared/hooks/useWebSocket";
import Dice from "./Dice";

export default function DiceComponent({ gameId, roundId=0, onDiceRolled }) {
    const [dice, setDice] = useState();
    const round = useMemo(() => roundId, [roundId]);

    useEffect(() => {
        if(gameId === undefined)
            return
        rollDiceForRound(gameId, round).then(x => {
            setDice(x);
            onDiceRolled([x.green, x.white]);
        });
    }, [gameId, round, onDiceRolled]);

    useWebSocket(`/topic/game/${gameId}/dice`, setDice);


    return (<div className="vertical-container center gap-1" >
        Würfelergebnis für Runde {round + 1}:
        <div className="horizontal-container gap-1">
            <Dice option={dice?.green} backgroundColor="green" valueColor="white"/>
            <Dice option={dice?.white} backgroundColor="white" valueColor="green" />
        </div>
    </div>)
    
}