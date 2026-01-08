import { Link, useLocation } from "react-router-dom";
import { IoChevronBackOutline } from "react-icons/io5";

export default function Back({onClick, size, color}) {
    const { state } = useLocation();

    const style = {
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        float: "left"
    };

    size = size ?? '1.7rem'
    return (
        <Link to={onClick ? '.' : '..'} relative="path" style={style} onClick={onClick} state={state}>
            <IoChevronBackOutline color={color ?? "var(--default-light)"} size={size}/>
        </Link>
    )
}