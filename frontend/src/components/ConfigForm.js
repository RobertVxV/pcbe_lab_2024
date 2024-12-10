import React, { useState, useEffect } from 'react';
import axios from 'axios';

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

    const [message, setMessage] = useState('');

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
                // Check if the response is an object and stringify if necessary
                if (typeof response.data === 'object') {
                    setMessage('Configuration updated successfully!');
                } else {
                    setMessage(response.data);
                }
            })
            .catch(error => setMessage('Error: ' + (error.response?.data || 'Request failed')));
    };


    // Reset configuration to default values from the backend
    const handleReset = () => {
        axios.post('http://localhost:8080/config/reset')
            .then(response => {
                setConfig(response.data);
                setMessage('Configuration reset successfully');
            })
            .catch(error => setMessage('Error resetting configuration: ' + error.response?.data || 'Request failed'));
    };

    return (
        <div>
            <h1>Application Configuration</h1>
            <form>
                <div>
                    <label>Start Food: </label>
                    <input
                        type="number"
                        name="startFood"
                        value={config.startFood}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>Reproduction Threshold: </label>
                    <input
                        type="number"
                        name="reproductionThreshold"
                        value={config.reproductionThreshold}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>T Full: </label>
                    <input
                        type="number"
                        name="timeFull"
                        value={config.timeFull}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>T Starve: </label>
                    <input
                        type="number"
                        name="timeStarve"
                        value={config.timeStarve}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>T Full Variance: </label>
                    <input
                        type="number"
                        name="timeFullVariance"
                        value={config.timeFullVariance}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>Sexual Cells Count: </label>
                    <input
                        type="number"
                        name="sexualCellsCount"
                        value={config.sexualCellsCount}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>Asexual Cells Count: </label>
                    <input
                        type="number"
                        name="asexualCellsCount"
                        value={config.asexualCellsCount}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <label>Food Amount After Death: </label>
                    <input
                        type="number"
                        name="foodAmountAfterDeath"
                        value={config.foodAmountAfterDeath}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <button onClick={(e) => handleSubmit(e, '')}>Save Configuration</button>
                    <button onClick={(e) => handleSubmit(e, '/reset')}>Update Configuration</button>
                    <button type="button" onClick={handleReset}>Reset to Defaults</button>
                </div>
            </form>
            <p>{message}</p>
        </div>
    );
};

export default ConfigForm;
