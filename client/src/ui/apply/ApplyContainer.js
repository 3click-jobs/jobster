import React from 'react'
import TextField from '@material-ui/core/TextField'
import Autocomplete from '@material-ui/lab/Autocomplete'
import Tabs from '@material-ui/core/Tabs'
import Tab from '@material-ui/core/Tab'
import Typography from '@material-ui/core/Typography'
import Box from '@material-ui/core/Box'

const cities = [
  { name: 'Temerin' },
  { name: 'Novi Sad' },
  { name: 'Belgrade' },
  { name: 'Subotica' },
  { name: 'Nis' }
]

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

function a11yProps(index) {
  return {
    id: `scrollable-auto-tab-${index}`,
    'aria-controls': `scrollable-auto-tabpanel-${index}`,
  };
}

export const ApplyContainer = () => {

  const [value, setValue] = React.useState(null)
  const [activeTab, setActiveTab] = React.useState(0)

  const handleTabChange = (event, newActiveTab) => {
    setActiveTab(newActiveTab)
  }

  return (
    <div>
      Apply for a job in three clicks.
      (1) Select a city
      (2) Select a job
      (3) View details & apply
      <div>
      <Tabs
        value={activeTab}
        onChange={handleTabChange}
        indicatorColor='primary'
        textColor='primary'
        variant='fullWidth'
      >
        <Tab label='Where' />
        <Tab label='What' />
        <Tab label='Apply' />
      </Tabs>
      <TabPanel value={activeTab} index={0}>
        Item One -> Render City selection component.
      </TabPanel>
      <TabPanel value={activeTab} index={1}>
        Item Two -> Render Job selection component. (disabled until city is chosen).
      </TabPanel>
      <TabPanel value={activeTab} index={2}>
        Item Three -> Render Job details & apply component. (disabled until city & job are chosen).
      </TabPanel>
        <Autocomplete
          id="combo-box-demo"
          options={cities}
          getOptionLabel={option => option.name}
          style={{ width: 300 }}
          onChange={(event, newValue) => {
            setValue(newValue);
          }}
          renderInput={params => (
            <TextField {...params} label="City" variant="outlined" fullWidth />
          )}
        />
      </div>
    </div>
  )
}

export default ApplyContainer