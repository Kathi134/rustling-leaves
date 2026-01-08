import './FancyToggle.css';

export default function FancyToggle({className, options, selected, onSelectionChange }) {    
    return (
        <div className={`toggle-container ${className}`} >
            {options.map((o, i) => <div key={i}>
                <input type="radio" name="toggle" id={`option-${i}`}
                    checked={selected === o} onChange={() => onSelectionChange(o)} />
                <label htmlFor={`option-${i}`}  className={selected === o ? 'active' : ''} >
                    {o}
                </label>
            </div>)}
        </div>
    );
};