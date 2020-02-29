import React from 'react'
import { useFormikContext, Form } from 'formik'
import TextField from '@material-ui/core/TextField'
import InputAdornment from '@material-ui/core/InputAdornment'
import CardActions from '@material-ui/core/CardActions'
import Button from '@material-ui/core/Button'
import Card from '@material-ui/core/Card'
import CardContent from '@material-ui/core/CardContent'
import { makeStyles } from '@material-ui/core/styles';
import { green, red } from '@material-ui/core/colors';
import CheckIcon from '@material-ui/icons/Check';
import ClearIcon from '@material-ui/icons/Clear';
import IconButton from '@material-ui/core/IconButton';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';

import PropTypes from 'prop-types'
import { useTranslation } from 'react-i18next'


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
 * Login form component, used by the Login component.
 */
export const LoginForm = ({
  handleSubmit,
  handleChange,
  handleBlur,
  values,
  touched,
  errors,
  isSubmitting
}) => {

  const { setFieldValue } = useFormikContext()

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
                      endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                    }
                  : values.username 
                    ? Boolean(errors.username)
                      ? {
                        endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                      }
                      : {
                        endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                      }
                    : null
              }
            />

            <TextField
              label={t('profile.labelPassword')}
              // label="Password"
              name="password"
              type={values.showPassword ? 'text' : 'password'}
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
                { endAdornment: 
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={() => setFieldValue('showPassword', !values.showPassword)}
                    >
                      {values.showPassword ? <Visibility /> : <VisibilityOff />}
                    </IconButton>
                    {touched.password
                      ? Boolean(errors.password)
                        ?  <ClearIcon style={{ color: red[600] }} />
                        : <CheckIcon style={{ color: green[600] }} />
                      : values.password 
                        ? Boolean(errors.password)
                          ? <ClearIcon style={{ color: red[600] }} />
                          : <CheckIcon style={{ color: green[600] }} />
                        : null}
                  </InputAdornment>
                }
              }
            />

          </CardContent>
          <CardActions className={classes.actions}>
            <Button color='primary' type='submit' disabled={isSubmitting}>
              {t('login.button')}
              {/* Log In */}
            </Button>
            {/* <Button color='secondary'>
              CLEAR
          </Button> */}

          </CardActions>
        </Card>
      </Form>
    </div>
  )
}

LoginForm.propTypes = {
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
   * Container object holding data about validation etc. errors.
   */
  errors: PropTypes.object,
  /**
   * If the form is being submitted the flag will be true.
   */
  isSubmitting: PropTypes.bool
}