import React from 'react'
import { connect } from 'react-redux'

import { assignCredentials, verifyUser } from '../../redux/actions/user'

import { Formik } from 'formik'
import { LoginForm } from './LoginForm'
import { accounts as accountsSchema } from '../../common/utils/schemas/accounts'

export const Login = ({
  isFetching,
  isError,
  assignCredentials,
  verifyUser
}) => {
  return (
    <Formik
      initialValues={{ username: '', password: '' }}
      onSubmit={async (values, formikBag) => {

        
        // registerCompany(values)
        //   .then(resp => {
        //     console.log('not err: ', resp)
        //     // if resp.errors -> you know its well formed so you can render it, else, depending on if it is 200 or 201 etc...
        //     formikBag.setErrors({ ...resp.errors })
        //   })
        //   .catch(err => console.log('error: ', err))

        await assignCredentials(values.username, values.password)
        await verifyUser()
        formikBag.setSubmitting(false)        
      }}

      validationSchema={accountsSchema.login}
      component={LoginForm}
    />
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
    assignCredentials: (username, password) => dispatch(assignCredentials(username, password)),
    verifyUser: () => dispatch(verifyUser())
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Login)