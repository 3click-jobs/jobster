import React from 'react'
import { connect } from 'react-redux'
import { actions as personsActions } from '../../../redux/actions/persons'
import { personsSelectors } from '../../../redux/selectors/persons'
import { Formik } from 'formik'
import { RegisterPersonForm } from './RegisterPersonForm'
import { persons as personsSchema } from '../../../common/utils/schemas/persons'
import ToTopButton from '../../toTopButton/ToTopButton'

// const initialDummyValues = {
//   firstName: 'Perica',
//   lastName: 'Mitrovic',
//   gender: 'GENDER_MALE',
//   birthDate: '2000-12-12',
//   mobilePhone: "381605229922",
//   email: "mail88@m2ail.com",
//   city: "Novi Sad",
//   country: "Serbia",
//   iso2Code: "SER",
//   countryRegion: "Vojvodina",
//   longitude: "45.5",
//   latitude: "45.6",
//   about: "Something about Mary",
//   username: "makaroni2",
//   accessRole: "ROLE_USER",
//   password: "password",
//   confirmedPassword: "password",
// }

const initialDummyValues = {
  firstName: '',
  lastName: '',
  gender: '',
  birthDate: new Date().toISOString().slice(0, 10),
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

export const RegisterPerson = ({
  isFetching,
  isError,
  createPerson,
  profile
}) => {


  // const [showPassword, setShowPassword] = React.useState(false)

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
          console.log(values)
          const payload = {...values}

          createPerson(payload).then(() => {formikBag.setSubmitting(false)})
        }}

        validationSchema={personsSchema.create}
        component={RegisterPersonForm}
      />
      <ToTopButton />
    </React.Fragment>
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