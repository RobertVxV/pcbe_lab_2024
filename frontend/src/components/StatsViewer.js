import React, { useState, useEffect } from 'react';
import axios from 'axios';

const StatsViewer = () => {
    const [stats, setStats] = useState(null); // To store the stats response
    const [error, setError] = useState('');  // To store any error messages
    const [loading, setLoading] = useState(true); // To handle loading state

    // Fetch the stats when the component mounts
    useEffect(() => {
        axios.get('http://localhost:8080/info/stats')
            .then(response => {
                setStats(response.data);
                setLoading(false);
            })
            .catch(err => {
                console.error("Error details:", err); // Log the full error object for debugging
                const errorMessage = err.response
                    ? `${err.response.status}: ${err.response.data.message || err.response.data}`
                    : err.message;
                setError(errorMessage);
                setLoading(false);
            });
    }, []);


    // Loading state
    if (loading) {
        return <div>Loading stats...</div>;
    }

    // Error state
    if (error) {
        return <div style={{ color: 'red' }}>{error}</div>;
    }

    // Display stats
    return (
        <div className="StatsViewer">
            <h1>Application Stats</h1>
            <ul>
                {stats && Object.entries(stats).map(([key, value]) => (
                    <li key={key}>
                        <strong>{key.replace(/([A-Z])/g, ' $1')}:</strong> {value}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default StatsViewer;
