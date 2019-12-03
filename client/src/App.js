import React, { Component } from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { HomePage } from './ui/home/HomePage'
import NavbarGeneric from './ui/navbar/NavbarGeneric';
import RegisterAccount from './ui/accounts/RegisterAccount';
import { ThemeProvider, createMuiTheme } from '@material-ui/core/styles'
import RegisterPerson from './ui/persons/register/RegisterPerson';

import { connect } from 'react-redux'
import { verifyUser, checkCredentials, assignCredentials, unassignCredentials } from './redux/actions/user'
import Login from './ui/accounts/Login';
import Signout from './ui/accounts/Signout'
import ApplyContainer from './ui/apply/ApplyContainer';
import OfferContainer from './ui/offer/OfferContainer';
import PersonsContainer from './ui/persons/browse/PersonsContainer';
import CompaniesContainer from './ui/companies/browse/CompaniesContainer';


const defaultTheme = createMuiTheme({
  palette: {
    primary: {
      main: '#191970',
    }
  }
})

export const App = ({
  role,
  username,
  verifyUser,
  checkCredentials
}) => {

  React.useEffect(() => {
    if (checkCredentials()) {
      verifyUser()
    } else {
      
    }
  }, [role, username])

  return (
    <ThemeProvider theme={defaultTheme}>
    {

    }
      <Router>
        <NavbarGeneric role={role} username={username} />
        <Switch>
          <Route exact path='/'>
            <HomePage />
          </Route>
          <Route exact path='/register-account'>
            <RegisterAccount />
          </Route>
          <Route exact path='/register-person'>
            <RegisterPerson />
          </Route>
          <Route exact path='/register-company'>

          </Route>
          <Route exact path='/accounts'>

          </Route>
          <Route exact path='/persons'>
            <PersonsContainer />
          </Route>
          <Route exact path='/companies'>
            <CompaniesContainer />
          </Route>
          <Route exact path='/login'>
            <Login />
          </Route>
          <Route exact path='/signout'>
            <Signout />
          </Route>
          <Route exact path='/apply'>
            <ApplyContainer />
          </Route>
          <Route exact path='/offer'>
            <OfferContainer />
          </Route>
        </Switch>
      </Router>
    </ThemeProvider>
  );

}

// connect to store
// check user
// 

const mapStateToProps = (state) => {
  return {
    role: state.user.role,
    username: state.user.username
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    verifyUser: () => dispatch(verifyUser()),
    checkCredentials: () => dispatch(checkCredentials())
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(App);
