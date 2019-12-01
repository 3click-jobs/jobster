import React from 'react'
import { connect } from 'react-redux'
import { actions as accountsActions } from '../../redux/actions/accounts'
import { accountsSelectors } from '../../redux/selectors/accounts'
import { Formik } from 'formik'
import { RegisterAccountForm } from './RegisterAccountForm'
import { accounts as accountsSchema } from '../../common/utils/schemas/accounts'

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

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RegisterAccount)