import * as yup from 'yup'

const accounts = {
  login: yup.object().shape({
    username: yup.string()
      .required('Username must be provided.')
      .min(5, 'Username must be at least ${min} characters long.')
      .max(20, 'Username must be at most ${max} characters long.'),
    password: yup.string()
      .required('Password must be provided.')
      // .matches(/(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{5,}/, 'Password is not valid, must contain at least 1 upper case letter, 1 lower case letter and 1 digit, no whitespace allowed.')
      .min(5, 'Password must be ${min} characters long or higher.')
      .matches(/^[A-Za-z0-9]*$/, 'Password is not valid, must contain only letters and numbers.'),
  }),
  create: yup.object().shape({
    username: yup.string()
      .required('Username must be provided.')
      .min(5, 'Username must be at least ${min} characters long.')
      .max(20, 'Username must be at most ${max} characters long.'),
    password: yup.string()
      .required('Password must be provided.')
      // .matches(/(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{5,}/, 'Password is not valid, must contain at least 1 upper case letter, 1 lower case letter and 1 digit, no whitespace allowed.')
      .min(5, 'Password must be ${min} characters long or higher.')
      .matches(/^[A-Za-z0-9]*$/, 'Password is not valid, must contain only letters and numbers.'),
    confirmedPassword: yup.string()
      .required('Password must be confirmed.')
      .oneOf([yup.ref('password'), null], "Passwords must match")
      .min(5, 'Password must be ${min} characters long or higher.')
      .matches(/^[A-Za-z0-9]*$/, 'Password is not valid, must contain only letters and numbers.')
  })
}

export { accounts }