export function getEnumTypeFromPoint(board, point) {
    return board.fields[point.y][point.x].type.enumName;
}