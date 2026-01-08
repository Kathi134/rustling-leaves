
export default function TickType({ tickError }) {
    return(<>
        <div>
            <span>Kreuze eine Gattung an!</span>
        </div>
        {tickError && <div className="vertical-container small center">
            <span className="error">Dieser Typ darf nicht angekreuzt werden.</span>
        </div>}
    </>)
}