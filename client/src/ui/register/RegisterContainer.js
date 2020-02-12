import React from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import { makeStyles } from '@material-ui/core/styles';
import RegisterPerson from '../persons/register/RegisterPerson';
import RegisterCompany from '../companies/register/RegisterCompany';
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


export const RegisterContainer = (props) => {

  const [activeTab, setActiveTab] = React.useState((props.profile && Object.keys(props.profile).includes("companyName")) ? 1 : 0)

  const classes = useStyles();
  const { t } = useTranslation();

  const handleTabChange = (event, newActiveTab) => {
    setActiveTab(newActiveTab)
  }

  const loggedUser = React.useMemo(() => {
    console.log('profile: ', {...props.profile})
    return {...props.profile}
  }, [props.profile])
  
//
// The code below needs refactoring, I guess..
//

  // const useComponentDidMount = func => React.useEffect(func, []);

  // const useComponentWillMount = func => {
  //   const willMount = React.useRef(true);
  
  //   if (willMount.current) {
  //     func();
  //   }
  
  //   useComponentDidMount(() => {
  //     willMount.current = false;
  //   });
  // };

  // let loggedUser = null

  // useComponentWillMount(() => {
  //   if (props.profile) {
  //     loggedUser = {...props.profile}
  //     // if (Object.keys(props.profile).includes("companyName")) {
  //     //   setActiveTab(1)
  //     // } 
  //   }
  // })

  
  return (
    <div>
      {!props.profile ? 
        <Tabs
            value={activeTab}
            onChange={handleTabChange}
            indicatorColor='primary'
            textColor='primary'
            variant='fullWidth'
        >
              <Tab 
                label={t('registration.person')}
                // label='Person' 
              />
              <Tab 
                label={t('registration.company')}
                // label='Company' 
              />
          {/* { (props.profile && Object.keys(props.profile).includes("companyName")) ? <Tab label='Company' /> : null}
          { (props.profile && Object.keys(props.profile).includes("firstName")) ? <Tab label='Person' /> : null} */}
        </Tabs>
      : null }
        <TabPanel value={activeTab} index={0}>
        <div className={classes.tabContainer}>
            <div className={classes.tabPaper}>
            <RegisterPerson profile={loggedUser} />
            </div>
        </div>
        </TabPanel>
        <TabPanel value={activeTab} index={1}>
        <div className={classes.tabContainer}>
            <div className={classes.tabPaper}>
            <RegisterCompany profile={loggedUser} />
            </div>
        </div>
        </TabPanel>            
    </div>
  )
}

export default RegisterContainer