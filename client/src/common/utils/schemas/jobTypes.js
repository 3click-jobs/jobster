import * as Yup from 'yup'

const jobTypes = {
    create: Yup.object().shape({
        jobTypeName: yup.string()
          .nullable()
          .matches(/^[_A-Za-z0-9-]{2,}$/, 'Company name is not valid.')
          .required('Job type name must be provided.')
      })
    }

export { jobTypes }