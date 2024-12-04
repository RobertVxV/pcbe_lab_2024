import React from 'react';

const Cell = ({ alive, onClick }) => {
    return (
        <div
            onClick={onClick}
            style={{
                width: 20,
                height: 20,
                backgroundColor: alive ? 'green' : 'gray',
                display: 'inline-block',
                margin: '1px',
            }}
        ></div>
    );
};

export default Cell;
