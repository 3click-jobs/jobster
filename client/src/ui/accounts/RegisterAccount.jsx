import React from 'react'
import { connect } from 'react-redux'
import { actions as accountsActions } from '../../redux/actions/accounts'
import { accountsSelectors } from '../../redux/selectors/accounts'
import { Formik } from 'formik'
import { RegisterAccountForm } from './RegisterAccountForm'
import { accounts as accountsSchema } from '../../common/utils/schemas/accounts'

import PropTypes from 'prop-types'

/**
 * Component for account registration. Server as a container component with meat in the RegisterAccountForm component.
 * Will get data from redux and feed it into the inner component.
 */
export const RegisterAccount = ({
  isFetching,
  isError,
  createAccount
}) => {
  return (
    <Formik
      initialValues={{ username: '', password: '', confirmedPassword: '' }}
      onSubmit={(values, formikBag) => {

        
        // registerCompany(values)
        //   .then(resp => {
        //     console.log('not err: ', resp)
        //     // if resp.errors -> you know its well formed so you can render it, else, depending on if it is 200 or 201 etc...
        //     formikBag.setErrors({ ...resp.errors })
        //   })
        //   .catch(err => console.log('error: ', err))

        const payload = {
          username: values.username,
          password: values.password,
          confirmedPassword: values.confirmedPassword,
          accessRole: 'ROLE_USER'
        }
        createAccount(payload).then(() => formikBag.setSubmitting(false))        
      }}

      validationSchema={accountsSchema.create}
      component={RegisterAccountForm}
    />
  )
}

const mapStateToProps = (state) => ({
  isFetching: accountsSelectors.isFetching(state),
  isError: accountsSelectors.error(state)
})

const mapDispatchToProps = (dispatch) => {
  return {
    createAccount: (payload) => dispatch(accountsActions.create(payload))
  }
}

RegisterAccount.propTypes = {
  /**
   * Flag for data fetching.
   */
  isFetching: PropTypes.bool,
  /**
   * Flag for errors with fetching.
   */
  isError: PropTypes.bool,
  /**
   * Callback function for account creation.
   */
  createAccount: PropTypes.func
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RegisterAccount)