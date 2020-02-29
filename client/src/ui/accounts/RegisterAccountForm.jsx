import React from 'react'
import { Form } from 'formik'
import TextField from '@material-ui/core/TextField'
import InputAdornment from '@material-ui/core/InputAdornment'
import CardActions from '@material-ui/core/CardActions'
import Button from '@material-ui/core/Button'
import Card from '@material-ui/core/Card'
import CardContent from '@material-ui/core/CardContent'
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next'

import PropTypes from 'prop-types'

// !!! IMPORTANT
// YOU HAVE WORKING EXAMPLES IN PROJECTS/DESIGN/FORMS etc
const useStyles = makeStyles(() => ({
  card: {
    maxWidth: 720,
    marginTop: 10
  },
  container: {
    display: "Flex",
    justifyContent: "center"
  },
  actions: {
    float: "right"
  }
}))

/**
 *  Account registration component. Used by the container component RegisterAccount.
 */
export const RegisterAccountForm = ({
  handleSubmit,
  handleChange,
  handleBlur,
  values,
  touched,
  errors,
  isSubmitting
}) => {

  const classes = useStyles();

  const { t } = useTranslation();

  return (
    <div className={classes.container}>
      <Form>
        <Card className={classes.card}>
          <CardContent>
            <TextField
              label={t('profile.labelUsername')}
              // label="User name"
              name="username"
              value={values.username}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.username && Boolean(errors.username))
                  ? errors.username
                  : ' '
              }
              error={touched.username && Boolean(errors.username)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.username
                  ? Boolean(errors.username)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.username)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label={t('profile.labelPassword')}
              // label="Password"
              name="password"
              value={values.password}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.password && Boolean(errors.password))
                  ? errors.password
                  : ' '
              }
              error={touched.password && Boolean(errors.password)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.password
                  ? Boolean(errors.password)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.password)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label={t('profile.labelConfirmPassword')}
              // label="Confirm password"
              name="confirmedPassword"
              value={values.confirmedPassword}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.confirmedPassword && Boolean(errors.confirmedPassword))
                  ? errors.confirmedPassword
                  : ' '
              }
              error={touched.confirmedPassword && Boolean(errors.confirmedPassword)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.confirmedPassword
                  ? Boolean(errors.confirmedPassword)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.confirmedPassword)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

          </CardContent>
          <CardActions className={classes.actions}>
            <Button color='primary' type='submit' disabled={isSubmitting}>
              {t('registration.buttonSubmit')}
              {/* SUBMIT */}
            </Button>
            <Button color='secondary'>
              {t('registration.buttonClear')}
              {/* CLEAR */}
          </Button>

          </CardActions>
        </Card>
      </Form>
    </div>
  )
}

RegisterAccountForm.propTypes = {
  /**
   * Submit handler callback, provided by Formik.
   */
  handleSubmit: PropTypes.func,
  /**
   * Input value change handler callback, provided by Formik.
   */
  handleChange: PropTypes.func,
  /**
   * Blur (element has lost focus) handler, provided by Formik.
   */
  handleBlur: PropTypes.func,
  /**
  * Container object holding values managed by Formik.
  */
  values: PropTypes.object,
  /**
   * Container object holding data about what inputs are touched.
  */
  touched: PropTypes.object,
  /**
   * Callback function for account creation.
  */
  errors: PropTypes.object,
  /**
   * If the form is being submitted the flag will be true.
  */
  isSubmitting: PropTypes.bool
}
