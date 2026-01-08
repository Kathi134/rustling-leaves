import Back from "../../shared/components/Back";
import { SimpleCircleButton } from "../../shared/components/CircleButton";

export default function MainComponent({
    children,
    title, headerClass="secondary",
    useBackButton,
    onAction, useActionButton=(onAction !== undefined)}, actionIcon="add"
) {
    return ( 
        <div className="vertical-container space-between below-top">
            
            <div className="horizontal-container space-between fixed-top ">
                <div className="horizontal-container space-between flex-1 horizontal-padding-1 padding-top bottom-margin">
                    { useBackButton 
                        ? <Back size="1.5rem" />
                        : <Back size="1.5rem" onClick={()=>{}} color={"var(--base)"} />
                    }
                    
                    <h2 className={`${headerClass} marked`}>{title}</h2>

                    { useActionButton
                        ? <SimpleCircleButton onClick={onAction} icon={actionIcon} color="var(--secondary-highlight-light)" />
                        : <Back size="1.5rem" onClick={()=>{}} color={"var(--base)"} />
                    }
                </div>
            </div>

            {children}
        </div>
    );
}