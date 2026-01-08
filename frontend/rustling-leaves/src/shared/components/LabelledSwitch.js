import { useCallback, useEffect, useMemo, useState } from "react";
import Switch from "react-switch";

export default function LabelledSwitch({label, checkedColor, uncheckedColor, initChecked = false, onChange = () => {}}) {
  const [checked, setChecked] = useState(initChecked);

  useEffect(() => {
    setChecked(initChecked)
  }, [initChecked])

  const computeColor = (variableName) => {
    if(variableName.includes("var(")) {
      const raw = variableName.replace("var(", "").replace(")", "");
      return getComputedStyle(document.documentElement)
        .getPropertyValue(raw)
        .trim();
    } else {
      return variableName
    }
  }
  
  const colorOn = useMemo(() => 
    computeColor(checkedColor ?? "var(--primary-highlight)"),
  [checkedColor])

  const colorOff = useMemo(() => 
    computeColor(uncheckedColor ?? "var(--default-light)"),
  [uncheckedColor])


  const handleChange = useCallback((val) => {
    onChange(val);
    setChecked(val);
  }, [onChange, setChecked]);

  return (
    <div className="horizontal-container gap-1">
        <Switch
            checked={checked}
            onChange={handleChange}
            onColor={colorOn}   
            offColor={colorOff}
            uncheckedIcon={false}
            checkedIcon={false}
            handleDiameter={21}
            height={28}
            width={50}
        />
        <span className="center">{label}</span>
    </div>
  );
}
