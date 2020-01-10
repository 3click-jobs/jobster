import * as Yup from 'yup'

const companies = {
  create: Yup.object().shape({
    companyName: Yup.string()
      .nullable()
      .matches(/^[_A-Za-z0-9-]{2,}$/, 'Company name is not valid.')
      .required('Company name must be provided.'),
    companyRegistrationNumber: Yup.string()
      .nullable()
      .matches(/^[0-9]{14}$/, 'Company registration number is not valid, can contain only numbers and must be exactly 14 numbers long.')
      .required('Company registration number must be provided.'),
    mobilePhone: Yup.string()
      .nullable()
      // .matches(/^([\(]{0,1}[\+]{0,1}[\(]{0,1}([3][8][1]){0,1}[\)]{0,1}[- \.\/]{0,1}[\(]{0,1}[0]{0,1}[\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\)]{0,1}[- \.\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$/,
      .matches(/^([(]{0,1}[+]{0,1}[(]{0,1}([3][8][1]){0,1}[)]{0,1}[- ./]{0,1}[(]{0,1}[0]{0,1}[)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[)]{0,1}[- ./]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$/,
        { message: 'Mobile phone number is not valid.', excludeEmptyString: true })
      .required('Phone number must be provided.'),
    email: Yup.string()
      .nullable()
      .max(50, 'E-mail must be maximum 50 characters long.')
      .required('E-mail must be provided.')
      .matches(/^[_A-Za-z0-9-+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/, 'Email is not valid.'),
    city: Yup.string()
      .matches(/^[A-Za-z\s]{2,}$/, 'City name is not valid.')
      .required('City must be provided.'),
    country: Yup.string()
      .matches(/^[A-Za-z\s]{2,}$/, 'Country name is not valid.'),
    iso2Code: Yup.string()
      .matches(/^[A-Za-z]{2,3}$/, 'ISO2 code is not valid.'),
    countryRegion: Yup.string()
      .matches(/^[A-Za-z\s]{0,}$/, 'Country region name is not valid.'),
    longitude: Yup.number()
      .min(-180, 'Longitude  must be -180 or higher!')
      .max(180, 'Longitude must be 180 or lower!'),
    latitude: Yup.number()
      .min(-90, 'Latitude  must be -90 or higher!')
      .max(90, 'Latitude must be 90 or lower!'),
    about: Yup.string()
      .max(255, 'About must be maximum 255 characters long.'),
    rating: Yup.number()
      .min(0).max(5),
    numberOfRatings: Yup.number().min(0),
    username: Yup.string()
      .required('Username must be provided.')
      .min(5, 'Username must be at least 5 characters long.')
      .max(20, 'Username must be at most 20 characters long.'),
    accessRole: Yup.string()
      .required('User role must be provided.')
      .oneOf(['ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST'], 'Role is not valid, must be ROLE_ADMIN, ROLE_USER or ROLE_GUEST"'),
    password: Yup.string()
      .required('Password must be provided.')
      .min(5, 'Password must be 5 characters long or higher.')
      // .matches(/(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{5,}/, 'Password is not valid, must contain at least 1 upper case letter, 1 lower case letter and 1 digit, no whitespace allowed.')
      .matches(/^[A-Za-z0-9]*$/, 'Password is not valid, must contain only letters and numbers.'),
    confirmedPassword: Yup.string()
      .required('Password must be provided.')
      .min(5, 'Password must be 5 characters long or higher.')
      .matches(/^[A-Za-z0-9]*$/, 'Password is not valid, must contain only letters and numbers.')
  })
}

export { companies }