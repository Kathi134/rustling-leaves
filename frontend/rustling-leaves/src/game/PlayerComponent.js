import { useCallback, useEffect, useMemo, useState } from "react";
import { getPlayerCard, getTickableFieldTypes, isAreaValid, getPointsOfTypeInArea } from "../shared/apiServices/gameService";
import FieldMatrix from "./Board/FieldMatrix/FieldMatrix";
import ScoringTags from "./Board/ScoringTags/ScoringTags";
import { getOptionValue } from "../shared/model/diceUtils";
import DrawRectangle from "./Board/FieldMatrix/DrawRectangle";
import TickType from "./Board/FieldMatrix/TickType";
import { equals, createAreaFromTopLeftAndDimensions, containsPoint } from "../shared/model/areaUtils";
import { getEnumTypeFromPoint } from "../shared/model/boardUtils";

const actions = {
    draw: "draw",
    tick: "tick",
    confirm: "confirm",
    wait: "wait"
}

// const ACTIONS = {
//     draw: {
//         name: "draw",
//         onCellClick: handleCellClickInDrawState,
//         onOutsideAreaClick: handleCellClickInDrawState
//     },
//     tick: {
//         name: "tick",
//         onCellClick: handleCellClickInTickState,
//         onOutsideAreaClick: handleCellClickInDrawState
//     }
// }

export default function PlayerComponent({ playerId, playerName, gameId, diceResults }) {
    const [card, setCard] = useState();
    const [expectedAction, setExpectedAction] = useState(actions.draw);

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

    const onPendingRectangleChange = useCallback((updateValue) => {
        if(equals(pendingRectangle, updateValue))
            return;
        setPendingRectangle(updateValue);

        if(expectedAction === actions.tick) {
            setTickedType(undefined);
        }

        isAreaValid(gameId, playerId, updateValue.topLeft, updateValue.bottomRight)
            .then(isValid => {
                if (isValid === true) {
                    setHints([]);
                    setPendingRectangleAllowed(true);
                    getTickableFieldTypes(gameId, playerId, updateValue.topLeft, updateValue.bottomRight)
                        .then(setAllowedTypes)
                        .then(() => setExpectedAction(actions.tick));
                } else {
                    setExpectedAction(actions.draw);
                    setHints(isValid)
                    setPendingRectangleAllowed(false);
                }
            });   
    }, [pendingRectangle, expectedAction, gameId, playerId]);

    const handleCellClickInDrawState = useCallback((_, rowIndex, colIndex) => {
        if(card?.boardTemplate === undefined || diceValues === undefined)
            return;

        const point = {x: colIndex, y: rowIndex};
        const area = createAreaFromTopLeftAndDimensions(point, card?.boardTemplate, diceValues);

        onPendingRectangleChange(area)
    }, [card, diceValues, onPendingRectangleChange]);    

   
    const onOutsideAreaClick = useMemo(() => {
        if(expectedAction === actions.tick)
            return handleCellClickInDrawState
    }, [expectedAction, handleCellClickInDrawState]);

    /* ticking */
    const [allowedTypes, setAllowedTypes] = useState();
    const [tickedType, setTickedType] = useState();
    const [tickedPoints, setTickedPoints] = useState([]);
    const [tickError, setTickError] = useState(false)

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
            setTickError(true);
            setTickedType(undefined);
            setTickedPoints([]);
        } else {
            setTickError(false);
            setTickedType(type);
            getPointsOfTypeInArea(gameId, playerId, pendingRectangle, type)
                .then(setTickedPoints)
                .then(() => setExpectedAction(actions.confirm))
        }
    }, [allowedTypes, card, gameId, onOutsideAreaClick, pendingRectangle, playerId]);
        
        // TODO: BUG: somehow frontend sends endless requests
        // TODO: BUG: sometimes tick doesnt work: gets set, works but then gets unset with [] again

    const onCellClick = useMemo(() => {
        if(expectedAction === actions.draw) 
            return handleCellClickInDrawState
        if(expectedAction === actions.tick)
            return handleCellClickInTickState
    }, [expectedAction, handleCellClickInDrawState, handleCellClickInTickState]);

    

    return (
        <div className="vertical-container center">
            <h3>{playerName}s Spiel-Karte</h3>
            <div>
                {expectedAction === actions.draw  
                    ? <DrawRectangle expectedRectangleDims={diceValues} hints={hints} allowed={pendingRectangleAllowed} />
                : expectedAction === actions.tick   
                    ? <TickType tickError={tickError}/>
                : expectedAction === actions.confirm
                    ? <span>Bestätige deine Auswahl des Typs: {tickedType}</span>
                : expectedAction === actions.wait
                    ? <span>Warte auf die anderen Spieler für die nächste Runde!</span>
                : <span>Unbekannte Aktion {expectedAction}</span>}
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