import React from 'react'
import { useFormikContext, Form } from 'formik'
import TextField from '@material-ui/core/TextField'
import InputAdornment from '@material-ui/core/InputAdornment'
import CardActions from '@material-ui/core/CardActions'
import Button from '@material-ui/core/Button'
import Card from '@material-ui/core/Card'
import CardContent from '@material-ui/core/CardContent'
import { makeStyles } from '@material-ui/core/styles';
import CityAPIContainer from '../../cities/CityAPIContainer'
import JobTypesContainer from '../../jobTypes/JobTypesContainer'
import Checkbox from '@material-ui/core/Checkbox';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import JobDayHoursContainer from '../../jobDayHours/JobDayHoursContainer';


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


export const PostOfferForm = ({
  handleSubmit,
  handleChange,
  handleBlur,
  values,
  touched,
  errors,
  isSubmitting, 
  handleReset,
}) => {

  const { setFieldValue } = useFormikContext()

  const classes = useStyles();


  return (
    <div className={classes.container}>
      <Form>
        <Card className={classes.card}>
          <CardContent>
            <JobTypesContainer selectedJobType={values.jobType} handleJobType={ (jobType)=> { if (jobType) { setFieldValue('jobType.id', jobType.id); setFieldValue('jobType.jobTypeName', jobType.jobTypeName); } else { setFieldValue('jobType.id', ''); setFieldValue('jobType.jobTypeName', ''); } } } />

            <TextField
              label="Details about job"
              name="detailsLink"
              value={values.detailsLink}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.detailsLink && Boolean(errors.detailsLink))
                  ? errors.detailsLink
                  : ' '
              }
              error={touched.detailsLink && Boolean(errors.detailsLink)}
              margin="normal"
              variant="outlined"
              fullWidth
              InputProps={
                touched.detailsLink
                  ? Boolean(errors.detailsLink)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.detailsLink)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <CityAPIContainer city={ { 
                                  name: values.city, 
                                  countryName: values.country,
                                  city: values.city, 
                                  country: values.country,
                                  iso2Code: values.iso2Code,
                                  countryRegion: values.countryRegion,
                                  longitude: values.longitude,
                                  latitude: values.latitude,
                                } } 
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

            <TextField
              label="Number of employees"
              name="numberOfEmployees"
              value={values.numberOfEmployees}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.numberOfEmployees && Boolean(errors.numberOfEmployees))
                  ? errors.numberOfEmployees
                  : ' '
              }
              error={touched.numberOfEmployees && Boolean(errors.numberOfEmployees)}
              margin="normal"
              variant="outlined"
              fullWidth={true}
              InputProps={
                touched.numberOfEmployees
                  ? Boolean(errors.numberOfEmployees)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.numberOfEmployees)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="Price per hour"
              name="price"
              value={values.price}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.price && Boolean(errors.price))
                  ? errors.price
                  : ' '
              }
              error={touched.price && Boolean(errors.price)}
              margin="normal"
              variant="outlined"
              fullWidth={true}
              InputProps={
                touched.price
                  ? Boolean(errors.price)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.price)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="Beginning date"
              name="beginningDate"
              type="date"
              value={values.beginningDate}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.beginningDate && Boolean(errors.beginningDate))
                  ? errors.beginningDate
                  : ' '
              }
              error={touched.beginningDate && Boolean(errors.beginningDate)}
              margin="normal"
              variant="outlined"
              fullWidth={true}
              InputProps={
                touched.beginningDate
                  ? Boolean(errors.beginningDate)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.beginningDate)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <TextField
              label="End date"
              name="endDate"
              type="date"
              value={values.endDate}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.endDate && Boolean(errors.endDate))
                  ? errors.endDate
                  : ' '
              }
              error={touched.endDate && Boolean(errors.endDate)}
              margin="normal"
              variant="outlined"
              fullWidth={true}
              InputProps={
                touched.endDate
                  ? Boolean(errors.endDate)
                    ? {
                      endAdornment: <InputAdornment position='end'>Touched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Touched and no errors</InputAdornment>
                    }
                  : Boolean(errors.endDate)
                    ? {
                      endAdornment: <InputAdornment position='end'>Untouched with errors</InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'>Untouched without errors</InputAdornment>
                    }
              }
            />

            <FormControl component="fieldset">
                <FormControlLabel
                  value={values.flexibileDates}
                  checked={values.flexibileDates}
                  control={<Checkbox color="primary" />}
                  label="Flexibile dates"
                  labelPlacement="end"
                  onChange={handleChange('flexibileDates')}
                />
            </FormControl>

            <JobDayHoursContainer />

            <FormControl component="fieldset">
                <FormControlLabel
                  value={values.flexibileDays}
                  checked={values.flexibileDays}
                  control={<Checkbox color="primary" />}
                  label="Flexibile days"
                  labelPlacement="end"
                  onChange={handleChange('flexibileDays')}
                />
            </FormControl>

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

export default PostOfferForm