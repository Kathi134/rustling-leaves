import { useCallback, useEffect, useMemo, useState } from "react";
import { getPlayerCard, getTickableFieldTypes, isAreaValid, getPointsOfTypeInArea } from "../shared/apiServices/gameService";
import FieldMatrix from "./Board/FieldMatrix/FieldMatrix";
import ScoringTags from "./Board/ScoringTags/ScoringTags";
import { getOptionValue } from "../shared/model/diceUtils";
import DrawRectangle from "./Board/FieldMatrix/DrawRectangle";
import TickType from "./Board/FieldMatrix/TickType";
import { equals, createAreaFromTopLeftAndDimensions, containsPoint } from "../shared/model/areaUtils";
import { getEnumTypeFromPoint } from "../shared/model/boardUtils";
import { useReducer } from "react";
import { actions, actionReducer, events } from "./Board/FieldMatrix/action-utils";


export default function PlayerComponent({ playerId, playerName, gameId, diceResults }) {
    const [card, setCard] = useState();
    const [action, dispatchAction] = useReducer(actionReducer, actions.DRAW);


    useEffect(() => {
        if(gameId === undefined || playerId === undefined) 
            return
        getPlayerCard(gameId, playerId).then(setCard)
    }, [gameId, playerId])


    /* drawing */
    const diceValues = useMemo(() => {
        if(diceResults)
            return diceResults?.map(getOptionValue);
        return [];
    }, [diceResults]);
    const [pendingRectangle, setPendingRectangle] = useState();
    const [pendingRectangleAllowed, setPendingRectangleAllowed] = useState(true);
    const [hints, setHints] = useState("");

        /* ticking */
    const [allowedTypes, setAllowedTypes] = useState();
    const [tickedType, setTickedType] = useState();
    const [tickedPoints, setTickedPoints] = useState([]);
    const [tickError, setTickError] = useState(false)

    const unsetTickedType = useCallback(() => {
        setTickedType(undefined)
        setTickedPoints([])
    }, [setTickedType, setTickedPoints])


    const onPendingRectangleChange = useCallback((updateValue) => {
        if(equals(pendingRectangle, updateValue))
            return;
        setPendingRectangle(updateValue);

        if(action === actions.TICK) {
            setTickedType(undefined);
        }

        isAreaValid(gameId, playerId, updateValue.topLeft, updateValue.bottomRight)
            .then(isValid => {
                if (isValid === true) {
                    setHints([]);
                    setPendingRectangleAllowed(true);
                    getTickableFieldTypes(gameId, playerId, updateValue.topLeft, updateValue.bottomRight)
                        .then(setAllowedTypes)
                        .then(() => dispatchAction(events.AREA_VALID));
                } else {
                    dispatchAction(events.AREA_INVALID);
                    setHints(isValid)
                    setPendingRectangleAllowed(false);
                }
            });   
    }, [pendingRectangle, action, gameId, playerId]);

    const handleCellClickInDrawState = useCallback((_, rowIndex, colIndex) => {
        if(card?.boardTemplate === undefined || diceValues === undefined)
            return;

        unsetTickedType();
        const point = {x: colIndex, y: rowIndex};
        const area = createAreaFromTopLeftAndDimensions(point, card?.boardTemplate, diceValues);

        onPendingRectangleChange(area)
    }, [card, diceValues, onPendingRectangleChange, unsetTickedType]);    

   
    const onOutsideAreaClick = useMemo(() => {
        if(action === actions.TICK || action === actions.CONFIRM)
            return handleCellClickInDrawState
    }, [action, handleCellClickInDrawState]);




   
    const handleCellClickInTickState = useCallback((_, rowIndex, colIndex) => {
        if(card?.boardTemplate === undefined || allowedTypes === undefined)
            return;
        
        const point = {x: colIndex, y: rowIndex};
        if(!containsPoint(pendingRectangle, point)) {
            onOutsideAreaClick(_, rowIndex, colIndex);
            return;
        }
        
        const type = getEnumTypeFromPoint(card?.boardTemplate, point);

        if(!allowedTypes.some(x => x.enumName === type)) {
            dispatchAction(events.TYPE_INVALID)
            unsetTickedType();
            setTickError(true);
        } else {
            setTickError(false);
            setTickedType(type);
            getPointsOfTypeInArea(gameId, playerId, pendingRectangle, type)
                .then(setTickedPoints)
                .then(() => dispatchAction(events.TYPE_VALID))
        }
    }, [allowedTypes, card, gameId, onOutsideAreaClick, pendingRectangle, playerId, unsetTickedType]);


    const onCellClick = useMemo(() => {
        if(action === actions.DRAW) 
            return handleCellClickInDrawState
        if(action === actions.TICK || action === actions.CONFIRM)
            return handleCellClickInTickState
    }, [action, handleCellClickInDrawState, handleCellClickInTickState]);

    

    return (
        <div className="vertical-container center">
            <h3>{playerName}s Spiel-Karte</h3>
            <div>
                {action === actions.DRAW  
                    ? <DrawRectangle expectedRectangleDims={diceValues} hints={hints} allowed={pendingRectangleAllowed} />
                : action === actions.TICK   
                    ? <TickType tickError={tickError}/>
                : action === actions.CONFIRM
                    ? <span>Bestätige deine Auswahl des Typs: {tickedType}</span>
                : action === actions.WAIT
                    ? <span>Warte auf die anderen Spieler für die nächste Runde!</span>
                : <span>Unbekannte Aktion {action}</span>}
            </div>

            {card &&  <>
                <FieldMatrix board={card.boardTemplate} onCellClick={onCellClick} areas={card.areas}
                    pendingRectangle={pendingRectangle} pendingRectangleAllowed={pendingRectangleAllowed} 
                    tickedPoints={tickedPoints} />
                <ScoringTags tags={card.scoringTags} />
            </>}
            <span>{card?.numberOfRoundsPlayed} gespielte Runde(n)</span>

            
        </div>
    );
}