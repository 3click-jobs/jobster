import React from 'react'
import { useFormikContext, Form } from 'formik'
import TextField from '@material-ui/core/TextField'
import InputAdornment from '@material-ui/core/InputAdornment'
import CardActions from '@material-ui/core/CardActions'
import Button from '@material-ui/core/Button'
import Card from '@material-ui/core/Card'
import CardContent from '@material-ui/core/CardContent'
import { makeStyles, createMuiTheme, ThemeProvider } from '@material-ui/core/styles';
import CityAPIContainer from '../../cities/CityAPIContainer'
import JobTypesContainer from '../../jobTypes/JobTypesContainer'
import Checkbox from '@material-ui/core/Checkbox';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import JobDayHoursContainer from '../../jobDayHours/JobDayHoursContainer';
import { green, red } from '@material-ui/core/colors';
import CheckIcon from '@material-ui/icons/Check';
import ClearIcon from '@material-ui/icons/Clear';


const theme = createMuiTheme({
  overrides: {
    MuiFormControl: {
      root: {
        verticalAlign: "middle"
      },
    },
  }
});

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


export const PostSeekForm = ({
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
    <ThemeProvider theme={theme}>
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
                      endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                    }
                  : values.detailsLink 
                    ? Boolean(errors.detailsLink)
                      ? {
                        endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                      }
                      : {
                        endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                      }
                    : null
            }
            />

            <CityAPIContainer city={ {  
                                  name: values.name, 
                                  countryName: values.countryName,
                                  city: values.city, 
                                  country: values.country,
                                  iso2Code: values.iso2Code,
                                  countryRegion: values.countryRegion,
                                  longitude: values.longitude,
                                  latitude: values.latitude,
                                } } 
                              setCity = {(props) => { if (props) {
                                  // values.city = props.city; 
                                  // values.country = props.country; 
                                  // values.iso2Code = props.iso2Code;
                                  // values.countryRegion = props.countryRegion; 
                                  // values.longitude = props.longitude; 
                                  // values.latitude = props.latitude;
                                  setFieldValue('name', props.name)
                                  setFieldValue('countryName', props.countryName)
                                  setFieldValue('city', props.city)
                                  setFieldValue('country', props.country)
                                  setFieldValue('iso2Code', props.iso2Code)
                                  setFieldValue('countryRegion', props.countryRegion)
                                  setFieldValue('longitude', props.longitude)
                                  setFieldValue('longitude', props.longitude)
                                } else {
                                  // values.city = ""; 
                                  // values.country = ""; 
                                  // values.iso2Code = "";
                                  // values.countryRegion = ""; 
                                  // values.longitude = ""; 
                                  // values.latitude = "";
                                  setFieldValue('name', "")
                                  setFieldValue('countryName', "")
                                  setFieldValue('city', '')
                                  setFieldValue('country', '')
                                  setFieldValue('iso2Code', '')
                                  setFieldValue('countryRegion', '')
                                  setFieldValue('longitude', '')
                                  setFieldValue('longitude', '')
                                } } } />

            <TextField
              label="Distance to job"
              name="distanceToJob"
              value={values.distanceToJob}
              onChange={handleChange}
              onBlur={handleBlur}
              helperText={
                (touched.distanceToJob && Boolean(errors.distanceToJob))
                  ? errors.distanceToJob
                  : ' '
              }
              error={touched.distanceToJob && Boolean(errors.distanceToJob)}
              margin="normal"
              variant="outlined"
              fullWidth={true}
              InputProps={
                touched.distanceToJob
                  ? Boolean(errors.distanceToJob)
                    ? {
                      endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                    }
                  : values.distanceToJob 
                    ? Boolean(errors.distanceToJob)
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
                      endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                    }
                  : values.price 
                    ? Boolean(errors.price)
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
                      endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                    }
                  : values.beginningDate 
                    ? Boolean(errors.beginningDate)
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
                      endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                    }
                    : {
                      endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                    }
                  : values.endDate 
                    ? Boolean(errors.endDate)
                      ? {
                        endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                      }
                      : {
                        endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                      }
                    : null
            }
            />

            <FormControl component="fieldset">
                <FormControlLabel
                  value={values.flexibileDates}
                  checked={values.flexibileDates}
                  control={<Checkbox color="primary" />}
                  label="Flexibile dates"
                  labelPlacement="bottom"
                  onChange={handleChange('flexibileDates')}
                />
            </FormControl>

            <JobDayHoursContainer listJobDayHours={values.listJobDayHoursPostDto} 
                                  handleChange={handleChange} 
                                  handleBlur={handleBlur} 
                                  touched={touched} 
                                  errors={errors} 
                                  setJobDayHoursChange={(props) => 
                                    { values.listJobDayHoursPostDto = [...props]; 
                                    } } 
                                  />

            <FormControl component="fieldset">
                <FormControlLabel
                  value={values.flexibileDays}
                  checked={values.flexibileDays}
                  control={<Checkbox color="primary" />}
                  label="Flexibile days"
                  labelPlacement="bottom"
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
    </ThemeProvider>
  )
}

export default PostSeekForm