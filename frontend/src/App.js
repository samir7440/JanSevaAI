import React from 'react';
import {
    BrowserRouter as Router,
    Routes,
    Route
} from 'react-router-dom';

import CitizenDashboard from './components/CitizenDashboard';
import AdminDashboard from './components/AdminDashboard';

import './App.css';

function App() {

    return (
        <Router>

            <div className="App">

                <main>

                    <Routes>

                        {/* Citizen Application */}
                        <Route
                            path="/"
                            element={<CitizenDashboard />}
                        />

                        {/* Admin Governance Dashboard */}
                        <Route
                            path="/admin"
                            element={<AdminDashboard />}
                        />

                    </Routes>

                </main>

            </div>

        </Router>
    );
}

export default App;