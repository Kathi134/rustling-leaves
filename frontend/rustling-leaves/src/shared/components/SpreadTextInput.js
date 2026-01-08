import { useState } from "react"
import { useEffect } from "react";
import { useRef } from "react";

export default function SpreadTextInput({type="text", inputClassName, values, onChange}) {
    const [localValues, setLocalValues] = useState(values);
    const inputRefs = useRef([]);

    
    useEffect(() => {
        setLocalValues(values);
    }, [values]);

    function updateValues(index, rawValue) {
        const value = rawValue.slice(-1); // enforce single character

        const updatedValues = [...localValues];
        updatedValues[index] = value;

        setLocalValues(updatedValues);
        onChange?.(value, index, updatedValues);

        // auto-focus next input if a character was entered
        if (value && index < inputRefs.current.length - 1) {
            inputRefs.current[index + 1]?.focus();
        }

        // auto-focus next input if a character was deleted
        if (rawValue === "" && index > 0) {
            inputRefs.current[index - 1]?.focus();
        }
    }

    function handlePaste(e) {
        e.preventDefault();

        const pastedText = e.clipboardData
            .getData("text")
            .slice(0, localValues.length);

        const updated = localValues.map((_, i) => pastedText[i] ?? "");

        setLocalValues(updated);
        onChange?.(pastedText[0] ?? "", 0, updated);

        // focus last filled input
        const lastIndex = Math.min(pastedText.length, inputRefs.current.length) - 1;
        if (lastIndex >= 0) {
            inputRefs.current[lastIndex]?.focus();
        }
    }

    return (<>
        <div className="horizontal-container gap-1">
            {localValues.map((v, idx) => (
                <input
                    key={idx}
                    ref={el => (inputRefs.current[idx] = el)}
                    className={inputClassName + " decent-input-number center"}
                    type={type}
                    value={v}
                    maxLength={2}
                    onChange={e => updateValues(idx, e.target.value)}
                    onPaste={handlePaste}
                />
            ))}
        </div>
    </>);
}


