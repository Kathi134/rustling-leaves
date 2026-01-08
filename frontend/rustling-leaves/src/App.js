import { BrowserRouter, Routes, Route } from "react-router-dom";
import MainLayout from "./global/mainlayout/MainLayout"
import WelcomePage from "./lobby/WelcomePage";
import LobbyConfigPage from "./lobby/LobbyConfigPage";
import GamePage from "./game/GamePage";

function App() {
  return (
    <BrowserRouter>
        <Routes>
          {/* <Route path="/" element={<SomeOutsideComponent/>} /> */}

          <Route element={<MainLayout/>}>
            <Route path="/" element={<WelcomePage/>} /> 
            <Route path="/lobby/:lobbyId/config" element={<LobbyConfigPage/>} /> 
            <Route path="/lobby/:lobbyId/game" element={<GamePage/>} /> 
            <Route path="/lobby/:lobbyId/" element={<LobbyConfigPage/>} /> 
          </Route>
        </Routes>
    </BrowserRouter>
  );
}

export default App;
