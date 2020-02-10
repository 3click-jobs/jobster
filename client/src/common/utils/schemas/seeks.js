import * as Yup from 'yup'

const seeks = {
  create: Yup.object().shape({
    employee: Yup.object()
        .nullable()
        .required('Employee must be provided.'),
    city: Yup.string()
        .nullable()
        .matches(/^[A-Za-z\s]{2,}$/, 'City name is not valid.')
        .required('City must be provided.'),
    country: Yup.string()
        .matches(/^[A-Za-z\s]{2,}$/, 'Country name is not valid.'),
    iso2Code: Yup.string()
        .matches(/^[A-Za-z]{2,3}$/, 'ISO2 code is not valid.'),
    countryRegion: Yup.string()
        .matches(/^[A-Za-zĆćČčĐđŠšŽž.\s]{0,}$/, 'Country region name is not valid.'),
    longitude: Yup.number()
        .min(-180, 'Longitude  must be -180 or higher!')
        .max(180, 'Longitude must be 180 or lower!'),
    latitude: Yup.number()
        .min(-90, 'Latitude  must be -90 or higher!')
        .max(90, 'Latitude must be 90 or lower!'),
    beginningDate: Yup.date()
        .nullable()
        .required('Beginning date must be provided.'),
    jobType: Yup.object()
        .nullable()
        .required('Job type must be provided.'),
    listJobDayHoursPostDto: Yup.array(),
    distanceToJob: Yup.number()
        .required('Distance to job must be provided.')
        .min(0, 'Distance to job must be 0 or higher!'),
    endDate: Yup.date()
        .nullable()
        .required('End date must be provided.'),
    flexibileDates: Yup.boolean(),
    flexibileDays: Yup.boolean(),
    detailsLink: Yup.string()
        .nullable()
        .matches(/^.{0,255}$/, 'Details must be 255 characters or lower!.')
        .required('Details must be provided.'),
    price: Yup.number()
        .nullable()
        .required('Price must be provided.')
        .min(0, 'Price must be 0 or higher!'),
  })
}

export { seeks }