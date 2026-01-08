import { useCallback, useEffect, useState } from "react";
import { getEnumTypeFromPoint } from "../../../shared/model/boardUtils"
import { getPointsOfTypeInArea } from "../../../shared/apiServices/gameService";

export default function useTickType({gameId, playerId, card, area, allowedTypes, onTicked, unset}) {
    const [tickedType, setTickedType] = useState();
    const [tickedPoints, setTickedPoints] = useState([]);
    const [tickError, setTickError] = useState(false);

    const unsetTickedType = useCallback(() => {
        setTickedType(undefined)
        setTickedPoints([])
    }, [setTickedType, setTickedPoints])

    useEffect(() => {
        if(unset) 
            unsetTickedType();
    }, [unset, unsetTickedType])

    const tick = useCallback((point) => {
        if(card?.boardTemplate === undefined || allowedTypes === undefined)
            return;
        
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
    }, [unsetTickedType, gameId, playerId, card, area, allowedTypes, onTicked]);

    return { tickedType, tickedPoints, tickError, tick}
}