import React from 'react'
import { connect } from 'react-redux'
import { compose } from 'redux'
import { withRouter } from 'react-router-dom'
import { assignCredentials, verifyUser } from '../../redux/actions/user'
import { unassignCredentials } from '../../redux/actions/user'
import AppBar from '@material-ui/core/AppBar'
import Toolbar from '@material-ui/core/Toolbar'
// import Typography from '@material-ui/core/Typography'
// import TouchAppIcon from '@material-ui/icons/TouchApp';
// import IconButton from '@material-ui/core/IconButton'
// import AccountCircle from '@material-ui/icons/AccountCircle';
import Drawer from '@material-ui/core/Drawer'
import NavbarDrawer from './NavbarDrawer'
import { makeStyles } from '@material-ui/styles';
import NavbarProfileGuest from './NavbarProfileGuest';
import NavbarProfileUser from './NavbarProfileUser';
import NavbarProfileAdmin from './NavbarProfileAdmin';
import { Link } from 'react-router-dom'
import Signout from '../accounts/Signout'
import Login from '../accounts/Login'
import LanguageSelector from '../languageSelector/LanguageSelector'

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
  },
  title: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    textAlign: 'center',
    flexGrow: 1,
    '& a': {
      textDecoration: 'none',
    },
    '& img': {
      position: 'relative',
      top: '1px',
      height: '40px'
    }
  }
}))

export const NavbarGeneric = ({
  role,
  username,
  history,
  assignCredentials, 
  verifyUser,
  unassignCredentials
}) => {
  const classes = useStyles()
  const [drawer, setDrawer] = React.useState(false)
  const [openedSignout, setOpenedSignout] = React.useState(false)
  const [openedLogin, setOpenedLogin] = React.useState(false)

  
  const toggleDrawer = state => event => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }

    setDrawer(state);
  }


  return (
    <div className={classes.root}>
      <AppBar position='static'>
        <Toolbar variant="dense" >
          { (role==="ROLE_USER" || role==="ROLE_ADMIN") && 
            <NavbarDrawer />
          }
          <div className={classes.title}>
            {/* <Typography variant="h6">
              3
          </Typography>
            <IconButton
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              color="inherit"
            >
              <TouchAppIcon />
            </IconButton>
            <Typography variant="h6">
              Jobs
          </Typography> */}
            <Link to='/'><img alt="3 Click Jobs" src='/img/triklikjobslogo.png' /></Link>
          </div>
          
          <LanguageSelector />

          {
            (role === 'ROLE_GUEST') &&
            <React.Fragment>
              <NavbarProfileGuest role={role} username={username} setOpen={() => setOpenedLogin(true)} />
              <Login username={username} open={openedLogin} setOpen={setOpenedLogin} verifyUser={verifyUser} assignCredentials={assignCredentials} history={history} />
            </React.Fragment>
          }
          {
            (role === 'ROLE_USER') &&
            <NavbarProfileUser role={role} username={username} setOpen={() => setOpenedSignout(true)} />
          }
          {
            (role === 'ROLE_ADMIN') &&
            <NavbarProfileAdmin role={role} username={username} setOpen={() => setOpenedSignout(true)} />
          }

        </Toolbar>
      </AppBar>
      { (role==="ROLE_USER" || role==="ROLE_ADMIN") && 
        <React.Fragment>
          <Drawer
            anchor='left'
            open={drawer}
            onClose={toggleDrawer(false)}
          >
            <p>3 Click Jobs</p>
          </Drawer>
          <Signout username={username} open={openedSignout} setOpen={setOpenedSignout} unassignCredentials={unassignCredentials} history={history} />
        </React.Fragment>
      }
    </div>
  )
}

const mapStateToProps = (state) => ({
  // isFetching: accountsSelectors.isFetching(state),
  // isError: accountsSelectors.error(state),
  username: state.user.username,
  // role: state.user.accessRole
})

const mapDispatchToProps = (dispatch) => {
  return {
    assignCredentials: (username, password) => dispatch(assignCredentials(username, password)),
    verifyUser: () => dispatch(verifyUser()),
    unassignCredentials: () => dispatch(unassignCredentials()),
  }
}

export default compose(
  withRouter,
  connect(
    mapStateToProps,
    mapDispatchToProps
  )
)(NavbarGeneric)