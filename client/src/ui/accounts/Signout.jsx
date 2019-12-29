import React from 'react'
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

// import { unassignCredentials } from '../../redux/actions/user'

export const Signout = ({
  username,
  unassignCredentials,
  history,
  open,
  setOpen
}) => {
  
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
        <DialogTitle id="alert-dialog-title">{"Sign out"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
          Are you sure you want to sign out as {username}?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => history.goBack() } color="primary">
            Cancel
          </Button>
          <Button onClick={() => { unassignCredentials(); history.push('/'); setOpen(false) }} color="primary" autoFocus>
            Ok
          </Button>
        </DialogActions>
      </Dialog>
  )
}


export default Signout