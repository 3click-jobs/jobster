import * as Yup from 'yup'

const offers = {
    create: Yup.object().shape({
        employer: Yup.object()
        .nullable()
        .required('Employer must be provided.'),
      city: Yup.string()
        .nullable()
        .matches(/^[A-Za-z\s]{2,}$/, 'City name is not valid.')
        .required('City must be provided.'),
      country: Yup.string()
        .matches(/^[A-Za-z\s]{2,}$/, 'Country name is not valid.'),
      iso2Code: Yup.string()
        .matches(/^[A-Za-z]{2,3}$/, 'ISO2 code is not valid.'),
      countryRegion: Yup.string()
        .matches(/^[A-Za-z\s]{0,}$/, 'Country region name is not valid.'),
      longitude: Yup.number()
        .min(-180, 'Longitude  must be ${min} or higher!')
        .max(180, 'Longitude must be ${max} or lower!'),
      latitude: Yup.number()
        .min(-90, 'Latitude  must be ${min} or higher!')
        .max(90, 'Latitude must be ${max} or lower!'),
      beginningDate: Yup.date()
        .nullable()
        .required('Beginning date must be provided.'),
      jobType: Yup.object()
        .nullable()
        .required('Job type must be provided.'),
      listJobDayHoursPostDto: Yup.array(),
      numberOfEmployees: Yup.number()
        .nullable()
        .required('Number of employees must be provided.')
        .min(1, 'Number of employees must be ${min} or higher!'),
      endDate: Yup.date()
        .nullable() 
        .required('End date must be provided.'),
      flexibileDates: Yup.boolean(),
      flexibileDays: Yup.boolean(),
      detailsLink: Yup.string()
        .nullable()
        .required('Details must be provided.'),
      price: Yup.number()
        .nullable()
        .required('Price must be provided.')
        .min(0, 'Price must be ${min} or higher!'),
    })
}

export { offers }