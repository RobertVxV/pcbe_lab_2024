import React, { useState, useEffect } from 'react';
import CellsManager from './CellsManager';
import FoodManager from './FoodManager';
import ConfigManager from './ConfigManager';
import StatsViewer from './StatsViewer';

const GameOfLife = () => {
    const [cells, setCells] = useState([]);
    const [food, setFood] = useState([]);
    const [generation, setGeneration] = useState(0);

    useEffect(() => {
        // Inițializare sau actualizare a celulelor și alimentelor
    }, []);

    return (
        <div>
            <h1>Game of Life</h1>
            <CellsManager cells={cells} setCells={setCells} />
            <FoodManager food={food} setFood={setFood} />
            <ConfigManager setGeneration={setGeneration} />
            <StatsViewer generation={generation} />
        </div>
    );
};

export default GameOfLife;
