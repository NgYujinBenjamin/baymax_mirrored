import React, { Fragment } from 'react';
import './App.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import { Container } from '@material-ui/core';

import Navbar from './components/layout/Navbar';
import Alert from './components/layout/Alert';
import Linearize from './components/linearization/Linearize';
import Baseline from './components/linearization/Baseline';
import Login from './components/auth/Login';
import PrivateRoute from './components/routing/PrivateRoute';

import UploadState from './context/upload/UploadState';
import AlertState from './context/alert/AlertState';
import AuthState from './context/auth/AuthState';

const App = () => {
  return (
    <AuthState>
    <UploadState>
    <AlertState>
      <Router>
        <Fragment>
          <Navbar />
            <Container>
              <Alert />
                <Switch>
                  <PrivateRoute exact path='/' component={Baseline} />
                  <PrivateRoute exact path='/linearize' component={Linearize} />
                  <Route exact path='/login' component={Login} />
                </Switch>
            </Container>
        </Fragment>
      </Router>
    </AlertState>  
    </UploadState>
    </AuthState>
  );
}

export default App;
