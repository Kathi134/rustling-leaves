import { Outlet } from "react-router-dom";
import "../basic.css"
import "./mainlayout.css"
// import MenuItem from "./MenuItem";

export default function MainLayout({children}) {
    

    return (<>
        <header></header>

        <main><div id="children-container"><Outlet /></div></main>

        {/* <footer>
            <nav className='horizontal-container space-evenly'>
                <MenuItem destination="play" />
                <MenuItem destination="new" />
                <MenuItem destination="stats" />
                <MenuItem destination="games" />
            </nav>
        </footer> */}
    </>);
}