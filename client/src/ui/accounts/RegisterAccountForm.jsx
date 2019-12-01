import React from 'react'
import { Form } from 'formik'
import TextField from '@material-ui/core/TextField'
import InputAdornment from '@material-ui/core/InputAdornment'
import CardActions from '@material-ui/core/CardActions'
import Button from '@material-ui/core/Button'
import Card from '@material-ui/core/Card'
import CardContent from '@material-ui/core/CardContent'

// !!! IMPORTANT
// YOU HAVE WORKING EXAMPLES IN PROJECTS/DESIGN/FORMS etc


export const RegisterAccountForm = ({
  handleSubmit,
  handleChange,
  handleBlur,
  values,
  touched,
  errors,
  isSubmitting
}) => {
  return (
    <Form>
      <Card>
        <CardContent>
          <TextField
            label="User name"
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
            label="Password"
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
            label="Confirm password"
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
        <CardActions>
          <Button color='primary' type='submit' disabled={isSubmitting}>
            SUBMIT
          </Button>
          <Button color='secondary'>
            CLEAR
         </Button>

        </CardActions>
      </Card>
    </Form>
  )
}
