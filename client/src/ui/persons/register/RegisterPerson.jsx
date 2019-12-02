import React from 'react'
import { connect } from 'react-redux'
import { actions as personsActions } from '../../../redux/actions/persons'
import { personsSelectors } from '../../../redux/selectors/persons'
import { Formik } from 'formik'
import { RegisterPersonForm } from './RegisterPersonForm'
import { persons as personsSchema } from '../../../common/utils/schemas/persons'

export const RegisterPerson = ({
  isFetching,
  isError,
  createPerson
}) => {
  return (
    <Formik
      initialValues={{ gender: 'GENDER_MALE', username: '', password: '', confirmedPassword: '' }}
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
        createPerson(payload).then(() => formikBag.setSubmitting(false))        
      }}

      validationSchema={personsSchema.create}
      component={RegisterPersonForm}
    />
  )
}

const mapStateToProps = (state) => ({
  isFetching: personsSelectors.isFetching(state),
  isError: personsSelectors.error(state)
})

const mapDispatchToProps = (dispatch) => {
  return {
    createPerson: (payload) => dispatch(personsActions.create(payload))
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RegisterPerson)