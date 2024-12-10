import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './ConfigForm.css';

const ConfigForm = () => {
    const [config, setConfig] = useState({
        startFood: 100, // Default value
        reproductionThreshold: 3, // Default value
        timeFull: 1000, // Default value
        timeFullVariance: 600, // Default value
        timeStarve: 2000, // Default value
        sexualCellsCount: 5, // Default value
        asexualCellsCount: 6, // Default value
        foodAmountAfterDeath: 5 // Default value
    });

    const [message, setMessage] = useState({ text: '', type: '' }); // Separate message text and type

    // Fetch the current configuration from the backend
    useEffect(() => {
        axios.get('http://localhost:8080/config')
            .then(response => setConfig(response.data))
            .catch(error => console.error('Error fetching config:', error));
    }, []);

    // Handle input field changes
    const handleChange = (e) => {
        const { name, value } = e.target;
        setConfig(prevConfig => ({
            ...prevConfig,
            [name]: value
        }));
    };

    // Handle form submission to save or update configuration
    const handleSubmit = (e, endpoint) => {
        e.preventDefault();
        axios.post(`http://localhost:8080/config${endpoint}`, config)
            .then(response => {
                setMessage({
                    text: 'Configuration updated successfully!',
                    type: 'success'
                });
            })
            .catch(error => setMessage({
                text: `Error: ${error.response?.data || 'Request failed'}`,
                type: 'error'
            }));
    };

    // Reset configuration to default values from the backend
    const handleReset = () => {
        axios.get('http://localhost:8080/config')
            .then(response => {
                setConfig(response.data);  // Use the config fetched from the backend
                setMessage({
                    text: 'Configuration reset successfully',
                    type: 'success'
                });
            })
            .catch(error => {
                // If fetching from the backend fails, set default values
                const defaultConfig = {
                    startFood: 100,
                    reproductionThreshold: 3,
                    timeFull: 1000,
                    timeFullVariance: 600,
                    timeStarve: 2000,
                    sexualCellsCount: 5,
                    asexualCellsCount: 6,
                    foodAmountAfterDeath: 5
                };

                setConfig(defaultConfig);  // Set the default values in case of error
                setMessage({
                    text: `Error resetting configuration: ${error.response?.data || 'Request failed'}. Default values applied.`,
                    type: 'error'
                });
            });
    };


    return (
        <div className="config-form-container">
            <h1 className="config-form-title">Application Configuration</h1>
            <form className="config-form">
                {Object.entries(config).map(([key, value]) => (
                    <div className="config-form-group" key={key}>
                        <label className="config-form-label">{key.replace(/([A-Z])/g, ' $1')}:</label>
                        <input
                            type="number"
                            name={key}
                            value={value}
                            onChange={handleChange}
                            className="config-form-input"
                        />
                    </div>
                ))}
                <div className="config-form-buttons">
                    <button className="config-form-button" onClick={(e) => handleSubmit(e, '')}>Save Configuration</button>
                    <button className="config-form-button" onClick={(e) => handleSubmit(e, '/reset')}>Update Configuration</button>
                    <button className="config-form-button" type="button" onClick={handleReset}>Reset to Defaults</button>
                </div>
            </form>
            <p className={`config-form-message ${message.type}`}>{message.text}</p>
        </div>
    );
};

export default ConfigForm;
