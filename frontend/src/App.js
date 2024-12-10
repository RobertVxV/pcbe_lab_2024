import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import ConfigForm from './components/ConfigForm'; // Import your ConfigForm component
import ActionButtons from './components/ActionButtons'; // Import other components
import StatsViewer from './components/StatsViewer'; // Import other components

import './App.css';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    {/* Route for the home page */}
                    <Route path="/" element={
                        <div>
                            <StatsViewer />
                            <ActionButtons />
                        </div>
                    } />

                    {/* Route for the settings page */}
                    <Route path="/settings" element={<ConfigForm />} />

                    {/* Additional routes for other components can go here */}
                    {/*<Route path="/" element={<StatsViewer />} />*/}
                </Routes>
            </div>
        </Router>
    );
}

export default App;
