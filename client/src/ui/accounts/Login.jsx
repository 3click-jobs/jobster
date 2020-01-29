import React from 'react'
// import { connect } from 'react-redux'

// import { assignCredentials, verifyUser } from '../../redux/actions/user'

import PropTypes from 'prop-types'

import { Formik } from 'formik'
import { LoginForm } from './LoginForm'
import { accounts as accountsSchema } from '../../common/utils/schemas/accounts'
import Dialog from '@material-ui/core/Dialog';

/**
 * Login component. Using a Dialog component.
 */
export const Login = ({
  isFetching,
  isError,
  username,
  assignCredentials,
  verifyUser,
  history,
  open,
  setOpen
}) => {

  let showPassword = false

  async function _handleSubmit(username, password) {
    await assignCredentials(username, password)
    await verifyUser()
  }


  return (
    <Dialog
        open={open}
        onClose={() => { setOpen(false) }}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
    >
      <Formik
        initialValues={{ username: '', password: '', showPassword: showPassword }}
        onSubmit={(values, formikBag) => {

          
          // registerCompany(values)
          //   .then(resp => {
          //     console.log('not err: ', resp)
          //     // if resp.errors -> you know its well formed so you can render it, else, depending on if it is 200 or 201 etc...
          //     formikBag.setErrors({ ...resp.errors })
          //   })
          //   .catch(err => console.log('error: ', err))

          _handleSubmit(values.username, values.password)
          // await assignCredentials(values.username, values.password)
          // await verifyUser().then(() => {setOpen(false)})
          formikBag.setSubmitting(false)  
          setOpen(false)
        }}

        validationSchema={accountsSchema.login}
        component={LoginForm}
      />
    </Dialog>
  )
}

// const mapStateToProps = (state) => ({
//   // isFetching: accountsSelectors.isFetching(state),
//   // isError: accountsSelectors.error(state),
//   username: state.user.username,
//   role: state.user.accessRole
// })

// const mapDispatchToProps = (dispatch) => {
//   return {
//     assignCredentials: (username, password) => dispatch(assignCredentials(username, password)),
//     verifyUser: () => dispatch(verifyUser())
//   }
// }

// export default connect(
//   mapStateToProps,
//   mapDispatchToProps
// )(Login)

Login.propTypes = {
  /**
   * True if fetching data is in process.
   */  
  isFetching: PropTypes.bool,
  /**
   * If fetching returns with an error the flag will be set to true.
   */
  isError: PropTypes.bool,
  /**
   * The currently logged in users username
   */
  username: PropTypes.string,
  /**
   * Callback function to assign credentials
   */
  assignCredentials: PropTypes.func,
  /**
   * Verify user callback.
   */
  verifyUser: PropTypes.func,
  /**
   * Browsing history
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

export default Login