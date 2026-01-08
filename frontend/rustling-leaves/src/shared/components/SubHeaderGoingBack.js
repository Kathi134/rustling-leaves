import Back from "./Back";

export default function SubHeaderGoingBack({children, title}) {
    return ( 
        <div className="horizontal-container space-between fixed-top">
            <Back size="1.5rem" />
            <h2 className="">{title}</h2>

            { children
                ? children
                : <Back size="1.5rem" onClick={()=>{}} color="var(--base)" />
            }   
        </div>
    );
}