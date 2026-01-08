import "./field-matrix.css"
import { containsPoint } from "../../../shared/model/areaUtils";


export default function FieldMatrix({ board, onCellClick, areas, pendingRectangle, pendingRectangleAllowed, tickedPoints }) {

    const getCellBorder = (y, x, inArea) => {
        if (!inArea) return undefined;

        const { topLeft, bottomRight } = inArea;
        const startY = topLeft.y;
        const startX = topLeft.x;
        const endY = bottomRight.y;
        const endX = bottomRight.x;

        let borders = "";

        if (y === startY) borders += "border-top ";
        if (y === endY) borders += "border-bottom ";
        if (x === startX) borders += "border-left ";
        if (x === endX) borders += "border-right ";

        return borders;
    }


    const computeClassesForCell = (field, rowIndex, colIndex) => {
        const point =  {x: colIndex, y: rowIndex}
        let classes = "field-cell ";

        if(field.isIncluded)  {
            classes += "included ";
            const wrappingArea = areas.find(a => containsPoint(a, point));
            classes += getCellBorder(point.y, point.x, wrappingArea);
        }

        if(field.isTicked || tickedPoints?.some(p => p.x === point.x && p.y === point.y)) {
            classes += "ticked ";
        }

        const topLeftRiverFieldPair = board.river.pairedRectangles
            .find(pair => (pair.topLeft.x === point.x && pair.topLeft.y === point.y))
        if(topLeftRiverFieldPair) {
            if(topLeftRiverFieldPair.topLeft.x === topLeftRiverFieldPair.bottomRight.x-1)
                classes += "river-right ";
            else if(topLeftRiverFieldPair.topLeft.y === topLeftRiverFieldPair.bottomRight.y-1)
                classes += "river-bottom ";
        }

        if(pendingRectangle && containsPoint(pendingRectangle, point)) {
            classes += "highlighted ";
            classes += pendingRectangleAllowed ? "" : "not-allowed ";
            classes += getCellBorder(point.y, point.x, pendingRectangle);
        }

        return classes
    }

    

    return (
        <div>
            <table>
                <tbody>
                    {board.fields.map((row, rowIndex) => (
                        <tr key={rowIndex}>
                            {row.map((field, colIndex) => (
                                <td key={colIndex} className={computeClassesForCell(field, rowIndex, colIndex)}
                                    style={{backgroundImage: `url("${process.env.PUBLIC_URL}/field-types/${field.type.enumName.toLocaleLowerCase()}.jpg")`}}
                                    onClick={(e) => onCellClick(e, rowIndex, colIndex)}>
                                    {field.isTicked ? "X" : ""}
                                </td>
                            ))}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}