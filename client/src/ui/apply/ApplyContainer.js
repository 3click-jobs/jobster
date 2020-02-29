import React from 'react';
import { connect } from 'react-redux'
// import TextField from '@material-ui/core/TextField';
// import Autocomplete from '@material-ui/lab/Autocomplete';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
// import CircularProgress from '@material-ui/core/CircularProgress';
import Button from '@material-ui/core/Button';
import Icon from '@material-ui/core/Icon';
import { makeStyles } from '@material-ui/core/styles';
// import { Grid } from "@material-ui/core";
import { actions as jobTypesActions } from '../../redux/actions/jobTypes'
import { jobTypesSelectors } from '../../redux/selectors/jobTypes'
// Import Search Bar Components
//import SearchBar from 'material-ui-search-bar';
// Import React Scrit Libraray to load Google object
//import Script from 'react-load-script';
// import Geonames from 'geonames.js';
import CityAPIContainer from '../cities/CityAPIContainer';
//import utf8 from 'utf8'
import OffersContainer from '../offers/browse/OffersContainer';
import JobTypesContainer from '../jobTypes/JobTypesContainer';
import TextField from '@material-ui/core/TextField'
import { useTranslation } from 'react-i18next'

import PropTypes from 'prop-types'

const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1),
    float: 'right',
  },
  buttonLeft: {
    margin: theme.spacing(1),
    float: 'left',
  },
  tabPaper: {
    maxWidth: 720,
    width: '100%',
    marginTop: 10
  },
  tabContainer: {
    display: "flex",
    justifyContent: 'center',
    alignItems: 'center',
  },
}));

// const cities = [
//   { name: 'Temerin' },
//   { name: 'Novi Sad' },
//   { name: 'Belgrade' },
//   { name: 'Subotica' },
//   { name: 'Nis' }
// ]

// const jobTypes = [
//   { name: 'Keramicar' },
//   { name: 'Zidar' },
//   { name: 'Tesar' },
//   { name: 'Mesar' },
//   { name: 'Vozac' }
// ]

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <Typography
      component="div"
      role="tabpanel"
      hidden={value !== index}
      id={`scrollable-auto-tabpanel-${index}`}
      aria-labelledby={`scrollable-auto-tab-${index}`}
      {...other}
    >
      {value === index && <Box p={3}>{children}</Box>}
    </Typography>
  );
}

// TabPanel.propTypes = {
//   children: PropTypes.node,
//   index: PropTypes.any.isRequired,
//   value: PropTypes.any.isRequired,
// };

// function a11yProps(index) {
//   return {
//     id: `scrollable-auto-tab-${index}`,
//     'aria-controls': `scrollable-auto-tabpanel-${index}`,
//   };
// }

/**
 * Main container component for all kinds of application.
 * Using the TabPanel and calling CityAPIContainer, JobTypesContainer and OffersContainer inside the panels.
 */
export const ApplyContainer = ({
  jobTypesAll,
  jobTypesIsLoading,
  jobTypesIsError,
  loadJobTypesAll
}) => {

  const [city, setCity] = React.useState(null)
  const [distance, setDistance] = React.useState(0)
  const [jobType, setJobType] = React.useState(null)
  const [activeTab, setActiveTab] = React.useState(0)
  // const [cityAPI, setCityAPI] = React.useState(null)
  // const [queryAPI, setQueryAPI] = React.useState([])
  const classes = useStyles();
  // let loading = false
  const { t } = useTranslation();

  React.useEffect(() => {
    if (jobTypesAll.length === 0)
      loadJobTypesAll()
  }, [loadJobTypesAll, jobTypesAll])


  const handleTabChange = (event, newActiveTab) => {
    setActiveTab(newActiveTab)
  }


  // const handleCitiesLoad = () => {
  //   // Declare Options For Autocomplete
  //   const options = {
  //     types: ['(cities)'],
  //   };

  //   // Initialize Google Autocomplete
  //   /*global google*/ // To disable any eslint 'google not defined' errors
  //   let autocomplete = new google.maps.places.Autocomplete(
  //     document.getElementById('autocomplete'),
  //     options,
  //   );

  //   // Avoid paying for data that you don't need by restricting the set of
  //   // place fields that are returned to just the address components and formatted
  //   // address.
  //   autocomplete.setFields(['address_components', 'formatted_address']);

  //   // Fire Event when a suggested name is selected
  //   autocomplete.addListener('place_changed', handleCItySelect);
  // }
  
  // const handleCItySelect = () => {

  //   // Extract City From Address Object
  //   const addressObject = this.autocomplete.getPlace();
  //   const address = addressObject.address_components;

  //   // Check if address is valid
  //   if (address) {
  //     // Set State
  //     setCityAPI(address[0].long_name);
  //     setQueryAPI(addressObject.formatted_address);
  //     console.log(address);
  //     console.log(addressObject);
  //   }
  // }


  // const geonames = new Geonames({
  //   username: 'drax82',
  //   lan: 'en',
  //   encoding: 'JSON'
  // });

  // const handleCitiesLoad = (event) => {
  //   if (!event.target.value || event.target.value === "" || event.target.value === " " ) {
  //     setJobType(null) 
  //     setCity(null);
  //   } else {
  //     loading = true;
  //     if (!loading) {
  //       return undefined;
  //     }
  //     let utf8Name = encodeURIComponent(event.target.value).replace('%20',' ');
  //     geonames.search({q: utf8Name, maxRows: '10', style: 'FULL', featureClass: 'P', lang: 'en' }) //get cities
  //       .then(resp => {
  //         setQueryAPI(resp.geonames);
  //         loading = false;
  //       })
  //       .catch(err => console.error(err));
  //   }
  // }
  
  // const handleCItySelect = (newValue) => {
  //     if (newValue && newValue !== "" && newValue !== " ") {
  //       setCity({name: newValue.name, latitude: newValue.lat, longitude: newValue.lng, countryRegion: newValue.adminName1, countryName: newValue.countryName, countryCode: newValue.countryCode}) 
  //     } else {
  //       setJobType(null) 
  //       setCity(null);
  //     }
  //     setQueryAPI([]);
  // }


  return (
    <React.Fragment>
      { 
        (!jobTypesIsLoading && jobTypesAll) &&
        <div>
          {/* Apply for a job in three clicks.
          (1) Select a city
          (2) Select a job
          (3) View details & apply */}
          {/* <Grid
            container
            spacing={0}
            justify="center"
            style={{ margin: '10px auto', minHeight: '15vh', maxWidth: '100%' }}
            >
            <Grid item xs={4} align="center">
              <img alt="Where" style={{padding: '.1em', width: '90%', maxWidth: '16vh', height: 'auto', maxHeight: '18vh'}} src={!(activeTab===0) ? '/img/animated-where.gif' : '/img/where.gif'} />
            </Grid>
            <Grid item xs={4} align="center">
              <img alt="What" style={{padding: '.1em', width: '90%', maxWidth: '16vh', height: 'auto', maxHeight: '18vh'}} src={!(activeTab===1) ? '/img/animated-what.gif' : '/img/what.gif'} />
            </Grid>
            <Grid item xs={4} align="center">
              <img alt="Apply" style={{padding: '.1em', width: '90%', maxWidth: '16vh', height: 'auto', maxHeight: '18vh'}} src={!(activeTab===2) ? '/img/animated-apply.gif' : '/img/apply.gif'} />
            </Grid>
          </Grid> */}
          <div>
            <Tabs
              value={activeTab}
              onChange={handleTabChange}
              indicatorColor='primary'
              textColor='primary'
              variant='fullWidth'
            >
              <Tab 
                label={t('applyOrEmploy.labelWhere')}
                // label='Where' 
              />
              {city ? 
                <Tab 
                  label={t('applyOrEmploy.labelWhat')}
                  // label='What'
                /> : <Tab 
                  label={t('applyOrEmploy.labelWhat')}
                  // label='What' 
                  disabled
                />}
              {city && jobType ? 
                <Tab 
                  label={t('applyOrEmploy.labelApply')}
                  // label='Apply'
                /> : <Tab 
                  label={t('applyOrEmploy.labelApply')}
                  // label='Apply' 
                  disabled
                />}
            </Tabs>
              <TabPanel value={activeTab} index={0}>
                {/* Item One -> Render City selection component. */}
                {/* <Autocomplete
                  id="combo-box-demo"
                  options={cities}
                  getOptionLabel={option => option.name}
                  style={{ width: 300 }}
                  value={city}
                  autoComplete
                  onChange={(event, newValue) => {
                    setCity(newValue); 
                    if (!city) { setJobType(null) };
                  }}
                  renderInput={params => (
                    <TextField {...params} label="City" variant="outlined" fullWidth />
                  )}
                /> */}

                {/* <Script
                    url="https://maps.googleapis.com/maps/api/js?key=YOUR-KEY&libraries=places"
                    onLoad={handleCitiesLoad}
                /> */}
                {/* <SearchBar id="autocomplete" placeholder="" hintText="Search City" value={queryAPI}
                  style={{
                    margin: '0 auto',
                    maxWidth: 800,
                  }}
                /> */}

                {/* <Autocomplete
                  id="autocomplete"
                  options={queryAPI}
                  getOptionLabel={option => option.name + ", " + option.countryName}
                  style={{ minWidth: 270 }}
                  clearOnEscape
                  disableClearable
                  value={city}
                  loading={loading}
                  autoComplete
                  onChange={(event, newValue) => {
                    handleCItySelect(newValue); 
                  }}
                  renderInput={params => {
                    // const inputProps = params.inputProps;
                    // inputProps.getOptionSelected= (option, value) => {
                    //   if(value)
                    //     return option.lng === value.lng && option.lat === value.lat;
                    //   return false
                    // };
                    return (
                      <TextField 
                        {...params} 
                        label= "City"
                        variant="outlined" 
                        required
                        fullWidth 
                        onChange={(event) => {
                          handleCitiesLoad(event); 
                        }}
                        InputProps={{
                          ...params.InputProps,
                          autoComplete: "false",
                          endAdornment: (
                            <React.Fragment>
                              {loading ? <CircularProgress color="inherit" size={20} /> : null}
                              {params.InputProps.endAdornment}
                            </React.Fragment>
                          ),
                        }}
                      />
                    )
                  }}
                /> */}
                <div className={classes.tabContainer}>
                  <div className={classes.tabPaper}>
                    <CityAPIContainer city={city} setCity = {setCity} />
                    <TextField
                      label={t('applyOrEmploy.acceptableDistance')}
                      // label="Acceptable distance from selected city"
                      name="distanceToJob"
                      type="number"
                      value={distance}
                      onChange={(event) => {
                        if (event.target.value < 0 || event.target.value === null || event.target.value === "" ) {
                          setDistance("0");
                        } else {
                            setDistance(event.target.value)
                          }
                      }}
                      margin="normal"
                      variant="outlined"
                      fullWidth={true}
                    />
                    <div>
                      <Button
                        disabled={ !city }
                        variant="contained"
                        color="primary"
                        className={classes.button}
                        endIcon={<Icon>arrow_forward_ios</Icon>} 
                        onClick={() => { setActiveTab(1); }}
                      >
                        {t('applyOrEmploy.buttonNext')}
                        {/* Next */}
                      </Button>
                    </div>
                  </div>
                </div>
              </TabPanel>
              <TabPanel value={activeTab} index={1}>
                {/* Item Two -> Render Job selection component. (disabled until city is chosen). */}
                <div className={classes.tabContainer}>
                  <div className={classes.tabPaper}>
                    {/* <Autocomplete
                      id="combo-box"
                      options={jobTypesAll}
                      getOptionLabel={option => option.jobTypeName}
                      style={{ minWidth: 270 }}
                      value={jobType}
                      clearOnEscape
                      disableClearable
                      autoComplete
                      onChange={(event, newValue) => {
                        setJobType(newValue);
                      }}
                      renderInput={params => (
                        <TextField {...params} 
                          label="Job type" 
                          variant="outlined" 
                          required 
                          fullWidth 
                          onChange={(event) => {
                            if (!event || event.target.value === "" || event.target.value === "")
                              setJobType(null);
                          }}
                        />
                      )}
                    /> */}
                    <JobTypesContainer selectedJobType={ jobType } handleJobType={ (jobType)=> { setJobType(jobType); } } />
                    <div>
                      <Button
                        variant="contained"
                        color="primary"
                        className={classes.buttonLeft}
                        startIcon={<Icon>arrow_back_ios</Icon>} 
                        onClick={() => { setActiveTab(0); }}
                      >
                        {t('applyOrEmploy.buttonBack')}
                        {/* Back */}
                      </Button>
                      <Button
                        disabled={ !jobType }
                        variant="contained"
                        color="primary"
                        className={classes.button}
                        endIcon={<Icon>arrow_forward_ios</Icon>} 
                        onClick={() => { setActiveTab(2); }}
                      >
                        {t('applyOrEmploy.buttonNext')}
                        {/* Next */}
                      </Button>
                    </div>
                  </div>
                </div>
              </TabPanel>
              <TabPanel value={activeTab} index={2}>
                {/* Item Three -> Render Job details & apply component. (disabled until city & job are chosen). */}
                <OffersContainer distance={distance} selectedCity={city} selectedJobType={jobType} />
                <div className={classes.tabContainer}>
                  <div className={classes.tabPaper}>
                    <div>
                      <Button
                        variant="contained"
                        color="primary"
                        className={classes.buttonLeft}
                        startIcon={<Icon>arrow_back_ios</Icon>} 
                        onClick={() => { setActiveTab(1); }}
                      >
                        {t('applyOrEmploy.buttonBack')}
                        {/* Back */}
                      </Button>
                      {/* <Button
                        variant="contained"
                        color="primary"
                        className={classes.button}
                        endIcon={<Icon>send</Icon>} 
                        onClick={() => { console.log("APPLY")}}
                      >
                        Apply
                      </Button> */}
                    </div>
                  </div>
                </div>
              </TabPanel>
            
          </div>
        </div>
      }
      {
        (!jobTypesIsLoading && jobTypesIsError) &&
        <p>
          {t('applyOrEmploy.errorAtLoading')}
          {/* Error at loading resources. */}
        </p>
      }
      {
        jobTypesIsLoading &&
        <p>
          {t('applyOrEmploy.resourcesAreLoading')}
          {/* Resources are loading ...  */}
        </p>
      }
    </React.Fragment>
  )
}

const mapStateToProps = (state) => {
  return {
    jobTypesAll: jobTypesSelectors.getAll(state),
    jobTypesIsLoading: jobTypesSelectors.isFetching(state),
    jobTypesIsError: jobTypesSelectors.error(state)
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    loadJobTypesAll: () => dispatch(jobTypesActions.fetchAll())
  }
}

ApplyContainer.propTypes = {
  /**
   * An array containing the job types.
   */
  jobTypesAll: PropTypes.array,
  /**
   * If the job types is loading the flag is true.
   */
  jobTypesIsLoading: PropTypes.bool,
  /**
   * If there was an error loading the job types the flag will be true.
   */
  jobTypesIsError: PropTypes.bool,
  /**
   * Callback to load the job types.
   */
  loadJobTypesAll: PropTypes.func
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ApplyContainer)