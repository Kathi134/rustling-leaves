import { actions } from "./Action"
import DrawRectangle from "./DrawRectangle"
import TickType from "./TickType"
import { AiOutlineRotateRight } from "react-icons/ai";

export default function ActionPanel({action, diceValues, onRotateRectangle, drawHints, pendingRectangleAllowed, tickError, onSave}) {
    return(<div>
        <div>
        {action === actions.DRAW  
            ? <DrawRectangle expectedRectangleDims={diceValues} hints={drawHints} allowed={pendingRectangleAllowed} />
        : action === actions.TICK   
            ? <TickType tickError={tickError}/>
        : action === actions.CONFIRM
            ? <button className="small" onClick={() => onSave(true)}>Speichern</button>
        : action === actions.WAIT
            ? <span>Warte auf die anderen Spieler für die nächste Runde!</span>
        : <span>Unbekannte Aktion {action}</span>}
        
        <button className="small" onClick={onRotateRectangle}><AiOutlineRotateRight /></button>
        </div>
        <div><button className="small" onClick={() => onSave(false)}>Fehlwurf</button></div>
    </div>)
}