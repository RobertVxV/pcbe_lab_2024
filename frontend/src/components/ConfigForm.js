import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './styles.css';

const ConfigForm = () => {
    const [config, setConfig] = useState({
        startFood: 100,
        reproductionThreshold: 3,
        timeFull: 1000,
        timeFullVariance: 600,
        timeStarve: 2000,
        sexualCellsCount: 5,
        asexualCellsCount: 6,
        foodAmountAfterDeath: 5
    });

    const [message, setMessage] = useState({ text: '', type: '' });

    useEffect(() => {
        axios.get('http://localhost:8080/config')
            .then(response => setConfig(response.data))
            .catch(error => console.error('Error fetching config:', error));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setConfig(prevConfig => ({
            ...prevConfig,
            [name]: value
        }));
    };

    const handleSubmit = (e, endpoint) => {
        e.preventDefault();
        axios.post(`http://localhost:8080/config${endpoint}`, config)
            .then(() => {
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

    const handleReset = () => {
        axios.get('http://localhost:8080/config')
            .then(response => {
                setConfig(response.data);
                setMessage({
                    text: 'Configuration reset successfully',
                    type: 'success'
                });
            })
            .catch(() => {
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
                setConfig(defaultConfig);
                setMessage({
                    text: 'Error resetting configuration. Default values applied.',
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
