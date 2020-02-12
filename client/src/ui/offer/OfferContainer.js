import React from 'react';
import { connect } from 'react-redux'
import { actions as jobTypesActions } from '../../redux/actions/jobTypes'
import { jobTypesSelectors } from '../../redux/selectors/jobTypes'
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
// import Geonames from 'geonames.js';
import CityAPIContainer from '../cities/CityAPIContainer';
import PostOffer from '../offers/post/PostOffer';
import JobTypesContainer from '../jobTypes/JobTypesContainer';
import { useTranslation } from 'react-i18next'


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


export const OfferContainer = ({
  jobTypesAll,
  jobTypesIsLoading,
  jobTypesIsError,
  loadJobTypesAll,
  profile
}) => {

  const [city, setCity] = React.useState(null)
  const [jobType, setJobType] = React.useState(null)
  const [activeTab, setActiveTab] = React.useState(0)
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
          {/* Post a job in three clicks.
          (1) Select a city
          (2) Describe the job
          (3) Confirm details & post */}
          {/* <Grid
            container
            spacing={0}
            justify="center"
            style={{ margin: '10px auto', minHeight: '15vh', maxWidth: '100%' }}
            >
            <Grid item xs={4} align="center">
              <img alt="Where" style={{padding: '.1em', width: '90%', maxWidth: '16vh', height: 'auto', maxHeight: '18vh'}} src={!(activeTab===0) ? null : '/img/where.gif'} />
            </Grid>
            <Grid item xs={4} align="center">
              <img alt="What" style={{padding: '.1em', width: '90%', maxWidth: '16vh', height: 'auto', maxHeight: '18vh'}} src={!(activeTab===1) ? null : '/img/what.gif'} />
            </Grid>
            <Grid item xs={4} align="center">
              <img alt="Offer" style={{padding: '.1em', width: '90%', maxWidth: '16vh', height: 'auto', maxHeight: '18vh'}} src={!(activeTab===2) ? null : '/img/offer.gif'} />
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
                label={t('applyOrEmploy.labelOffer')}
                // label='Offer'
              /> : <Tab 
                label={t('applyOrEmploy.labelOffer')}
                // label='Offer' 
                disabled
              />}
          </Tabs>
          <TabPanel value={activeTab} index={0}>
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
            <div className={classes.tabContainer}>
              <div className={classes.tabPaper}>
                {/* 2. Describe the job */}
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
            <div className={classes.tabContainer}>
              <div className={classes.tabPaper}>
                {/* Item Three -> Confirm details & post. (disabled until city & job are chosen). */}
                <PostOffer employer={profile} city={city} jobType={jobType} setCity = {setCity} handleJobType={ (jobType)=> { setJobType(jobType); } } />
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
                    onClick={() => { console.log("OFFER")}}
                  >
                    Offer
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

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OfferContainer)