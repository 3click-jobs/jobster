import React from 'react'
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
  username
}) => {
  const classes = useStyles()
  const [drawer, setDrawer] = React.useState(false)

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
          <NavbarDrawer />
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

          {
            (role === 'ROLE_GUEST') &&
            <NavbarProfileGuest role={role} username={username} />
          }
          {
            (role === 'ROLE_USER') &&
            <NavbarProfileUser role={role} username={username} />
          }
          {
            (role === 'ROLE_ADMIN') &&
            <NavbarProfileAdmin role={role} username={username} />
          }

        </Toolbar>
      </AppBar>
      <Drawer
        anchor='left'
        open={drawer}
        onClose={toggleDrawer(false)}
      >
        <p>THis is a drawer</p>
      </Drawer>
    </div>
  )
}

export default NavbarGeneric