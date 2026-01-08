import { actions } from "./Action"
import DrawRectangle from "./DrawRectangle"
import TickType from "./TickType"

export default function ActionPanel({action, diceValues, drawHints, pendingRectangleAllowed, tickError, tickedType}) {
    return(<div>
        {action === actions.DRAW  
            ? <DrawRectangle expectedRectangleDims={diceValues} hints={drawHints} allowed={pendingRectangleAllowed} />
        : action === actions.TICK   
            ? <TickType tickError={tickError}/>
        : action === actions.CONFIRM
            ? <span>Bestätige deine Auswahl des Typs: {tickedType}</span>
        : action === actions.WAIT
            ? <span>Warte auf die anderen Spieler für die nächste Runde!</span>
        : <span>Unbekannte Aktion {action}</span>}
        
    </div>)
}