import React, { Fragment } from 'react';
import './App.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import { Container } from '@material-ui/core';

import Navbar from './components/layout/Navbar';
import Alert from './components/layout/Alert';
import Schedule from './components/schedule/Schedule';
import Baseline from './components/schedule/Baseline';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import Admin from './components/pages/Admin';
import Profile from './components/pages/Profile';
import Landing from './components/pages/Landing';
import History from './components/history/History';
import HistoryDetails from './components/history/HistoryDetails';
import PrivateRoute from './components/routing/PrivateRoute';

import UploadState from './context/upload/UploadState';
import AlertState from './context/alert/AlertState';
import AuthState from './context/auth/AuthState';
import AdminState from './context/admin/AdminState';
import setAuthToken from './utils/setAuthToken';

if(localStorage.token){
  setAuthToken(localStorage.token);
}

const App = () => {
  return (
    <AuthState>
    <UploadState>
    <AdminState>
    <AlertState>
      <Router>
        <Fragment>
          <Navbar />
            <Container>
              <Alert />
                <Switch>
                  <PrivateRoute exact path='/' component={Landing} />
                  <PrivateRoute exact path='/baseline' component={Baseline} />
                  <PrivateRoute exact path='/schedule' component={Schedule} />
                  <PrivateRoute exact path='/admin' component={Admin} />
                  <PrivateRoute exact path='/profile' component={Profile} />
                  <PrivateRoute exact path='/history' component={History} />
                  <PrivateRoute exact path='/history/:id' component={HistoryDetails} />
                  <Route exact path='/login' component={Login} />
                  <Route exact path='/register' component={Register} />
                </Switch>
            </Container>
        </Fragment>
      </Router>
    </AlertState>
    </AdminState>
    </UploadState>
    </AuthState>
  );
}

export default App;
