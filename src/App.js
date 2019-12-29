import React, { Fragment } from 'react';
import './App.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import { Container } from '@material-ui/core';

import Navbar from './components/layout/Navbar';
import Alert from './components/layout/Alert';
import Home from './components/pages/Home';

import UploadState from './context/upload/UploadState';
import AlertState from './context/alert/AlertState';

const App = () => {
  return (
    <UploadState>
    <AlertState>
      <Router>
        <Fragment>
          <Navbar />
            <Container>
              <Alert />
                <Switch>
                  <Route exact path='/' component={Home} />
                </Switch>
            </Container>
        </Fragment>
      </Router>
    </AlertState>  
    </UploadState>
  );
}

export default App;
