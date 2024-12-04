import React from 'react';

const FoodManager = ({ food, setFood }) => {
    const addFood = () => {
        setFood([...food, { id: food.length, position: { x: Math.random() * 10, y: Math.random() * 10 } }]);
    };

    return (
        <div>
            <h2>Food Manager</h2>
            <button onClick={addFood}>Add Food</button>
            <div>
                {food.map(f => (
                    <div key={f.id} style={{ position: 'absolute', top: f.position.y, left: f.position.x }}>
                        🍎
                    </div>
                ))}
            </div>
        </div>
    );
};

export default FoodManager;
