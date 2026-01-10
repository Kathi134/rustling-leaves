import { getTickableFieldTypes, isAreaValid } from "../../../shared/apiServices/gameService";
import { createAreaFromTopLeftAndDimensions } from "../../../shared/model/areaUtils";
import { useState, useCallback, useEffect } from "react";

export default function useRectangleDrawing({ gameId, playerId, card, diceValues, onDrawn, onRotated }) {
    const [pendingRectangle, setRectangle] = useState();
    const [pendingRectangleAllowed, setAllowed] = useState(true);
    const [hints, setHints] = useState([]);
    const [allowedTypes, setAllowedTypes] = useState();
    const [dimensions, setDimensions] = useState();

    useEffect(() => setDimensions(diceValues), [diceValues, setDimensions])

    const draw = useCallback((point, currDimensions=dimensions) => {
        if(card?.boardTemplate === undefined || currDimensions === undefined)
            return;

        const area = createAreaFromTopLeftAndDimensions(point, card.boardTemplate, currDimensions);
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
    }, [card, dimensions, gameId, playerId, onDrawn]);

    const rotate = useCallback((point) => {
        if(!dimensions || dimensions?.length < 2)
            return
        
        const newDims = [dimensions[1], dimensions[0]];
        setDimensions(newDims);
        if(point) {
            draw(point, newDims)
            onRotated();
        }
    }, [dimensions, onRotated, draw])

    return { pendingRectangle, pendingRectangleAllowed, hints, allowedTypes, draw, rotate };
}