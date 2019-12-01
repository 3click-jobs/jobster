import React from 'react'
import { Link  } from 'react-router-dom';
import IconButton from '@material-ui/core/IconButton'
import AccountCircle from '@material-ui/icons/AccountCircle';
import Menu from '@material-ui/core/Menu'
import MenuItem from '@material-ui/core/MenuItem'

export const NavbarProfileAdmin = () => {
  const [anchorEl, setAnchorEl] = React.useState(null)

  const isMenuOpen = Boolean(anchorEl)

  const handleProfileMenuOpen = event => {
    setAnchorEl(event.currentTarget);
  }

  const handleMenuClose = () => {
    setAnchorEl(null);
  }

  const menuId = 'primary-search-account-menu'

  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
      id={menuId}
      keepMounted
      transformOrigin={{ vertical: 'top', horizontal: 'right' }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      <MenuItem onClick={handleMenuClose} component={Link} to='/profile'>
        Profile
      </MenuItem>
      <MenuItem onClick={handleMenuClose}>
        <Link to='/account'>My Account</Link>
      </MenuItem>
      <MenuItem onClick={handleMenuClose}>
        <Link to='/signout'>Sign-out</Link>
      </MenuItem>
    </Menu>
  )

  return (
    <React.Fragment>
      <IconButton
        aria-label="account of current user"
        aria-controls={menuId}
        aria-haspopup="true"
        onClick={handleProfileMenuOpen}
        color="inherit"
      >
        <AccountCircle />
      </IconButton>
      {renderMenu}
    </React.Fragment>
  )
}

export default NavbarProfileAdmin