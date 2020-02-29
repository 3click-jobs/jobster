import React from 'react'
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import { useTranslation } from 'react-i18next'

import PropTypes from 'prop-types'
// import { unassignCredentials } from '../../redux/actions/user'

/**
 * Component for user sign-out.
 */
export const Signout = ({
  username,
  unassignCredentials,
  history,
  open,
  setOpen
}) => {
  
  const { t } = useTranslation();

  // const [open, setOpen] = useState(false);

  // const handleClose = () => {
  //   setOpen(false);
  // };


  // <div>
  //     Sign out as {username}?
  //     <button onClick={() => { unassignCredentials(); history.push('/') }}>SIGNOUT</button>
  //     <button>Cancel</button>
  // </div>


  return (
      <Dialog
        open={open}
        onClose={() => { setOpen(false) }}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{t('signout.title')}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            {t('signout.text')}
            {/* Are you sure you want to sign out as  */}
            {username}?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false) } color="primary">
            {t('signout.buttonNo')}
            {/* Cancel */}
          </Button>
          <Button onClick={() => { unassignCredentials(); history.push('/'); setOpen(false) }} color="primary" autoFocus>
            {t('signout.buttonYes')}
            {/* Ok */}
          </Button>
        </DialogActions>
      </Dialog>
  )
}

Signout.propTypes = {
  /**
   * The currently logged in users username
   */
  username: PropTypes.string,
  /**
   * Callback function that will remove the credentials.
   */
  unassignCredentials: PropTypes.func,
  /**
   * Browsing history, from the react-router HOC.
   */
  history: PropTypes.object,
  /**
   * Dialog flag.
   */
  open: PropTypes.bool,
  /**
   * Callback function to set the open flag to false, called on Dialog close.
   */
  setOpen: PropTypes.func
}

export default Signout