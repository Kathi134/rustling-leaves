import { getTickableFieldTypes, isAreaValid } from "../../../shared/apiServices/gameService";
import { createAreaFromTopLeftAndDimensions } from "../../../shared/model/areaUtils";
import { useState, useCallback } from "react";

export default function useRectangleDrawing({ gameId, playerId, card, diceValues, onDrawn }) {
    const [pendingRectangle, setRectangle] = useState();
    const [pendingRectangleAllowed, setAllowed] = useState(true);
    const [hints, setHints] = useState([]);
    const [allowedTypes, setAllowedTypes] = useState();

    const draw = useCallback((point) => {
        if(card?.boardTemplate === undefined || diceValues === undefined)
            return;

        const area = createAreaFromTopLeftAndDimensions(point, card.boardTemplate, diceValues);
        setRectangle(area);

        isAreaValid(gameId, playerId, area.topLeft, area.bottomRight)
            .then(isValid => {
                if (isValid === true) {
                    setAllowed(true);
                    setHints([]);
                    getTickableFieldTypes(gameId, playerId, area.topLeft, area.bottomRight)
                        .then(setAllowedTypes)
                        .then(() => onDrawn(true))
                } else {
                    setAllowed(false);
                    setHints(isValid);
                    setAllowedTypes([]);
                    onDrawn(false);
                }
            })
    }, [card, diceValues, gameId, playerId, onDrawn]);

    return { pendingRectangle, pendingRectangleAllowed, hints, allowedTypes, draw };
}