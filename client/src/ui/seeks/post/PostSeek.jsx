import React from 'react'
import { connect } from 'react-redux'
import { actions as seeksActions } from '../../../redux/actions/seeks'
import { seeksSelectors } from '../../../redux/selectors/seeks'
import { Formik } from 'formik'
import { PostSeekForm } from './PostSeekForm'
import { seeks as seeksSchema } from '../../../common/utils/schemas/seeks'
import ToTopButton from '../../toTopButton/ToTopButton'

// const initialValues = {
//     employee: {
//         about: "Something about WWW",
//         accessRole: "ROLE_USER",
//         birthDate: "2000-12-12",
//         city: "Tavankut",
//         confirmedPassword: "password",
//         country: "Serbia",
//         countryRegion: "Vojvodina",
//         email: "mail888@m2ail.com",
//         firstName: "PeraWWW",
//         gender: "GENDER_MALE",
//         id: 4,
//         iso2Code: "RS",
//         lastName: "Peric",
//         latitude: "46.04843",
//         longitude: "19.48721",
//         mobilePhone: "381605228882",
//         password: "password",
//         username: "perawww"
//     },
//     jobType: {
//         id: 7,
//         jobTypeName: "Bagerista"
//     },
//     detailsLink: "Kao nesto da se radi kao dodatni posao.",
//     distanceToJob: 0,
//     price: 200.50,
//     beginningDate: "2019-12-22",
//     endDate: "2020-03-03",
//     flexibileDates: true,
//     flexibileDays: true,
//     listJobDayHoursPostDto: [
//       {    
//         day: "DAY_MONDAY",
//         flexibileHours: false,
//         fromHour: 16,
//         id: 28,
//         isMinMax: false,
//         toHour: 19
//       },
//       {    
//         day: "DAY_TUESDAY",
//         flexibileHours: false,
//         fromHour: 2,
//         id: 29,
//         isMinMax: true,
//         toHour: 8
//       },
//       {    
//         day: "DAY_WEDNESDAY",
//         flexibileHours: true,
//         fromHour: 16,
//         id: 30,
//         isMinMax: false,
//         toHour: 19
//       },        
//       {    
//         day: "DAY_THURSDAY",
//         flexibileHours: true,
//         fromHour: 4,
//         id: 31,
//         isMinMax: true,
//         toHour: 12
//       },
//       {    
//         day: "DAY_FRIDAY",
//         flexibileHours: false,
//         fromHour: 2,
//         id: 32,
//         isMinMax: true,
//         toHour: 5,
//       },
//       {    
//         day: "DAY_SATURDAY",
//         flexibileHours: false,
//         fromHour: 16,
//         id: 33,
//         isMinMax: false,
//         toHour: 19
//       },
//       {    
//         day: "DAY_SUNDAY",
//         flexibileHours: false,
//         fromHour: 1,
//         id: 34,
//         isMinMax: true,
//         toHour: 4
//       },
//   ],
//         city: "Temerin",
//         country: "Serbia",
//         countryRegion: "Vojvodina",
//         longitude: 19.88917,
//         latitude: 45.40861,
//         iso2Code: "RS"
// }

const initialValues = {
  employee: undefined,
  jobType: undefined,
  detailsLink: "",
  distanceToJob: 0,
  price: 0,
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


export const PostSeek = ({
  isFetching,
  isError,
  createSeek,
  loadSeeksAll,
  city,
  jobType,
  employee,
  distance,
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
    if (city && jobType && distance >= 0  ) {
      initialValues.employee = {...employee};
      initialValues.jobType = {...jobType}; 
      initialValues.name = city.name; 
      initialValues.countryName = city.countryName; 
      initialValues.city = city.city;
      initialValues.country = city.country;
      initialValues.countryRegion = city.countryRegion;
      initialValues.longitude = city.longitude;
      initialValues.latitude = city.latitude;
      initialValues.iso2Code = city.iso2Code;
      initialValues.distanceToJob = distance;
    }
  })


  return (
    <React.Fragment>
      <Formik
        initialValues={initialValues}
        onSubmit={(values, actions) => {

          console.log('submitting...')

          actions.setStatus({
            success: 'Submitting post seek...',
            code: 'sending' 
          })

          const payload = {...values}

          createSeek(payload)
            .then(() => {
              actions.setSubmitting(false) 
              loadSeeksAll()
              actions.setStatus({
                success: 'Post seek successfully created.',
                code: 'success'
              })
            })
        }}

        validationSchema={seeksSchema.create}
        component={PostSeekForm}
      />
      <ToTopButton />
    </React.Fragment>
  )
}

const mapStateToProps = (state) => ({
  isFetching: seeksSelectors.isFetching(state),
  isError: seeksSelectors.error(state)
})

const mapDispatchToProps = (dispatch) => {
  return {
    createSeek: (payload) => dispatch(seeksActions.create(payload)),
    loadSeeksAll: () => dispatch(seeksActions.fetchAll()),
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PostSeek)