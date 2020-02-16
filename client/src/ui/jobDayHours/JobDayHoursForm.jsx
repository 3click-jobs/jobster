import React from 'react'
import TextField from '@material-ui/core/TextField'
import InputAdornment from '@material-ui/core/InputAdornment'
import Card from '@material-ui/core/Card'
import CardContent from '@material-ui/core/CardContent'
import { makeStyles, createMuiTheme, ThemeProvider } from '@material-ui/core/styles';
import Checkbox from '@material-ui/core/Checkbox';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import { RadioGroup, Radio } from '@material-ui/core'
import CheckIcon from '@material-ui/icons/Check';
import ClearIcon from '@material-ui/icons/Clear';
import { green, red } from '@material-ui/core/colors';
import { useTranslation } from 'react-i18next'


const theme = createMuiTheme({
  overrides: {
    MuiFormControl: {
      root: {
        verticalAlign: "middle",
        // alignSelf: 'center',
      },
    },
    MuiFormControlLabel: {
      root: {
        verticalAlign: "middle",
        // alignSelf: 'center',
      },
    },
  },
});

const useStyles = makeStyles(() => ({
  card: {
    maxWidth: 680,
    marginTop: 3,
  },
  container: {
    display: "Flex",
    justifyContent: "center"
  },
  actions: {
    float: "right"
  }
}))


export const JobDayHoursForm = ({
  handleChange,
  handleBlur,
  jobDayHours,
  touched,
  errors,
  handleJobDayHoursChange
}) => {

  const classes = useStyles();

  const { t } = useTranslation();

  return (
    <ThemeProvider theme={theme}>
    <Card className={classes.card}>
      <CardContent>
    {/* <div> */}
        <FormControl component="fieldset">
            <FormControlLabel
              name="day"
              value={jobDayHours.day}
              checked={jobDayHours.chck}
              control={<Checkbox color="primary" 
                                onChange={(event) => {
                                  event.persist()
                                  handleJobDayHoursChange(event, jobDayHours.day);
                                }} />}
              label={jobDayHours.day ? 
                jobDayHours.day.substr(4) === "MONDAY" ? 
                  `${t('day.monday')}` 
                  : jobDayHours.day.substr(4) === "TUESDAY" ?
                    `${t('day.tuesday')}`
                    : jobDayHours.day.substr(4) === "WEDNESDAY" ?
                      `${t('day.wednesday')}`
                      : jobDayHours.day.substr(4) === "THURSDAY" ?
                        `${t('day.thursday')}`
                        : jobDayHours.day.substr(4) === "FRIDAY" ?
                          `${t('day.friday')}`
                          : jobDayHours.day.substr(4) === "SATURDAY" ?
                            `${t('day.saturday')}`
                            : jobDayHours.day.substr(4) === "SUNDAY" ?
                              `${t('day.sunday')}`
                              : null
                : null}
              // label={jobDayHours.day ? jobDayHours.day.substr(4) : null}
              labelPlacement="end"
              // onChange={handleChange('day')}
              style = {{width: 100}}
            />
        </FormControl>

        {jobDayHours.chck ? 
          <React.Fragment >

            <FormControl component="fieldset" >
              {/* <FormLabel component="legend">Chose one:</FormLabel> */}
              <RadioGroup 
                aria-label="isMinMax" 
                name="isMinMax"
                value={jobDayHours.isMinMax} 
                // checked={Boolean(jobDayHours.isMinMax)}
                // onChange={handleChange} 
                onChange={(event) => {
                  event.persist()
                  handleJobDayHoursChange(event, jobDayHours.day);
                }}
                style = {{ marginLeft: 30, marginRight: 20 }}
              >
                <FormControlLabel 
                  value={false} 
                  control={<Radio 
                  checked={jobDayHours.isMinMax === false} />} 
                  label={t('jobDayHoursForm.fromToOClock')}
                  // label="From-to o'clock" 
                  labelPlacement="end" 
                />
                <FormControlLabel 
                  value={true} 
                  control={<Radio 
                  checked={jobDayHours.isMinMax === true} />} 
                  label={t('jobDayHoursForm.minMaxHours')}
                  // label="Min-max hours" 
                  labelPlacement="end"
                />
              </RadioGroup>
            </FormControl>

            {/* <div style = {{display: "inline"}} > */}
            <FormControl component="fieldset" style = {{display: "inline"}} >
              <TextField
                label={jobDayHours.isMinMax ? 
                  `${t('jobDayHoursForm.minADay')}` : `${t('jobDayHoursForm.from')}`
                  // "Min a day" : "From" 
                  }
                name="fromHour"
                type="number"
                value={jobDayHours.fromHour}
                // onChange={handleChange}
                onChange={(event) => {
                  event.persist()
                  handleJobDayHoursChange(event, jobDayHours.day); 
                  }}
                required 
                onBlur={handleBlur}
                helperText={
                  (touched && touched.fromHour && Boolean(errors.fromHour))
                    ? errors.fromHour
                    : ' '
                }
                error={touched && touched.fromHour && Boolean(errors.fromHour)}
                margin="normal"
                variant="outlined"
                // fullWidth={true}
                InputProps={
                  (touched && touched.fromHour)
                    ? Boolean(errors.fromHour)
                      ? {
                        endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                      }
                      : {
                        endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                      }
                    : jobDayHours.fromHour 
                      ? Boolean(errors.fromHour)
                        ? {
                          endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                        }
                        : {
                          endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                        }
                      : null
                }
                style = {{width: 100 }}
              />

              <TextField
                label={jobDayHours.isMinMax ? 
                  `${t('jobDayHoursForm.maxADay')}` : `${t('jobDayHoursForm.to')}`
                  // "Max a day" : "To" 
                  }
                name="toHour"
                type="number"
                value={jobDayHours.toHour}
                // onChange={handleChange}
                onChange={(event) => {
                  event.persist()
                  handleJobDayHoursChange(event, jobDayHours.day);
                }}
                required 
                onBlur={handleBlur}
                helperText={
                  (touched && touched.toHour && Boolean(errors.toHour))
                    ? errors.toHour
                    : ' '
                }
                error={touched && touched.toHour && Boolean(errors.toHour)}
                margin="normal"
                variant="outlined"
                // fullWidth={true}
                InputProps={
                  (touched && touched.toHour)
                    ? Boolean(errors.toHour)
                      ? {
                        endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                      }
                      : {
                        endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                      }
                    : jobDayHours.toHour
                      ? Boolean(errors.toHour)
                        ? {
                          endAdornment: <InputAdornment position='end'><ClearIcon style={{ color: red[600] }} /></InputAdornment>
                        }
                        : {
                          endAdornment: <InputAdornment position='end'><CheckIcon style={{ color: green[600] }} /></InputAdornment>
                        }
                      : null
                }
                style = {{width: 100, marginLeft: 5 }}
              />
              </FormControl>

              <div style = {{width: "100%", justifyContent: "right" }} >
                <FormControl component="fieldset">
                  <FormControlLabel
                    name="flexibileHours"
                    value={jobDayHours.flexibileHours}
                    checked={jobDayHours.flexibileHours}
                    control={<Checkbox color="primary" 
                                      onChange={(event) => {
                                        event.persist()
                                        handleJobDayHoursChange(event, jobDayHours.day);
                                      }} />}
                    label={t('jobDayHoursForm.flexibileHours')}
                    // label="Flexibile hours"
                    labelPlacement="bottom"
                    onChange={handleChange('flexibileHours')}
                  />
                </FormControl>
              </div>
            {/* </div> */}
          </React.Fragment>
        : null}
    {/* </div> */}
      </CardContent>
    </Card>
    </ThemeProvider>
  )
}

export default JobDayHoursForm