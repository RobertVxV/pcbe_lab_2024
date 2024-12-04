import React from 'react';

const StatsViewer = ({ generation }) => {
    return (
        <div>
            <h2>Game Stats</h2>
            <p>Generation: {generation}</p>
        </div>
    );
};

export default StatsViewer;
