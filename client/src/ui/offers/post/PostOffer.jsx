import React from 'react'
import { connect } from 'react-redux'
import { actions as offersActions } from '../../../redux/actions/offers'
import { offersSelectors } from '../../../redux/selectors/offers'
import { Formik } from 'formik'
import { PostOfferForm } from './PostOfferForm'
import { offers as offersSchema } from '../../../common/utils/schemas/offers'
import ToTopButton from '../../toTopButton/ToTopButton'

// const initialValues = {
//     employer: {
//         about: "Something about JobsterDOOD",
//         accessRole: "ROLE_USER",
//         city: "Temerin",
//         companyName: "JobsterDOOD",
//         companyRegistrationNumber: "12344478912345",
//         confirmedPassword: "password",
//         country: "Serbia",
//         countryRegion: "Vojvodina",
//         email: "mail144@m21ail.com",
//         id: 3,
//         iso2Code: "RS",
//         latitude: "45.40861",
//         longitude: "19.88917",
//         mobilePhone: "381644442122",
//         password: "password",
//         username: "jobsterDOOD",
//     },
//     jobType: {
//         id: 7,
//         jobTypeName: "Bagerista"
//     },
//     detailsLink: "Kao nesto da se radi kao dodatni posao.",
//     numberOfEmployees: 5,
//     price: 200.50,
//     elapse: false,
//     beginningDate: "2019-12-22",
//     endDate: "2020-03-03",
//     flexibileDates: true,
//     flexibileDays: true,
//     listJobDayHoursPostDto: [
//         {    
//           day: "DAY_MONDAY",
//           flexibileHours: false,
//           fromHour: 16,
//           id: 28,
//           isMinMax: false,
//           toHour: 19
//         },
//         {    
//           day: "DAY_TUESDAY",
//           flexibileHours: false,
//           fromHour: 2,
//           id: 29,
//           isMinMax: true,
//           toHour: 8
//         },
//         {    
//           day: "DAY_WEDNESDAY",
//           flexibileHours: true,
//           fromHour: 16,
//           id: 30,
//           isMinMax: false,
//           toHour: 19
//         },        
//         {    
//           day: "DAY_THURSDAY",
//           flexibileHours: true,
//           fromHour: 4,
//           id: 31,
//           isMinMax: true,
//           toHour: 12
//         },
//         {    
//           day: "DAY_FRIDAY",
//           flexibileHours: false,
//           fromHour: 2,
//           id: 32,
//           isMinMax: true,
//           toHour: 5,
//         },
//         {    
//           day: "DAY_SATURDAY",
//           flexibileHours: false,
//           fromHour: 16,
//           id: 33,
//           isMinMax: false,
//           toHour: 19
//         },
//         {    
//           day: "DAY_SUNDAY",
//           flexibileHours: false,
//           fromHour: 1,
//           id: 34,
//           isMinMax: true,
//           toHour: 4
//         },
//     ],
//         city: "Temerin",
//         country: "Serbia",
//         countryRegion: "Vojvodina",
//         longitude: 19.88917,
//         latitude: 45.40861,
//         iso2Code: "RS"
// }

const initialValues = {
  employer: undefined,
  jobType: undefined,
  detailsLink: "",
  numberOfEmployees: 0,
  price: 0,
  elapse: false,
  beginningDate: new Date().toISOString().slice(0, 10),
  endDate: new Date().toISOString().slice(0, 10),
  flexibileDates: false,
  flexibileDays: false,
  listJobDayHoursPostDto: [],
      city: "",
      country: "",
      countryRegion: "",
      longitude: undefined,
      latitude: undefined,
      iso2Code: ""
}


export const PostOffer = ({
  isFetching,
  isError,
  createOffer,
  loadOffersAll,
  city,
  jobType,
  employer,
  setCity,
  handleJobType
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
      initialValues.employer = {...employer}
      initialValues.jobType = {...jobType};
      initialValues.name = city.name; 
      initialValues.countryName = city.countryName; 
      initialValues.city = city.city;
      initialValues.country = city.country;
      initialValues.countryRegion = city.countryRegion;
      initialValues.longitude = city.longitude;
      initialValues.latitude = city.latitude;
      initialValues.iso2Code = city.iso2Code;
    }
  })

  // React.useEffect(() => {
  //   handleJobType({...initialValues.jobType}); 
  // }, [initialValues.jobType, handleJobType])

  // React.useEffect(() => {
  //   setCity({ name: initialValues.name, 
  //                   countryName: initialValues.countryName, 
  //                   city: initialValues.city, 
  //                   country: initialValues.country,
  //                   iso2Code: initialValues.iso2Code,
  //                   countryRegion: initialValues.countryRegion,
  //                   longitude: initialValues.longitude,
  //                   latitude: initialValues.latitude });
  // }, [initialValues.city, setCity])

  
  return (
    <React.Fragment>
      <Formik
        initialValues={initialValues}
        onSubmit={(values, formikBag) => {

            const payload = {...values}
            // console.log("payload")
            console.log(payload)

            createOffer(payload).then(() => {formikBag.setSubmitting(false); loadOffersAll()})
            // console.log("DONE")

        }}

        validationSchema={offersSchema.create}
        component={PostOfferForm}
      />
      <ToTopButton />
    </React.Fragment>
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