import React, { useState } from 'react';
import axios from 'axios';

const ActionButtons = () => {
    const [message, setMessage] = useState(''); // To display success or error messages
    const [cellType, setCellType] = useState('sexual'); // To store the selected cell type
    const [cellCount, setCellCount] = useState(5); // To store the number of cells
    const [foodUnits, setFoodUnits] = useState(10); // To store the number of food units to be added

    // Handle cell type change
    const handleCellTypeChange = (e) => {
        setCellType(e.target.value);
    };

    // Handle cell count change
    const handleCellCountChange = (e) => {
        setCellCount(e.target.value);
    };

    // Handle food units change
    const handleFoodUnitsChange = (e) => {
        setFoodUnits(e.target.value);
    };

    // Add food units
    const addFood = () => {
        if (foodUnits <= 0 || isNaN(foodUnits)) {
            setMessage("Please enter a valid positive number for food units.");
            return;
        }

        axios.post('http://localhost:8080/food', { units: parseInt(foodUnits) })
            .then(response => {
                setMessage(response.data);
            })
            .catch(err => {
                console.error("Error adding food:", err);
                setMessage("Error adding food: " + (err.response?.data || err.message));
            });
    };

    // Add cells
    const addCells = () => {
        if (cellCount <= 0 || !["sexual", "asexual"].includes(cellType.toLowerCase())) {
            setMessage("Please enter a valid type (sexual/asexual) and positive number of cells.");
            return;
        }

        axios.post('http://localhost:8080/cells', { type: cellType, count: parseInt(cellCount) })
            .then(response => {
                setMessage(response.data);
            })
            .catch(err => {
                console.error("Error adding cells:", err);
                setMessage("Error adding cells: " + (err.response?.data || err.message));
            });
    };

    return (
        <div className="ActionButtons">
            <h2>Actions</h2>

            {/* Section for adding cells */}
            <div>
                <label>Cell Type: </label>
                <select value={cellType} onChange={handleCellTypeChange}>
                    <option value="sexual">Sexual</option>
                    <option value="asexual">Asexual</option>
                </select>
            </div>
            <div style={{ marginTop: "10px" }}>
                <label>Cell Count: </label>
                <input
                    type="number"
                    value={cellCount}
                    onChange={handleCellCountChange}
                    min="1"
                    style={{ marginLeft: '10px', marginRight: '10px' }}
                />
            </div>
            <button onClick={addCells} style={{ marginTop: "10px" }}>Add Cells</button>

            {/* Section for adding food units */}
            <div style={{ marginTop: "20px" }}>
                <label>Food Units: </label>
                <input
                    type="number"
                    value={foodUnits}
                    onChange={handleFoodUnitsChange}
                    min="1"
                    style={{ marginLeft: '10px', marginRight: '10px' }}
                />
            </div>
            <button onClick={addFood} style={{ marginTop: "10px" }}>Add Food</button>

            {/* Display messages */}
            {message && <p style={{ marginTop: "10px", color: "green" }}>{message}</p>}
        </div>
    );
};

export default ActionButtons;
