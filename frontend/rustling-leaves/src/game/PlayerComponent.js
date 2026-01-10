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


export default function PlayerComponent({ playerId, playerName, gameId, globalRound, clickEventsEnabled, card, diceResults, onSave }) {
    const [action, dispatchAction] = useReducer(actionReducer, actions.DRAW);

    useEffect(() => {
        dispatchAction(events.NEW_ROUND_STARTED)
    }, [globalRound])

    // reset if rotated or falsely drawn
    const [resetCounter, setResetCounter] = useState(0);
    const resetUnsetEvent = useCallback(() => setResetCounter(0), [setResetCounter]);
    const onRotated = useCallback(() => setResetCounter(c => c+1), [setResetCounter]);
    useEffect(() => setResetCounter(c => (action === actions.DRAW) ? c+1 : 0), [action])


    // draw
    const diceValues = useMemo(() => diceResults ? diceResults.map(getOptionValue) : [], [diceResults]);

    const onDrawn = (success) => dispatchAction(success ? events.AREA_VALID : events.AREA_INVALID)
    const { pendingRectangle, pendingRectangleAllowed, hints, allowedTypes, draw, rotate, unset: unsetRectangle } =
        useRectangleDrawing({gameId, playerId, card, diceValues, onDrawn, onRotated})


    // tick
    const onTicked = (success) => dispatchAction(success ? events.TYPE_VALID : events.TYPE_INVALID)
    const { tickedType, tickedPoints, tickError, tick } = 
        useTickType({gameId, playerId, card, area: pendingRectangle, allowedTypes, onTicked, unset: resetCounter, resetUnsetEvent })
   
    
    // handle cell click according to state
    const tickIfInArea = useCallback((point) => 
        containsPoint(pendingRectangle, point) ? tick(point) : draw(point)
    , [tick, draw, pendingRectangle])


    const onCellClick = useMemo(() => {
        if(!clickEventsEnabled)
            return () => {}
        if(action === actions.DRAW) 
            return draw
        if(action === actions.TICK || action === actions.CONFIRM)
            return tickIfInArea
    }, [action, draw, tickIfInArea, clickEventsEnabled]);

    const onSaveClick = useCallback(() => {
        onSave(pendingRectangle, tickedType)
        dispatchAction(events.CONFIRMED)
        unsetRectangle(); 
    }, [unsetRectangle, onSave, pendingRectangle, tickedType])

    return (
        <div className="vertical-container center">
            <h3>{playerName}s Spiel-Karte</h3>

            {clickEventsEnabled &&
                <ActionPanel action={action} diceValues={diceValues}
                    drawHints={hints} pendingRectangleAllowed={pendingRectangleAllowed} onRotateRectangle={() => rotate(pendingRectangle?.topLeft)}
                    tickError={tickError}
                    onSave={onSaveClick} />
            }

            {card && <>
                <FieldMatrix board={card.boardTemplate} onCellClick={onCellClick} areas={card.areas}
                    pendingRectangle={pendingRectangle} pendingRectangleAllowed={pendingRectangleAllowed} 
                    tickedPoints={tickedPoints} tickedType={tickedType} />
                <ScoringTags tags={card.scoringTags} />
            </>}
            <span>{card?.numberOfRoundsPlayed} gespielte Runde(n)</span>
            
        </div>
    );
}