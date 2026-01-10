import { useCallback, useEffect, useState } from "react";
import { getEnumTypeFromPoint } from "../../../shared/model/boardUtils"
import { getPointsOfTypeInArea } from "../../../shared/apiServices/gameService";

export default function useTickType({gameId, playerId, card, area, allowedTypes, onTicked, unset, resetUnsetEvent}) {
    const [tickedType, setTickedType] = useState();
    const [tickedPoints, setTickedPoints] = useState([]);
    const [tickError, setTickError] = useState(false);

    const unsetTickedType = useCallback(() => {
        setTickedType(undefined)
        setTickedPoints([])
    }, [setTickedType, setTickedPoints])

    useEffect(() => {
        if(unset > 0) {
            console.log("unse")
            unsetTickedType();
            resetUnsetEvent();
        }
    }, [unset, unsetTickedType, resetUnsetEvent])

    const tick = useCallback((point) => {
        if(card?.boardTemplate === undefined || allowedTypes === undefined)
            return;
        
        resetUnsetEvent();
        const type = getEnumTypeFromPoint(card?.boardTemplate, point);
        if(!allowedTypes.some(x => x.enumName === type)) {
            setTickError(true);
            unsetTickedType();
            onTicked(false);
        } else {
            setTickError(false);
            setTickedType(type);
            getPointsOfTypeInArea(gameId, playerId, area, type)
                .then(setTickedPoints)
                .then(() => onTicked(true))
        }
    }, [setTickError, resetUnsetEvent, unsetTickedType, onTicked, setTickedType, setTickedPoints, area, allowedTypes, gameId, playerId, card]);

    return { tickedType, tickedPoints, tickError, tick}
}