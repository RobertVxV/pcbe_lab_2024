import React from 'react';

const CellsManager = ({ cells, setCells }) => {
    const toggleCellState = (index) => {
        const updatedCells = [...cells];
        updatedCells[index] = !updatedCells[index];
        setCells(updatedCells);
    };

    return (
        <div>
            <h2>Cells Manager</h2>
            <div>
                {cells.map((cell, index) => (
                    <button key={index} onClick={() => toggleCellState(index)}>
                        {cell ? 'Alive' : 'Dead'}
                    </button>
                ))}
            </div>
        </div>
    );
};

export default CellsManager;
