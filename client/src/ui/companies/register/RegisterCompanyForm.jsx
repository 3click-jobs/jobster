import React from 'react'
import { Form } from 'formik'
import TextField from '@material-ui/core/TextField'
import InputAdornment from '@material-ui/core/InputAdornment'
import CardActions from '@material-ui/core/CardActions'
import Button from '@material-ui/core/Button'
import Card from '@material-ui/core/Card'
import CardContent from '@material-ui/core/CardContent'
import { makeStyles } from '@material-ui/core/styles';
import CityAPIContainer from '../../cities/CityAPIContainer'

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


export const RegisterCompanyForm = ({
  handleSubmit,
  handleChange,
  handleBlur,
  values,
  touched,
  errors,
  isSubmitting, 
  handleReset,
  city
}) => {

  const classes = useStyles();

  return (
    <div className={classes.container}>
      <Form>
        <Card className={classes.card}>
          <CardContent>
            <TextField
              label="Company name"
              name="companyName"
              value={values.companyName}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.companyName && Boolean(errors.companyName))
                  ? errors.companyName
                  : ' '
              }
              error={touched.companyName && Boolean(errors.companyName)}
              margin="normal"
              fullWidth
              InputProps={
                touched.companyName
                  ? Boolean(errors.companyName)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.companyName)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="Company registration number"
              name="companyRegistrationNumber"
              value={values.companyRegistrationNumber}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.companyRegistrationNumber && Boolean(errors.companyRegistrationNumber))
                  ? errors.companyRegistrationNumber
                  : ' '
              }
              error={touched.companyRegistrationNumber && Boolean(errors.companyRegistrationNumber)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.companyRegistrationNumber
                  ? Boolean(errors.companyRegistrationNumber)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.companyRegistrationNumber)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="Mobile phone"
              name="mobilePhone"
              value={values.mobilePhone}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.mobilePhone && Boolean(errors.mobilePhone))
                  ? errors.mobilePhone
                  : ' '
              }
              error={touched.mobilePhone && Boolean(errors.mobilePhone)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.mobilePhone
                  ? Boolean(errors.mobilePhone)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.mobilePhone)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="Email"
              name="email"
              value={values.email}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.email && Boolean(errors.email))
                  ? errors.email
                  : ' '
              }
              error={touched.email && Boolean(errors.email)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.email
                  ? Boolean(errors.email)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.email)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <CityAPIContainer city={ values.city ? { name: values.city,
                                countryName: values.country,
                                city: values.city,
                                country: values.country,
                                iso2Code: values.iso2Code,
                                countryRegion: values.countryRegion,
                                longitude: values.longitude,
                                latitude: values.latitude } : null } 
                              setCity = {(props) => { if (props) {
                                values.city = props.city; 
                                values.country = props.country; 
                                values.iso2Code = props.iso2Code;
                                values.countryRegion = props.countryRegion; 
                                values.longitude = props.longitude; 
                                values.latitude = props.latitude
                              } else {
                                values.city = ""; 
                                values.country = ""; 
                                values.iso2Code = "";
                                values.countryRegion = ""; 
                                values.longitude = ""; 
                                values.latitude = ""
                              } } } />

            {/* <TextField
              label="City"
              name="city"
              value={values.city}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.city && Boolean(errors.city))
                  ? errors.city
                  : ' '
              }
              error={touched.city && Boolean(errors.city)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.city
                  ? Boolean(errors.city)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.city)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="Country"
              name="country"
              value={values.country}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.country && Boolean(errors.country))
                  ? errors.country
                  : ' '
              }
              error={touched.country && Boolean(errors.country)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.country
                  ? Boolean(errors.country)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.country)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="ISO2Code"
              name="iso2Code"
              value={values.iso2Code}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.iso2Code && Boolean(errors.iso2Code))
                  ? errors.iso2Code
                  : ' '
              }
              error={touched.iso2Code && Boolean(errors.iso2Code)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.iso2Code
                  ? Boolean(errors.iso2Code)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.iso2Code)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="Country region"
              name="countryRegion"
              value={values.countryRegion}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.countryRegion && Boolean(errors.countryRegion))
                  ? errors.countryRegion
                  : ' '
              }
              error={touched.countryRegion && Boolean(errors.countryRegion)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.countryRegion
                  ? Boolean(errors.countryRegion)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.countryRegion)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="Longitude"
              name="longitude"
              value={values.longitude}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.longitude && Boolean(errors.longitude))
                  ? errors.longitude
                  : ' '
              }
              error={touched.longitude && Boolean(errors.longitude)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.longitude
                  ? Boolean(errors.longitude)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.longitude)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="Latitude"
              name="latitude"
              value={values.latitude}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.latitude && Boolean(errors.latitude))
                  ? errors.latitude
                  : ' '
              }
              error={touched.latitude && Boolean(errors.latitude)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.latitude
                  ? Boolean(errors.latitude)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.latitude)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            /> */}

            <TextField
              label="About"
              name="about"
              value={values.about}
              onChange={handleChange}
              onBlur={handleBlur}
              multiline
              rows="4"
              helperText={
                (touched.about && Boolean(errors.about))
                  ? errors.about
                  : ' '
              }
              error={touched.about && Boolean(errors.about)}
              margin="normal"
              fullWidth={true}
              InputProps={
                touched.about
                  ? Boolean(errors.about)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.about)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />



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
          <CardActions className={classes.actions}>
            <Button color='primary' type='submit' disabled={isSubmitting}>
              SUBMIT
            </Button>
            <Button color='secondary' onClick={handleReset}>
              CLEAR
          </Button>

          </CardActions>
        </Card>
      </Form>
    </div>
  )
}

export default RegisterCompanyForm