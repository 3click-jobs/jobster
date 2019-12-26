import React from 'react'
import { connect } from 'react-redux'
import { actions as offersActions } from '../../../redux/actions/offers'
import { offersSelectors } from '../../../redux/selectors/offers'
import { Formik } from 'formik'
import { PostOfferForm } from './PostOfferForm'
import { offers as offersSchema } from '../../../common/utils/schemas/offers'

const initialValues = {
    employer: {
        about: "Something about JobsterDOOD",
        accessRole: "ROLE_USER",
        city: "Temerin",
        companyName: "JobsterDOOD",
        companyRegistrationNumber: "12344478912345",
        confirmedPassword: "password",
        country: "Serbia",
        countryRegion: "Vojvodina",
        email: "mail144@m21ail.com",
        id: 3,
        iso2Code: "RS",
        latitude: "45.40861",
        longitude: "19.88917",
        mobilePhone: "381644442122",
        password: "password",
        username: "jobsterDOOD",
    },
    jobType: {
        id: 7,
        jobTypeName: "Bagerista"
    },
    detailsLink: "Kao nesto da se radi kao dodatni posao.",
    numberOfEmployees: 5,
    price: 200.50,
    elapse: false,
    beginningDate: "2019-12-22",
    endDate: "2020-03-03",
    flexibileDates: true,
    flexibileDays: true,
    listJobDayHoursPostDto: [
        {    
          day: "DAY_MONDAY",
          flexibileHours: false,
          fromHour: 16,
          id: 28,
          isMinMax: false,
          toHour: 19
        },
        {    
          day: "DAY_THURSDAY",
          flexibileHours: true,
          fromHour: 16,
          id: 29,
          isMinMax: false,
          toHour: 19
        },
        {    
          day: "DAY_FRIDAY",
          flexibileHours: true,
          fromHour: 2,
          id: 30,
          isMinMax: true,
          toHour: 5,
        }    
    ],
        city: "Temerin",
        country: "Serbia",
        countryRegion: "Vojvodina",
        longitude: 19.88917,
        latitude: 45.40861,
        iso2Code: "RS"
}


export const PostOffer = ({
  isFetching,
  isError,
  createOffer,
  loadOffersAll,
  city,
  jobType
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

  useComponentWillMount(() => {
    if (city && jobType) {
      initialValues.jobType = {...jobType};
      initialValues.city = city.city;
      initialValues.country = city.country;
      initialValues.countryRegion = city.countryRegion;
      initialValues.longitude = city.longitude;
      initialValues.latitude = city.latitude;
      initialValues.iso2Code = city.iso2Code;
      console.log(initialValues)
    }
  })


  return (
    <Formik
      initialValues={initialValues}
      onSubmit={(values, formikBag) => {

          const payload = {...values}

          createOffer(payload).then(() => {formikBag.setSubmitting(false); loadOffersAll()})
      }}

      validationSchema={offersSchema.create}
      component={PostOfferForm}
    />
  )
}

const mapStateToProps = (state) => ({
  isFetching: offersSelectors.isFetching(state),
  isError: offersSelectors.error(state)
})

const mapDispatchToProps = (dispatch) => {
  return {
    createOffer: (payload) => dispatch(offersActions.create(payload)),
    loadOffersAll: () => dispatch(offersActions.fetchAll()),
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PostOffer)