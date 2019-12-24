import React from 'react';
import './App.css';
import { Container } from '@material-ui/core';
import Navbar from './components/layout/Navbar';
import Baseline from './components/linearization/Baseline';
import UploadState from './context/upload/UploadState';

const App = () => {
  return (
    <UploadState>
      <div className='App'>
        <Navbar />
        <Container>
          <Baseline />
        </Container>
      </div>
    </UploadState>
  );
}

export default App;
