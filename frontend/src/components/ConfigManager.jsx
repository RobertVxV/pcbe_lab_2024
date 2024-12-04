import React from 'react';

const ConfigManager = ({ setGeneration }) => {
    const startGame = () => {
        setGeneration(0);
        // Inițierea jocului, resetarea celulelor și alimentelor
    };

    return (
        <div>
            <h2>Game Configuration</h2>
            <button onClick={startGame}>Start Game</button>
        </div>
    );
};

export default ConfigManager;
import React from 'react';

const ConfigManager = ({ setGeneration }) => {
    const startGame = () => {
        setGeneration(0);
        // Inițierea jocului, resetarea celulelor și alimentelor
    };

    return (
        <div>
            <h2>Game Configuration</h2>
            <button onClick={startGame}>Start Game</button>
        </div>
    );
};

export default ConfigManager;
