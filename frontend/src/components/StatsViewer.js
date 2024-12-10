import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './styles.css';

const StatsViewer = () => {
    const [stats, setStats] = useState(null); // To store the stats response
    const [error, setError] = useState(''); // To store any error messages
    const [loading, setLoading] = useState(true); // To handle loading state

    const fetchStats = () => {
        axios.get('http://localhost:8080/info/stats')
            .then(response => {
                setStats(response.data);
                setLoading(false);
                setError(''); // Clear error on successful fetch
            })
            .catch(err => {
                console.error("Error details:", err); // Log the full error object for debugging
                const errorMessage = err.response
                    ? `${err.response.status}: ${err.response.data.message || err.response.data}`
                    : err.message;
                setError(errorMessage);
                setLoading(false);
            });
    };

    // Fetch the stats when the component mounts and set up auto-refresh
    useEffect(() => {
        fetchStats(); // Initial fetch
        const interval = setInterval(fetchStats, 1000); // Refresh every 1 second

        // Cleanup interval on component unmount
        return () => clearInterval(interval);
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
        <div className="container">
            <h1>Application Stats</h1>
            {stats && Object.entries(stats).map(([key, value]) => (
                <div key={key} style={{ marginBottom: '10px' }}>
                    <strong>{key.replace(/([A-Z])/g, ' $1')}:</strong> {value}
                </div>
            ))}
        </div>
    );
};

export default StatsViewer;
