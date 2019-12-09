import React, { useState } from 'react'
import { connect } from 'react-redux'
import { compose } from 'redux'
import { withRouter } from 'react-router-dom'
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

import { unassignCredentials } from '../../redux/actions/user'

export const Signout = ({
  username,
  // role,
  unassignCredentials,
  history
}) => {
  const [open, setOpen] = useState(true);

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
        onClose={() => { setOpen(false); history.goBack() }}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{"Sign out"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
          Are you sure you want to sign out as {username}?.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => history.goBack() } color="primary">
            Disagree
          </Button>
          <Button onClick={() => { unassignCredentials(); history.push('/') }} color="primary" autoFocus>
            Agree
          </Button>
        </DialogActions>
      </Dialog>
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
    unassignCredentials: () => dispatch(unassignCredentials()),
  }
}

export default compose(
  withRouter,
  connect(
    mapStateToProps,
    mapDispatchToProps
  )
)(Signout)