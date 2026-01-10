import { useCallback, useEffect, useMemo, useState } from "react";
import FieldMatrix from "./Board/FieldMatrix/FieldMatrix";
import ScoringTags from "./Board/ScoringTags/ScoringTags";
import { getOptionValue } from "../shared/model/diceUtils";
import { useReducer } from "react";
import { actions, actionReducer, events } from "./Board/FieldMatrix/Action";
import useRectangleDrawing from "./Board/FieldMatrix/DrawRectangleHook";
import useTickType from "./Board/FieldMatrix/TickTypeHook";
import { containsPoint } from "../shared/model/areaUtils";
import ActionPanel from "./Board/FieldMatrix/ActionPanel";


export default function PlayerComponent({ playerId, playerName, gameId, card, diceResults, onSave }) {
    const [action, dispatchAction] = useReducer(actionReducer, actions.DRAW);

    // draw
    const diceValues = useMemo(() => diceResults ? diceResults.map(getOptionValue) : [], [diceResults]);

    const onDrawn = (success) => dispatchAction(success ? events.AREA_VALID : events.AREA_INVALID)
    const { pendingRectangle, pendingRectangleAllowed, hints, allowedTypes, rotatedRectangle, draw, rotate } =
        useRectangleDrawing({gameId, playerId, card, diceValues, onDrawn})


    // reset if rotated or falsely drawn
    const [resetCounter, setResetCounter] = useState(0);
    const resetUnsetEvent = useCallback(() => setResetCounter(0), [setResetCounter]);
    useEffect(() => {
        setResetCounter(c => (action === actions.DRAW || rotatedRectangle) ? c+1 : 0);
    }, [action, rotatedRectangle]);

    useEffect(() => console.log(resetCounter, action), [resetCounter, action]);

    // tick
    const onTicked = (success) => dispatchAction(success ? events.TYPE_VALID : events.TYPE_INVALID)
    const { tickedType, tickedPoints, tickError, tick } = 
        useTickType({gameId, playerId, card, area: pendingRectangle, allowedTypes, onTicked, unset: resetCounter, resetUnsetEvent })
   
    
    // handle cell click according to state
    const tickIfInArea = useCallback((point) => 
        containsPoint(pendingRectangle, point) ? tick(point) : draw(point)
    , [tick, draw, pendingRectangle])

    const onCellClick = useMemo(() => {
        if(action === actions.DRAW) 
            return draw
        if(action === actions.TICK || action === actions.CONFIRM)
            return tickIfInArea
    }, [action, draw, tickIfInArea]);


    return (
        <div className="vertical-container center">
            <h3>{playerName}s Spiel-Karte</h3>

            <ActionPanel action={action} diceValues={diceValues}
                drawHints={hints} pendingRectangleAllowed={pendingRectangleAllowed} onRotateRectangle={() => rotate(pendingRectangle?.topLeft)}
                tickError={tickError}
                onSave={() => onSave(pendingRectangle, tickedType)} />

            {card &&  <>
                <FieldMatrix board={card.boardTemplate} onCellClick={onCellClick} areas={card.areas}
                    pendingRectangle={pendingRectangle} pendingRectangleAllowed={pendingRectangleAllowed} 
                    tickedPoints={tickedPoints} tickedType={tickedType} />
                <ScoringTags tags={card.scoringTags} />
            </>}
            <span>{card?.numberOfRoundsPlayed} gespielte Runde(n)</span>
            
        </div>
    );
}