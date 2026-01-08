import { getOptionValue, isOptionClouded } from "../shared/model/diceUtils";
import "./dice.css";

export default function Dice({ option = "OPTION_1", backgroundColor = "white", valueColor = "green" }) {
    const value = getOptionValue(option);
    const isClouded = isOptionClouded(option);

    return (
        <div className="dice" style={{ backgroundColor, borderColor: valueColor }}>
            <div className={`pip-grid pip-${value}`}>
                {Array.from({ length: value }).map((_, i) => (
                    <span key={i} className="pip" style={{ backgroundColor: valueColor }}/>
                ))}
            </div>

            {isClouded && (
                <svg className="cloud" viewBox="2 0 64 64" aria-hidden>
                    <path
                        d="M18 42
                            Q8 42 9 32
                            Q9 24 18 23
                            Q20 12 32 14
                            Q38 8 46 14
                            Q56 18 54 30
                            Q62 34 58 44
                            Q56 50 46 50
                            H22
                            Q18 50 18 42 Z
                            "
                        fill="none"
                        stroke={valueColor}
                        strokeWidth="1.5"
                        strokeLinejoin="round"
                    />
                    
                </svg>
            )}
        </div>
    );
};
