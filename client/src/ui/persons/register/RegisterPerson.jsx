import React from 'react'
import { connect } from 'react-redux'
import { actions as personsActions } from '../../../redux/actions/persons'
import { personsSelectors } from '../../../redux/selectors/persons'
import { Formik } from 'formik'
import { RegisterPersonForm } from './RegisterPersonForm'
import { persons as personsSchema } from '../../../common/utils/schemas/persons'

const initialDummyValues = {
  firstName: 'Perica',
  lastName: 'Mitrovic',
  gender: 'GENDER_MALE',
  birthDate: '2000-12-12',
  mobilePhone: "381605229922",
  email: "mail88@m2ail.com",
  city: "Novi Sad",
  country: "Serbia",
  iso2Code: "SER",
  countryRegion: "Vojvodina",
  longitude: "45.5",
  latitude: "45.6",
  about: "Something about Mary",
  username: "makaroni2",
  accessRole: "ROLE_USER",
  password: "password",
  confirmedPassword: "password",
}

export const RegisterPerson = ({
  isFetching,
  isError,
  createPerson,
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

  useComponentWillMount(() => {
    if (profile) {
      initialValues={...profile}
    } else if (initialDummyValues) {
      initialValues={...initialDummyValues}
    }
  })

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

        createPerson(payload).then(() => {formikBag.setSubmitting(false)})
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