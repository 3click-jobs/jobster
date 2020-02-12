import React from 'react'
import { makeStyles } from '@material-ui/core/styles';
import JobDayHoursForm from './JobDayHoursForm'
import Card from '@material-ui/core/Card'
import FormLabel from '@material-ui/core/FormLabel'
import CardContent from '@material-ui/core/CardContent'
import { useTranslation } from 'react-i18next'


const days = [
    {    
        chck: false,
        day: "DAY_MONDAY",
        flexibileHours: false,
        fromHour: "",
        isMinMax: false,
        toHour: "",
        id: 1
      },
      {    
        chck: false,
        day: "DAY_TUESDAY",
        flexibileHours: false,
        fromHour: "",
        isMinMax: false,
        toHour: "",
        id: 2
      },
      {    
        chck: false,
        day: "DAY_WEDNESDAY",
        flexibileHours: false,
        fromHour: "",
        isMinMax: false,
        toHour: "",
        id: 3
      },
      {    
        chck: false,
        day: "DAY_THURSDAY",
        flexibileHours: false,
        fromHour: "",
        isMinMax: false,
        toHour: "",
        id: 4
      },
      {    
        chck: false,
        day: "DAY_FRIDAY",
        flexibileHours: false,
        fromHour: "",
        isMinMax: false,
        toHour: "",
        id: 5
      },
      {    
        chck: false,
        day: "DAY_SATURDAY",
        flexibileHours: false,
        fromHour: "",
        isMinMax: false,
        toHour: "",
        id: 6
      },
      {    
        chck: false,
        day: "DAY_SUNDAY",
        flexibileHours: false,
        fromHour: "",
        isMinMax: false,
        toHour: "",
        id: 7
      },
]

const useStyles = makeStyles(() => ({
  card: {
    maxWidth: 700,
    marginTop: 10
  },
  container: {
    display: "flex",
    justifyContent: "center"
  },
  actions: {
    float: "right"
  }
}))


export const JobDayHoursContainer = (props) => {

  const [inputListJobDayHours, setInputListJobDayHours] = React.useState([])
  const classes = useStyles();
  const { t } = useTranslation();

  const { touched, errors, handleChange, handleBlur } = props;

//   const useComponentDidMount = func => React.useEffect(func, []);

//   const useComponentWillMount = func => {
//     const willMount = React.useRef(true);
  
//     if (willMount.current) {
//       func();
//     }
  
//     useComponentDidMount(() => {
//       willMount.current = false;
//     });
//   };

//   let inputListJobDayHours = []

//   useComponentWillMount(() => {
//     if (props.listJobDayHours) {
//         const tempList = []
//         props.listJobDayHours.forEach((el) => { tempList.push({...el, chck: true}); })
//         inputListJobDayHours = days.map( day => props.listJobDayHours.some(x => x.day === day.day) ? tempList.find(element => element.day === day.day) : day)
//         // setInputListJobDayHours(tempList)
//     }
//     else
//         // setInputListJobDayHours(days)
//         inputListJobDayHours = [...days]
//   })

  React.useEffect(() => {
    if (props.listJobDayHours) {
        const tempList = []
        props.listJobDayHours.forEach((el) => { tempList.push({...el, chck: true}); })
        // inputListJobDayHours = days.map( day => props.listJobDayHours.some(x => x.day === day.day) ? tempList.find(element => element.day === day.day) : day)
        setInputListJobDayHours(days.map( day => props.listJobDayHours.some(x => x.day === day.day) ? tempList.find(element => element.day === day.day) : day))
    }
    else {
        setInputListJobDayHours([...days])
        // inputListJobDayHours = [...days]
    }
  }, [props.listJobDayHours])


  const handleJobDayHoursChange = (newValue, forDay) => {
    if (newValue && newValue.target.name === "day") {
        // console.log(newValue)
        let changedListJobDayHours = inputListJobDayHours.map( day => (newValue.target.value === day.day) ? {...day, chck: !day.chck } : day)
        // console.log(changedListJobDayHours)
        setInputListJobDayHours([...changedListJobDayHours])
        props.setJobDayHoursChange( changedListJobDayHours.filter( day => (day.chck === true) ).map( day => ( ({chck, ...o }) => o )(day) ) );
    }
    else if (newValue ) {
        // console.log("handleJobDayHoursChange ok")
        // console.log(newValue)
        // console.log(newValue.target.name)
        // console.log(newValue.target.type)
        // console.log(newValue.target.value)
        // inputListJobDayHours.map( day => Object.keys(day).map( (elKey, index) => (newValue.target.name === elKey) ? console.log("Key: " + elKey + ", index: " + index + ", values: " + day[elKey] + ", INVERTED: " + !day[elKey]) : null ) )
        // OK JE OVA inputListJobDayHours.map( day => { console.log(Object.assign({}, ...Object.entries(day).map( (elKey, index) => ({[elKey[0]]: elKey[1]}) ))) })
        // let changedListJobDayHours = inputListJobDayHours.map( day => (forDay === day.day) ? (Object.keys(day).map( (elKey, index) => day[elKey] = (newValue.target.name === elKey) ? (newValue.target.type === "checkbox") ? !day[elKey] : day[elKey] = newValue.target.value : day[elKey] ) ) : day)
        let changedListJobDayHours = inputListJobDayHours.map( day => (forDay === day.day) ? ( Object.assign({}, ...Object.entries(day).map( (elKey, index) => ({ [elKey[0]]: (newValue.target.name === elKey[0]) ? ( (newValue.target.type === "checkbox") ? !elKey[1] : ( (newValue.target.value === "") ? 0 : JSON.parse(newValue.target.value.replace(/^0+(?=\d)/, ''))) ) : elKey[1] })  ) ) ) : day )
        // console.log(changedListJobDayHours)
        setInputListJobDayHours([...changedListJobDayHours])
        props.setJobDayHoursChange( changedListJobDayHours.filter( day => (day.chck === true) ).map( day => ( ({chck, ...o }) => o )(day) ) );
    } 
  }


  return (
    // <div className={classes.container}>
    <Card className={classes.card}>
        <CardContent>
            <FormLabel component="legend">
              {t('jobDayHoursContainer.choseDaysAndDetails')}
              {/* Chose day/s and details */}
            </FormLabel>
            {inputListJobDayHours.map(jobDayHours => {
                return <JobDayHoursForm key={jobDayHours.day} handleJobDayHoursChange={handleJobDayHoursChange} jobDayHours={jobDayHours} handleChange={handleChange} handleBlur={handleBlur} touched={touched} errors={errors} />
            })}
        </CardContent>
    </Card>
    // </div>
  )
}

export default JobDayHoursContainer