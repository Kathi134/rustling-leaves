import { IconContext } from 'react-icons';
import { BiFoodMenu, BiSolidMessageAltAdd } from "react-icons/bi";
import { NavLink, useLocation } from 'react-router-dom';
import './mainlayout.css'
import '../basic.css'
import { BsStars } from "react-icons/bs";
import { IoMdStats } from "react-icons/io";
import { useParams } from 'react-router-dom';


const MENU_ICONS = {
    'play': <BsStars />,
    'stats': <IoMdStats />,
    'games': <BiFoodMenu/>,
    'new': <BiSolidMessageAltAdd />
}

function MenuItem({destination, displayName}) {
    const location = useLocation()
    const { userId } = useParams();
    const { state } = useLocation();
    const currPath = location.pathname.split("/")[3] ?? "play";

    const iconStyle = {
        'color': currPath===destination ? 'var(--primary-highlight)' : 'var(--black)',
        'size': '2rem',
    }

    return (<>
        <NavLink to={`/account/${userId}/${destination}`} state={state}>
            <div className='vertical-container center'>
                <IconContext.Provider value={iconStyle}>
                    {MENU_ICONS[destination]}
                </IconContext.Provider>
                <span className="a unset">{displayName}</span>
            </div>
        </NavLink>
    </>);
}

export default MenuItem;