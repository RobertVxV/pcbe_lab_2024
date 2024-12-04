import React, { useState, useEffect, useCallback } from "react";
import "./App.css";

// Inițializarea celulelor (10x10 grilă, fiecare celulă este vie sau moartă)
const createInitialGrid = (rows, cols) => {
    const grid = [];
    for (let i = 0; i < rows; i++) {
        const row = [];
        for (let j = 0; j < cols; j++) {
            row.push({ alive: Math.random() > 0.7 }); // Random: 30% celule vii
        }
        grid.push(row);
    }
    return grid;
};

function App() {
    const [grid, setGrid] = useState(createInitialGrid(10, 10));
    const [isRunning, setIsRunning] = useState(false);

    // Funcție pentru a simula următorul pas al jocului
    const nextStep = useCallback(() => {
        setGrid((prevGrid) => {
            const newGrid = prevGrid.map((row, rowIndex) =>
                row.map((cell, colIndex) => {
                    // Reguli Game of Life (simplificat)
                    const liveNeighbors = countLiveNeighbors(prevGrid, rowIndex, colIndex);
                    if (cell.alive) {
                        return liveNeighbors === 2 || liveNeighbors === 3
                            ? { ...cell }
                            : { alive: false };
                    } else {
                        return liveNeighbors === 3
                            ? { alive: true }
                            : { alive: false };
                    }
                })
            );
            return newGrid;
        });
    }, []); // Adăugăm un array gol ca dependență, ceea ce înseamnă că funcția va fi memorată

    // Numără vecinii vii ai unei celule
    const countLiveNeighbors = (grid, rowIndex, colIndex) => {
        const directions = [
            [-1, -1], [-1, 0], [-1, 1],
            [0, -1],          [0, 1],
            [1, -1], [1, 0], [1, 1]
        ];
        let count = 0;
        directions.forEach(([x, y]) => {
            const newRow = rowIndex + x;
            const newCol = colIndex + y;
            if (newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length) {
                if (grid[newRow][newCol].alive) count++;
            }
        });
        return count;
    };

    // Controlați jocul cu butonul "Start/Stop"
    useEffect(() => {
        if (isRunning) {
            const interval = setInterval(nextStep, 500); // Pași la fiecare 500ms
            return () => clearInterval(interval); // Curățare interval la oprire
        }
    }, [isRunning, nextStep]); // `nextStep` este acum memorată cu `useCallback`

    return (
        <div className="App">
            <h1>Game of Life</h1>
            <div
                style={{
                    display: "grid",
                    gridTemplateColumns: `repeat(10, 20px)`, // 10 coloane, câte 20px lățime
                }}
            >
                {grid.flatMap((row, rowIndex) =>
                    row.map((cell, colIndex) => (
                        <div
                            key={`${rowIndex}-${colIndex}`}
                            style={{
                                width: "20px",
                                height: "20px",
                                backgroundColor: cell.alive ? "green" : "white",
                                border: "1px solid black",
                            }}
                        />
                    ))
                )}
            </div>
            <button onClick={() => setIsRunning(!isRunning)}>
                {isRunning ? "Stop" : "Start"}
            </button>
        </div>
    );
}

export default App;
