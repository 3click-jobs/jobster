import React from 'react'
import MenuIcon from '@material-ui/icons/Menu'
import IconButton from '@material-ui/core/IconButton'
import Drawer from '@material-ui/core/Drawer'
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import { Link as RouterLink } from 'react-router-dom';

import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import FaceIcon from '@material-ui/icons/Face';
import BusinessIcon from '@material-ui/icons/Business';

const ConnectedLink = React.forwardRef((props, ref) => <RouterLink innerRef={ref} {...props} />);

function ListItemLink(props) {
  return <ListItem button component={ConnectedLink} {...props} />;
}

export const NavbarDrawer = () => {
  const [drawer, setDrawer] = React.useState(false)

  const toggleDrawer = state => event => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }

    setDrawer(state);
  }

  return (
    <React.Fragment>
      <IconButton edge="start" color="inherit" aria-label="menu" onClick={toggleDrawer(true)}>
        <MenuIcon />
      </IconButton>
      <Drawer
        anchor='left'
        open={drawer}
        onClose={toggleDrawer(false)}
      >
        <div
          role="presentation"
          onClick={toggleDrawer(false)}
          onKeyDown={toggleDrawer(false)}
        >
          <List component="nav" aria-label="register entities">
            <ListItemLink to='/register-account'>
              <ListItemIcon>
                <AccountCircleIcon />
              </ListItemIcon>
              <ListItemText primary="New Account" />
            </ListItemLink>
            <ListItemLink to='/register-person'>
              <ListItemIcon>
                <FaceIcon />
              </ListItemIcon>
              <ListItemText primary="New Person" />
            </ListItemLink>
            <ListItemLink to='/register-company'>
              <ListItemIcon>
                <BusinessIcon />
              </ListItemIcon>
              <ListItemText primary="New Company" />
            </ListItemLink>
          </List>
          <Divider />
          <List component="nav" aria-label="browse entities">
            <ListItem button>
              <ListItemText primary="Trash" />
            </ListItem>
            <ListItemLink to='/profile'>
              <ListItemText primary="Spam" />
            </ListItemLink>
          </List>
        </div>

      </Drawer>
    </React.Fragment>
  )
}

export default NavbarDrawer