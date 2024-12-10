import React, { useState } from 'react';
import axios from 'axios';
import './styles.css';

const ActionButtons = () => {
    const [message, setMessage] = useState(''); // Message text
    const [messageType, setMessageType] = useState(''); // Message type ('success' or 'error')
    const [cellType, setCellType] = useState('sexual');
    const [cellCount, setCellCount] = useState(5);
    const [foodUnits, setFoodUnits] = useState(10);

    const handleCellTypeChange = (e) => setCellType(e.target.value);

    const handleCellCountChange = (e) => setCellCount(e.target.value);

    const handleFoodUnitsChange = (e) => setFoodUnits(e.target.value);

    const addFood = () => {
        if (foodUnits <= 0 || isNaN(foodUnits)) {
            setMessageType('error');
            setMessage('Please enter a valid positive number for food units.');
            return;
        }

        axios.post('http://localhost:8080/food', { units: parseInt(foodUnits) })
            .then(response => {
                setMessageType('success');
                setMessage(response.data);
            })
            .catch(err => {
                setMessageType('error');
                setMessage('Error adding food: ' + (err.response?.data || err.message));
            });
    };

    const addCells = () => {
        if (cellCount <= 0 || !['sexual', 'asexual'].includes(cellType.toLowerCase())) {
            setMessageType('error');
            setMessage('Please enter a valid type (sexual/asexual) and positive number of cells.');
            return;
        }

        axios.post('http://localhost:8080/cells', { type: cellType, count: parseInt(cellCount) })
            .then(response => {
                setMessageType('success');
                setMessage(response.data);
            })
            .catch(err => {
                setMessageType('error');
                setMessage('Error adding cells: ' + (err.response?.data || err.message));
            });
    };

    return (
        <div className="container">
            <h2>Actions</h2>
            <div className="form-group">
                <label>Cell Type: </label>
                <select value={cellType} onChange={handleCellTypeChange}>
                    <option value="sexual">Sexual</option>
                    <option value="asexual">Asexual</option>
                </select>
            </div>
            <div className="form-group">
                <label>Cell Count: </label>
                <input
                    type="number"
                    value={cellCount}
                    onChange={handleCellCountChange}
                    min="1"
                />
                <button onClick={addCells}>Add Cells</button>
            </div>
            <div className="form-group">
                <label>Food Units: </label>
                <input
                    type="number"
                    value={foodUnits}
                    onChange={handleFoodUnitsChange}
                    min="1"
                />
                <button onClick={addFood}>Add Food</button>
            </div>
            {message && <p className={`message ${messageType}`}>{message}</p>}
        </div>
    );
};

export default ActionButtons;
