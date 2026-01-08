import { useCallback, useEffect, useMemo, useState, useRef } from "react";
import { getPlayerCard, getTickableFieldTypes, isAreaValid, getPointsOfTypeInArea } from "../shared/apiServices/gameService";
import FieldMatrix from "./Board/FieldMatrix/FieldMatrix";
import ScoringTags from "./Board/ScoringTags/ScoringTags";
import { getOptionValue } from "../shared/model/diceUtils";
import DrawRectangle from "./Board/FieldMatrix/DrawRectangle";
import TickType from "./Board/FieldMatrix/TickType";
import { equals } from "../shared/model/areaUtils";


const actions = {
    draw: "draw",
    tick: "tick",
    confirm: "confirm",
    wait: "wait"
}

export default function PlayerComponent({ playerId, playerName, gameId, diceResults }) {
    const [card, setCard] = useState();
    const [expectedAction, setExpectedAction] = useState(actions.draw);

    const onCellClickRef = useRef(() => {});
    const updateOnCellClickRef = useCallback((fn) => { onCellClickRef.current = fn }, []);
    const onCellClick = useCallback((...args) => {console.log("foo");onCellClickRef.current(...args) }, []);

    const onOutsideAreaClickRef = useRef(() => {});
    const updateOutsideAreaClickRef = useCallback((fn) => { onOutsideAreaClickRef.current = fn }, []);
    const onOutsideAreaClick = useCallback((...args) => {onOutsideAreaClickRef.current?.(...args) }, []);


    // drawing related states
    const diceValues = useMemo(() => {
        if(diceResults)
            return diceResults?.map(getOptionValue);
        return [];
    }, [diceResults]);
    const [pendingRectangle, setPendingRectangle] = useState();
    const [pendingRectangleAllowed, setPendingRectangleAllowed] = useState(true);
    const [hints, setHints] = useState("");

    // ticking related states
    const [allowedTypes, setAllowedTypes] = useState();
    const [tickedType, setTickedType] = useState();
    const [tickedPoints, setTickedPoints] = useState([]);
    

    useEffect(() => {
        if(gameId === undefined || playerId === undefined) 
            return
        getPlayerCard(gameId, playerId).then(setCard)
    }, [gameId, playerId])

    useEffect(() => {
        console.log(onCellClickRef.current)
        console.log(onOutsideAreaClickRef.current)
    }, [onCellClickRef, onOutsideAreaClickRef]);


    const onPendingRectangleChange = useCallback((updateValue) => {
        if(equals(pendingRectangle, updateValue))
            return;
        setPendingRectangle(updateValue);
    }, [pendingRectangle]);

    // on drawn rectangle change
    useEffect(() => {
        if(pendingRectangle === undefined)
            return

        console.log("gicht")
        if(expectedAction === actions.tick) {
            setTickedType(undefined);
        }

        // BUG happens all the time!!!
        isAreaValid(gameId, playerId, pendingRectangle.topLeft, pendingRectangle.bottomRight)
            .then(isValid => {
                if (isValid === true) {
                    setHints([]);
                    setPendingRectangleAllowed(true);
                    getTickableFieldTypes(gameId, playerId, pendingRectangle.topLeft, pendingRectangle.bottomRight)
                        .then(setAllowedTypes)
                        .then(() => {
                            if(expectedAction === actions.draw) {
                                console.log(onCellClickRef)
                                updateOutsideAreaClickRef(onCellClickRef.current)
                                console.log(onOutsideAreaClickRef)
                            }
                        })
                        .then(() => setExpectedAction(actions.tick));
                } else {
                    setExpectedAction(actions.draw);
                    setHints(isValid)
                    setPendingRectangleAllowed(false);
                }
            });   
    }, [pendingRectangle, expectedAction, gameId, playerId, updateOnCellClickRef, setExpectedAction, onCellClickRef, updateOutsideAreaClickRef]);

    // on ticked type change
    useEffect(() => {
        if(tickedType === undefined) {
            setTickedPoints([]);
            return
        }
        getPointsOfTypeInArea(gameId, playerId, pendingRectangle, tickedType)
            .then(setTickedPoints)
            .then(() => setExpectedAction(actions.confirm))
        
        // TODO: BUG: somehow frontend sends endless requests
        // TODO: BUG: sometimes tick doesnt work: gets set, works but then gets unset with [] again
    }, [tickedType, pendingRectangle, gameId, playerId]);


    return (
        <div className="vertical-container center">
            <h3>{playerName}s Spiel-Karte</h3>
            <div>
                {expectedAction === actions.draw  
                    ? <DrawRectangle setOnClick={updateOnCellClickRef} onResult={onPendingRectangleChange} expectedRectangleDims={diceValues} board={card?.boardTemplate} 
                        hints={hints} allowed={pendingRectangleAllowed} />
                : expectedAction === actions.tick   
                    ? <TickType setOnClick={updateOnCellClickRef} onResult={setTickedType} board={card?.boardTemplate} 
                        inArea={pendingRectangle} onOutsideAreaClick={onOutsideAreaClick} allowedTypes={allowedTypes}/>
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