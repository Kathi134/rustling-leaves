import { IoMdAddCircle, IoIosCheckmarkCircle } from "react-icons/io";
import { RiEditCircleFill } from "react-icons/ri";
import { HiPlus } from "react-icons/hi";
import { Link } from "react-router-dom";
import "./CircleButton.css"

function CircleButtonIcon({icon, color, size}) {
    const OPTION_ICONS = {
        'add': IoMdAddCircle,
        'save': IoIosCheckmarkCircle,
        'edit': RiEditCircleFill,
        'tiny-add': HiPlus,
    }
    return OPTION_ICONS[icon]({color: color ??'var(--primary-highlight)', size: size ?? "1.7rem"});
}

export function SubmitCircleButton({icon, color, size, className, useIconBox = true}) {
    useIconBox = useIconBox ?? true;
    const classList = `circle-btn-container ${className} ${useIconBox ? 'icon-box' : ''}`
    return (
        <button className={classList} type='submit'>
            <CircleButtonIcon icon={icon} color={color} size={size} />
        </button>
    );
}

export function LinkCircleButton({icon, color, size, className, useIconBox = true, to}) {
    useIconBox = useIconBox ?? true;
    const classList = `${className} ${useIconBox ? 'icon-box' : ''}`
    return (
        <Link to={to} relative="path" className={classList}>
            <CircleButtonIcon icon={icon} color={color} size={size} />
        </Link>
    );
}

export function SimpleCircleButton({icon, color, size, className, useIconBox = true, onClick}) {
    const classList = `unset circle-btn-container ${className} ${useIconBox ? 'icon-box' : ''}`
    return (
        <button className={classList} onClick={onClick}>
            <CircleButtonIcon icon={icon} color={color} size={size} />
        </button>
    );
}