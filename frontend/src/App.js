import React from 'react';
import ConfigForm from './components/ConfigForm';
import StatsViewer from './components/StatsViewer';
import ActionButtons from './components/ActionButtons';

function App() {
  return (
      <div className="App">
        <ConfigForm />
        <hr />
        <StatsViewer />
        <hr />
        <ActionButtons />
      </div>
  );
}

export default App;
