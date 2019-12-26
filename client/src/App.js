import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { HomePage } from './ui/home/HomePage'
import NavbarGeneric from './ui/navbar/NavbarGeneric';
import RegisterAccount from './ui/accounts/RegisterAccount';
import { ThemeProvider, createMuiTheme } from '@material-ui/core/styles'
import RegisterPerson from './ui/persons/register/RegisterPerson';
import RegisterCompany from './ui/companies/register/RegisterCompany';

import { connect } from 'react-redux'
import { verifyUser, checkCredentials } from './redux/actions/user'
import Login from './ui/accounts/Login';
import Signout from './ui/accounts/Signout'
import ApplyContainer from './ui/apply/ApplyContainer';
import OfferContainer from './ui/offer/OfferContainer';
import PersonsContainer from './ui/persons/browse/PersonsContainer';
import CompaniesContainer from './ui/companies/browse/CompaniesContainer';
import SeeksContainer from './ui/seeks/browse/SeeksContainer';
import OffersContainer from './ui/offers/browse/OffersContainer';
import EmployContainer from './ui/employ/EmployContainer';
import SeekContainer from './ui/seek/SeekContainer';


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
  checkCredentials,
  authReady,
  hasCredentials
}) => {

  React.useEffect(() => {
    checkCredentials()
  }, [hasCredentials, checkCredentials])

  React.useEffect(() => {
    if (hasCredentials)
      verifyUser()
  }, [hasCredentials, verifyUser])

  return (
    <ThemeProvider theme={defaultTheme}>
      {
        !authReady &&
        <p>Waiting for authentication</p>
      }
      {
        authReady &&
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
              <RegisterCompany />
            </Route>
            <Route exact path='/accounts'>

            </Route>
            <Route exact path='/persons'>
              <PersonsContainer />
            </Route>
            <Route exact path='/companies'>
              <CompaniesContainer />
            </Route>
            <Route exact path='/seeks'>
              <SeeksContainer />
            </Route>
            <Route exact path='/offers'>
              <OffersContainer />
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
            <Route exact path='/employ'>
              <EmployContainer />
            </Route>
            <Route exact path='/seek'>
              <SeekContainer />
            </Route>
          </Switch>
        </Router>
      }
    </ThemeProvider>
  );

}

// connect to store
// check user
// 

const mapStateToProps = (state) => {
  return {
    role: state.user.role,
    username: state.user.username,
    authReady: state.user.authReady,
    hasCredentials: state.user.hasCredentials
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
