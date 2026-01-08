import { convertReasonToReadable } from "../../../shared/model/areaUtils";

export default function DrawRectangle({ expectedRectangleDims, hints, allowed }) {
    return(<>
        <div>
            <span>Zeichne ein {expectedRectangleDims[0]} x {expectedRectangleDims[1]} Rechteck!</span>
        </div>
        {!allowed && <div className="vertical-container small center">
            {hints.map(convertReasonToReadable).map((h, idx) => <span key={idx} className="error">{h}</span>)}
        </div>}
    </>)
}