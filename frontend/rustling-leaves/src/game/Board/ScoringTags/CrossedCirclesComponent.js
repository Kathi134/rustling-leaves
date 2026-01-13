import "./scoring-tags.css"

export default function CrossedCirclesComponent({val, max}) {
    return(
        <div className="circle-container">
            {Array.from({ length: max }).map((_, idx) => (
                <div key={idx} className={`circle ${idx < val ? "crossed" : ""}`} />
            ))}
        </div>
    )
}