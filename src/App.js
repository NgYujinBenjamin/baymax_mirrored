import React from 'react';
import './App.css';
import { Container } from '@material-ui/core';
import Navbar from './components/layout/Navbar';
import Alert from './components/layout/Alert';
import Baseline from './components/linearization/Baseline';
import UploadState from './context/upload/UploadState';
import AlertState from './context/alert/AlertState';

const App = () => {
  return (
    <UploadState>
    <AlertState>
      <div className='App'>
        <Navbar />
        <Container>
          <Alert />
          <Baseline />
        </Container>
      </div>
    </AlertState>  
    </UploadState>
  );
}

export default App;
