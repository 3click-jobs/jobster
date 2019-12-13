import React from 'react'
import { connect } from 'react-redux'
import { actions as companiesActions } from '../../../redux/actions/companies'
import { companiesSelectors } from '../../../redux/selectors/companies'
import { Formik } from 'formik'
import { RegisterCompanyForm } from './RegisterCompanyForm'
import { companies as companiesSchema } from '../../../common/utils/schemas/companies'

const initialValues = {
  companyName: 'JobsterLTD',
  companyRegistrationNumber: '12345678912345',
  mobilePhone: "381645242122",
  email: "mail1@m21ail.com",
  city: "Novi Sad",
  country: "Serbia",
  iso2Code: "SER",
  countryRegion: "Vojvodina",
  longitude: "45.5",
  latitude: "45.6",
  about: "Something about Jobster",
  username: "jobster",
  accessRole: "ROLE_USER",
  password: "password",
  confirmedPassword: "password",
}

export const RegisterCompany = ({
  isFetching,
  isError,
  createCompany
}) => {
  return (
    <Formik
      initialValues={initialValues}
      onSubmit={(values, formikBag) => {


        // registerCompany(values)
        //   .then(resp => {
        //     console.log('not err: ', resp)
        //     // if resp.errors -> you know its well formed so you can render it, else, depending on if it is 200 or 201 etc...
        //     formikBag.setErrors({ ...resp.errors })
        //   })
        //   .catch(err => console.log('error: ', err))
        console.log(values)
        const payload = {...values}

        createCompany(payload).then(() => formikBag.setSubmitting(false))
      }}

      validationSchema={companiesSchema.create}
      component={RegisterCompanyForm}
    />
  )
}

const mapStateToProps = (state) => ({
  isFetching: companiesSelectors.isFetching(state),
  isError: companiesSelectors.error(state)
})

const mapDispatchToProps = (dispatch) => {
  return {
    createCompany: (payload) => dispatch(companiesActions.create(payload))
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RegisterCompany)