import React from 'react'
import { connect } from 'react-redux'
import { compose } from 'redux'
import { withRouter } from 'react-router-dom'

import { unassignCredentials } from '../../redux/actions/user'

export const Signout = ({
  username,
  role,
  unassignCredentials,
  history
}) => {
  return (
    <div>
      Sign out as {username}?
      <button onClick={() => { unassignCredentials(); history.push('/') }}>SIGNOUT</button>
      <button>Cancel</button>
    </div>
  )
}

const mapStateToProps = (state) => ({
  // isFetching: accountsSelectors.isFetching(state),
  // isError: accountsSelectors.error(state),
  username: state.user.username,
  role: state.user.accessRole
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