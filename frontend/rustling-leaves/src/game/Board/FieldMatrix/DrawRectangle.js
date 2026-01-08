import { useCallback, useEffect } from "react";
import { convertReasonToReadable, createAreaFromTopLeftAndDimensions } from "../../../shared/model/areaUtils";

export default function DrawRectangle({ expectedRectangleDims, setOnClick, onResult, board, hints, allowed }) {
    const handleCellClick = useCallback((event, rowIndex, colIndex) => {
        if(board === undefined || expectedRectangleDims === undefined)
            return;

        console.log("DrawRectangle cellHandler")

        const point = {x: colIndex, y: rowIndex};
        const area = createAreaFromTopLeftAndDimensions(point, board, expectedRectangleDims);

        onResult(area)
    }, [board, expectedRectangleDims, onResult]);

    useEffect(() => {
        setOnClick(() => handleCellClick);
    }, [setOnClick, handleCellClick]);

    return(<>
        <div>
            <span>Zeichne ein {expectedRectangleDims[0]} x {expectedRectangleDims[1]} Rechteck!</span>
        </div>
        {!allowed && <div className="vertical-container small center">
            {hints.map(convertReasonToReadable).map((h, idx) => <span key={idx} className="error">{h}</span>)}
        </div>}
    </>)
}