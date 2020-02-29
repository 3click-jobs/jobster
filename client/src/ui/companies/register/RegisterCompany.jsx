import React from 'react'
import { connect } from 'react-redux'
import { actions as companiesActions } from '../../../redux/actions/companies'
import { companiesSelectors } from '../../../redux/selectors/companies'
import { Formik } from 'formik'
import { RegisterCompanyForm } from './RegisterCompanyForm'
import { companies as companiesSchema } from '../../../common/utils/schemas/companies'
import ToTopButton from '../../toTopButton/ToTopButton'

import PropTypes from 'prop-types'

// const initialDummyValues = {
//   companyName: 'PeraLTD',
//   companyRegistrationNumber: '12349978912345',
//   mobilePhone: "381649942122",
//   email: "mail99@m21ail.com",
//   city: "Novi Sad",
//   country: "Serbia",
//   iso2Code: "SER",
//   countryRegion: "Vojvodina",
//   longitude: "45.5",
//   latitude: "45.6",
//   about: "Something about Jobster",
//   username: "jobster",
//   accessRole: "ROLE_USER",
//   password: "password",
//   confirmedPassword: "password",
// }

const initialDummyValues = {
  companyName: '',
  companyRegistrationNumber: '',
  mobilePhone: "",
  email: "",
  city: "",
  country: "",
  iso2Code: "",
  countryRegion: "",
  longitude: "",
  latitude: "",
  about: "",
  username: "",
  accessRole: "ROLE_USER",
  password: "",
  confirmedPassword: "",
}

/**
 * Register new company (entity offering jobs) container component.
 */
export const RegisterCompany = ({
  isFetching,
  isError,
  createCompany,
  profile
}) => {


  const useComponentDidMount = func => React.useEffect(func, []);

  const useComponentWillMount = func => {
    const willMount = React.useRef(true);
  
    if (willMount.current) {
      func();
    }
  
    useComponentDidMount(() => {
      willMount.current = false;
    });
  };

  let initialValues={}
  let showPassword = false
  let showConfirmedPassword = false

  useComponentWillMount(() => {
    if (profile) {
      initialValues={...profile, showPassword: showPassword, showConfirmedPassword: showConfirmedPassword}
    } else if (initialDummyValues) {
      initialValues={...initialDummyValues, showPassword: showPassword, showConfirmedPassword: showConfirmedPassword}
    }
  })


  return (
    <React.Fragment>
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
          // console.log(values)
          const payload = {...values}

          createCompany(payload).then(() => formikBag.setSubmitting(false))
        }}

        validationSchema={companiesSchema.create}
        component={RegisterCompanyForm}
      />
      <ToTopButton />
    </React.Fragment>
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

RegisterCompany.propTypes = {
  /**
   * Flag indicating if data is being fetched.
   */
  isFetching: PropTypes.bool,
  /**
   * Flag indicating if there was an error fetching the data.
   */
  isError: PropTypes.bool,
  /**
   * Callback function handling the creation of a new entity based on form input.
   */
  createCompany: PropTypes.func,
  /**
   * Profile object with company data.
   */
  profile: PropTypes.object
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RegisterCompany)