import { useCallback, useEffect, useState } from "react";
import { containsPoint } from "../../../shared/model/areaUtils";
import { getEnumTypeFromPoint } from "../../../shared/model/boardUtils";

export default function TickType({ allowedTypes, inArea, setOnClick, onResult, onOutsideAreaClick, board }) {
    const [tickError, setTickError] = useState(false)

    const handleCellClick = useCallback((event, rowIndex, colIndex) => {
        if(board === undefined || allowedTypes === undefined)
            return;

        console.log("TickType cellHandler")
        
        const point = {x: colIndex, y: rowIndex};
        if(!containsPoint(inArea, point)) {
            onOutsideAreaClick(event, rowIndex, colIndex);
            return;
        }

        const type = getEnumTypeFromPoint(board, point);

        if(!allowedTypes.some(x => x.enumName === type)) {
            setTickError(true);
            onResult(undefined);
        } else {
            setTickError(false);
            onResult(type);
        }
    }, [board, onOutsideAreaClick, allowedTypes, onResult, inArea]);

    useEffect(() => {
        setOnClick(() => handleCellClick);
    }, [setOnClick, handleCellClick]);

    return(<>
        <div>
            <span>Kreuze eine Gattung an!</span>
        </div>
        {tickError && <div className="vertical-container small center">
            <span className="error">Dieser Typ darf nicht angekreuzt werden.</span>
        </div>}
    </>)
}