import React from 'react'
import { connect } from 'react-redux'
import FilterListIcon from '@material-ui/icons/FilterList';
import Drawer from '@material-ui/core/Drawer'
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
// import { Link as RouterLink } from 'react-router-dom';

import Fab from '@material-ui/core/Fab';
import Tooltip from '@material-ui/core/Tooltip';
import { makeStyles } from '@material-ui/styles';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import Collapse from '@material-ui/core/Collapse';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import Checkbox from '@material-ui/core/Checkbox';
import TextField from '@material-ui/core/TextField'
// import FilteringJobDaysHoursForm from './FilteringJobDaysHoursForm';
import { Formik, Form } from 'formik'
import { offers as offersSchema } from '../../common/utils/schemas/offers'
import JobDayHoursContainer from '../jobDayHours/JobDayHoursContainer';



const useStyles = makeStyles(theme => ({
    root: {
      flexGrow: 1,
    },
    title: {
      display: 'flex',
      justifyContent: 'center',
      // alignItems: 'center',
      // textAlign: 'center',
      flexGrow: 1,
      '& a': {
        textDecoration: 'none',
      },
      '& img': {
        position: 'relative',
        top: '1px',
        height: '40px'
      }
    },
    floatingTopRight : {
      margin: 0,
      top: 'auto',
      right: 20,
    //   top: 60,
      left: 'auto',
      position: 'fixed',
      overflowX: 'hidden',
    },
  }))

// const ConnectedLink = React.forwardRef((props, ref) => <RouterLink innerRef={ref} {...props} />);

// function ListItemLink(props) {
//   return <ListItem button component={ConnectedLink} {...props} />;
// }


export const FilteringPosts = ({
    price,
    beginningDate,
    endDate,
    flexibileDates,
    flexibileDays,
    listJobDayHours,
    handleChangePrice,
    handleChangeBeginningDate,
    handleChangeEndDate,
    handleChangeFlexibileDates,
    handleChangeFlexibileDays,
    handleChangeDayHours,
  }) => {

  const [drawer, setDrawer] = React.useState(false)
  const [openPrice, setOpenPrice] = React.useState(false);
  const [openDates, setOpenDates] = React.useState(false);
  const [openDaysHours, setOpenDaysHours] = React.useState(false);

  const classes = useStyles()

  const toggleDrawer = state => event => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }
    setDrawer(state);
    if (!state) {
        setOpenPrice(false);
        setOpenDates(false);
        setOpenDaysHours(false)
    }

  }

  const handleClickPrice = () => {
    if (!openPrice) {
        setOpenDates(false);
        setOpenDaysHours(false)
    }
    setOpenPrice(!openPrice);
  };

  const handleClickDates = () => {
    if (!openDates) {
        setOpenPrice(false);
        setOpenDaysHours(false)
    }
    setOpenDates(!openDates);
  };

  const handleClickDaysHours = () => {
    if (!openDaysHours) {
        setOpenPrice(false);
        setOpenDates(false)
    }
    setOpenDaysHours(!openDaysHours);
  };


  return (
    <div className={classes.root}>
        <Tooltip title="Top" aria-label="top" placement="right-end" onClick={toggleDrawer(true)}>
            <Fab color="primary" visibility="hidden" className={classes.floatingTopRight} >
                <FilterListIcon />
            </Fab>
        </Tooltip>
        <Drawer
            anchor='right'
            open={drawer}
            onClose={toggleDrawer(false)}
        >
            <div
            role="presentation"
            // onClick={toggleDrawer(false)}
            // onKeyDown={toggleDrawer(false)}
            >
            <React.Fragment>
                <List component="nav" aria-label="Users">

                    <ListItem button onClick={handleClickPrice}>
                        <ListItemText primary="Maximum price" />
                        {openPrice ? <ExpandLess /> : <ExpandMore />}
                    </ListItem>
                    <Collapse in={openPrice} timeout="auto" unmountOnExit>
                        <List component="div" disablePadding>
                            <ListItem button className={classes.nested}>
                                <TextField
                                    label="Price per hour"
                                    name="price"
                                    type="number"
                                    value={price}
                                    // onChange={handleChange}
                                    onChange={(event) => {
                                        if (event.target.value < 0 || event.target.value === null || event.target.value === "" ) {
                                            event.persist()
                                            handleChangePrice(JSON.parse("0"));
                                        } else {
                                            event.persist()
                                            handleChangePrice(JSON.parse(event.target.value.replace(/^0+(?=\d)/, '')))
                                        }
                                    }}
                                    margin="normal"
                                    variant="outlined"
                                    fullWidth={true}
                                />
                            </ListItem>
                        </List>
                    </Collapse>

                    <ListItem button onClick={handleClickDates}>
                        <ListItemText primary="Acceptable dates" />
                        {openDates ? <ExpandLess /> : <ExpandMore />}
                    </ListItem>
                    <Collapse in={openDates} timeout="auto" unmountOnExit>
                        <List component="div" disablePadding>
                            <ListItem button className={classes.nested}>
                                <TextField
                                    label="Beginning date"
                                    name="beginningDate"
                                    type="date"
                                    value={beginningDate}
                                    // onChange={handleChange}
                                    onChange={(event) => {
                                        if (event.target.value === null || event.target.value === "" ) {
                                            handleChangeBeginningDate("");
                                        } else {
                                            handleChangeBeginningDate(event.target.value)
                                        }
                                    }}
                                    margin="normal"
                                    variant="outlined"
                                    fullWidth={true}
                                />
                            </ListItem>
                            <ListItem button className={classes.nested}>
                                <TextField
                                    label="End date"
                                    name="endDate"
                                    type="date"
                                    value={endDate}
                                    // onChange={handleChange}
                                    onChange={(event) => {
                                        if (event.target.value === null || event.target.value === "" ) {
                                            handleChangeEndDate("");
                                        } else {
                                            handleChangeEndDate(event.target.value)
                                        }
                                    }}
                                    margin="normal"
                                    variant="outlined"
                                    fullWidth={true}
                                />
                            </ListItem>
                            <ListItem button className={classes.nested}>
                                <FormControl component="fieldset">
                                    <FormControlLabel
                                        value={flexibileDates}
                                        checked={flexibileDates}
                                        control={<Checkbox color="primary" />}
                                        label="Flexibile dates"
                                        labelPlacement="bottom"
                                        // onChange={handleChange}
                                        onChange={(event) => {
                                            // console.log(event.target)
                                            // event.persist()
                                            handleChangeFlexibileDates(JSON.parse(event.target.value))
                                            }
                                        }   
                                    />
                                </FormControl>
                            </ListItem>
                        </List>
                    </Collapse>

                    <ListItem button onClick={handleClickDaysHours}>
                        <ListItemText primary="Acceptable days and hours" />
                        {openDaysHours ? <ExpandLess /> : <ExpandMore />}
                    </ListItem>
                    <Collapse in={openDaysHours} timeout="auto" unmountOnExit>
                        <List component="div" disablePadding>
                            <ListItem button className={classes.nested}>
                                <Formik
                                    initialValues={ {listJobDayHoursPostDto: listJobDayHours } }
                                    onSubmit={(values, formikBag ) => {

                                        console.log(values)
                                        listJobDayHours = [...values]

                                        handleChangeDayHours([...values])
                                        formikBag.setSubmitting(false)

                                    }}

                                    validationSchema={offersSchema.create}
                                    // component={FilteringJobDaysHoursForm}
                                    
                                    // render={(form) => {
                                    //   return <React.Fragment>
                                    //         <Form>
                                    //             <JobDayHoursContainer listJobDayHours={form.values.listJobDayHoursPostDto} 
                                    //                                   handleChange={form.handleChange} 
                                    //                                   handleBlur={form.handleBlur} 
                                    //                                   touched={form.touched} 
                                    //                                   errors={form.errors} 
                                    //                                   setJobDayHoursChange={(props) => 
                                    //                                     { 
                                    //                                         // values.listJobDayHoursPostDto = [...props];
                                    //                                         form.setFieldValue('listJobDayHoursPostDto', [...props])
                                    //                                         handleChangeDayHours([...props])
                                    //                                     } } 
                                    //                                   />
                                    //         </Form>
                                    //     </React.Fragment>
                                    // }}

                                >
                                    {(form) => {
                                      return <React.Fragment>
                                            <Form>
                                                <JobDayHoursContainer listJobDayHours={form.values.listJobDayHoursPostDto} 
                                                                      handleChange={form.handleChange} 
                                                                      handleBlur={form.handleBlur} 
                                                                      touched={form.touched} 
                                                                      errors={form.errors} 
                                                                      setJobDayHoursChange={(props) => 
                                                                        { 
                                                                            // values.listJobDayHoursPostDto = [...props];
                                                                            form.setFieldValue('listJobDayHoursPostDto', [...props])
                                                                            handleChangeDayHours([...props])
                                                                        } } 
                                                                      />
                                            </Form>
                                        </React.Fragment>
                                    }}
                                </Formik>
                                {/* <JobDayHoursContainer listJobDayHours={listJobDayHours} 
                                                    setJobDayHoursChange={(props) => 
                                                        { listJobDayHours = [...props]; 
                                                            handleChangeDayHours([...props]);
                                                        } } 
                                                    /> */}
                            </ListItem>
                            <ListItem button className={classes.nested}>
                                <FormControl component="fieldset">
                                    <FormControlLabel
                                        value={flexibileDays}
                                        checked={flexibileDays}
                                        control={<Checkbox color="primary" />}
                                        label="Flexibile days"
                                        labelPlacement="bottom"
                                        // onChange={handleChange}
                                        onChange={(event) => {
                                            // console.log(event.target)
                                                handleChangeFlexibileDays(JSON.parse(event.target.value))
                                            }
                                        }   
                                    />
                                </FormControl>                   
                            </ListItem>
                        </List>
                    </Collapse>
                    <Divider />
                </List>
            </React.Fragment>
            </div>
        </Drawer>
    </div>
  )
}

const mapStateToProps = (state) => {
  return {
    role: state.user.role
  }
}

export default connect(
  mapStateToProps
)(FilteringPosts)