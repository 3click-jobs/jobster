import React from 'react'
import { connect } from 'react-redux'
import MenuIcon from '@material-ui/icons/Menu'
import IconButton from '@material-ui/core/IconButton'
import Drawer from '@material-ui/core/Drawer'
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import { Link as RouterLink } from 'react-router-dom';

// import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import FaceIcon from '@material-ui/icons/Face';
import BusinessIcon from '@material-ui/icons/Business';
import RecentActors from '@material-ui/icons/RecentActors';
import Build from '@material-ui/icons/Build';
import { useTranslation } from 'react-i18next'

const ConnectedLink = React.forwardRef((props, ref) => <RouterLink innerRef={ref} {...props} />);

function ListItemLink(props) {
  return <ListItem button component={ConnectedLink} {...props} />;
}

export const NavbarDrawer = ({
    role
  }) => {

  const [drawer, setDrawer] = React.useState(false)

  const { t } = useTranslation();

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
          {/* <List component="nav" aria-label="register entities">
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
          <Divider /> */}
          { role==="ROLE_ADMIN" &&
          <React.Fragment>
            <List component="nav" aria-label="Users">
              <ListItemLink to='/persons'>
                <ListItemIcon>
                  <FaceIcon />
                </ListItemIcon>
                <ListItemText 
                  primary={t('navbarDrawer.browsePersons')}
                  // primary="Browse persons" 
                />
              </ListItemLink>
              <ListItemLink to='/companies'>
                <ListItemIcon>
                  <BusinessIcon />
                </ListItemIcon>
                <ListItemText 
                  primary={t('navbarDrawer.browseCompanies')}
                  // primary="Browse companies" 
                />
              </ListItemLink>
            </List>
            <Divider />
            <List component="nav" aria-label="Offers and seeks">
              <ListItemLink to='/seeks'>
                <ListItemIcon>
                  <RecentActors />
                </ListItemIcon>
                <ListItemText 
                  primary={t('navbarDrawer.browseJobSeeks')}
                  // primary="Browse job seeks" 
                />
              </ListItemLink>
              <ListItemLink to='/offers'>
                <ListItemIcon>
                  <Build />
                </ListItemIcon>
                <ListItemText 
                  primary={t('navbarDrawer.browseJobOffers')}
                  // primary="Browse job offers" 
                />
              </ListItemLink>
            </List>
          </React.Fragment>
          }
          { role==="ROLE_USER" &&
          <React.Fragment>
            <List component="nav" aria-label="Employees">
              <ListItemLink to='/apply'>
                <ListItemIcon>
                  <FaceIcon />
                </ListItemIcon>
                <ListItemText 
                  primary={t('navbarDrawer.getAJob')}
                  // primary="Get a job" 
                />
              </ListItemLink>
              <ListItemLink to='/seek'>
                <ListItemIcon>
                  <BusinessIcon />
                </ListItemIcon>
                <ListItemText 
                  primary={t('navbarDrawer.seekForAJob')}
                  // primary="Seek for a job" 
                />
              </ListItemLink>
            </List>
            <Divider />
            <List component="nav" aria-label="Employers ">
              <ListItemLink to='/employ'>
                <ListItemIcon>
                  <RecentActors />
                </ListItemIcon>
                <ListItemText 
                  primary={t('navbarDrawer.findAEmployee')}
                  // primary="Find a employee" 
                />
              </ListItemLink>
              <ListItemLink to='/offer'>
                <ListItemIcon>
                  <Build />
                </ListItemIcon>
                <ListItemText 
                  primary={t('navbarDrawer.offerAJob')}
                  // primary="Offer a job" 
                />
              </ListItemLink>
            </List>
          </React.Fragment>
          }
        </div>

      </Drawer>
    </React.Fragment>
  )
}

const mapStateToProps = (state) => {
  return {
    role: state.user.role
  }
}

export default connect(
  mapStateToProps
)(NavbarDrawer)