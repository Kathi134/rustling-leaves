export function containsPoint(area, point) {

    return point.y >= area.topLeft.y && point.y <= area.bottomRight.y
            && point.x >= area.topLeft.x && point.x <= area.bottomRight.x

    // return area.points.some(p => p.x === point.x && p.y === point.y)
}

export function equals(area1, area2) {
    if(area1 === area2)
        return true;
    if((!area1 && area2) || (area1 && !area2))
        return false;
    return area1.topLeft?.x === area2.topLeft?.x
        && area1.topLeft?.y === area2.topLeft?.y
        && area1.bottomRight?.x === area2.bottomRight?.x
        && area1.bottomRight?.y === area2.bottomRight?.y;
}

export function createAreaFromTopLeftAndDimensions(point, board, dims) {
    const [rectWidth, rectHeight] = dims;
    const numRows = board.fields.length;
    const numCols = board.fields[0].length;

    // Adjust top-left if rectangle would overflow
    let startY = point.y;
    let startX = point.x;

    if (startY + rectHeight > numRows) {
        startY = numRows - rectHeight;
    }
    if (startX + rectWidth > numCols) {
        startX = numCols - rectWidth;
    }
     
    // Generate all cell positions inside the rectangle
    const area = {
        topLeft: { x: startX,  y: startY },
        bottomRight: { x: startX + rectWidth - 1, y: startY + rectHeight - 1 },
    }

    return area;
}

export function convertReasonToReadable(hint) {
    switch(hint) {
        case "IncludeSinglePointDrawCondition": return "Das Rechteck muss dein Startfeld beinhalten!";
        case "RiverDrawCondition": return "Das Rechteck überlappt mit dem Fluss.";
        case "AdjacentDrawCondition": return "Das Rechteck muss angrenzend gezeichnet werden.";
        case "NoOverlapDrawCondition": return "Das Rechteck überlappt mit einem bereits markierten Bereich.";
        default: return "Fehler beim Zeichnen.";
    };
}