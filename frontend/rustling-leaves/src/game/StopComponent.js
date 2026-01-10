
export default function StopComponent({onAnswer}) {
    return(<>
        <div className="vertical-container center">
            <span>Eine neue Runde hat begonnen!</span>
            <span>Möchtest du vorher aus dem Spiel aussteigen?</span>
            <div className="horizontal-container gap-1">
                <button className="small" onClick={() => onAnswer(true)}>Ja</button>
                <button className="small" onClick={() => onAnswer(false)}>Nein</button>
            </div>
        </div> 
    </>)
}