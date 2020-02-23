import React from 'react'
import { connect } from 'react-redux'
import Grid from '@material-ui/core/Grid'

import { actions as seeksActions } from '../../../redux/actions/seeks'
import { seeksSelectors } from '../../../redux/selectors/seeks'
import SeekCard from './SeekCard'
import ToTopButton from '../../toTopButton/ToTopButton';
import FilteringPosts from '../../filteringPosts/FilteringPosts'
import { useTranslation } from 'react-i18next'


const toRadians = (angle) => {
  return angle * (Math.PI / 180);
}

const toDegrees = (radians) => {
  return radians * (180 / Math.PI);
}

const countDistance = (lat1, lon1, lat2, lon2, unit) => {
  if ((lat1 === lat2) && (lon1 === lon2)) {
    return 0;
  } else {
    let theta = lon1 - lon2;
    let dist = Math.sin(toRadians(lat1)) * Math.sin(toRadians(lat2)) + Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) * Math.cos(toRadians(theta));
    dist = Math.acos(dist);
    dist = toDegrees(dist);
    dist = dist * 60 * 1.1515; //Miles
    if (unit === "KM") { 
      dist = dist * 1.609344; //Kilometers
    } else if (unit === "NM") {
      dist = dist * 0.8684; //Nautical Miles
    }
    return (dist);
  }
}

const dates = {
  convert:function(d) {
      // Converts the date in d to a date-object. The input can be:
      //   a date object: returned without modification
      //  an array      : Interpreted as [year,month,day]. NOTE: month is 0-11.
      //   a number     : Interpreted as number of milliseconds
      //                  since 1 Jan 1970 (a timestamp) 
      //   a string     : Any format supported by the javascript engine, like
      //                  "YYYY/MM/DD", "MM/DD/YYYY", "Jan 31 2009" etc.
      //  an object     : Interpreted as an object with year, month and date
      //                  attributes.  **NOTE** month is 0-11.
      return (
          d.constructor === Date ? d :
          d.constructor === Array ? new Date(d[0],d[1],d[2]) :
          d.constructor === Number ? new Date(d) :
          d.constructor === String ? new Date(d) :
          typeof d === "object" ? new Date(d.year,d.month,d.date) :
          NaN
      );
  },
  compare:function(a,b) {
      // Compare two dates (could be of any type supported by the convert
      // function above) and returns:
      //  -1 : if a < b
      //   0 : if a = b
      //   1 : if a > b
      // NaN : if a or b is an illegal date
      // NOTE: The code inside isFinite does an assignment (=).
      return (
          isFinite(a=this.convert(a).valueOf()) &&
          isFinite(b=this.convert(b).valueOf()) ?
          (a>b)-(a<b) :
          NaN
      );
  },
  inRange:function(d,start,end) {
      // Checks if date in d is between dates in start and end.
      // Returns a boolean or NaN:
      //    true  : if d is between start and end (inclusive)
      //    false : if d is before start or after end
      //    NaN   : if one or more of the dates is illegal.
      // NOTE: The code inside isFinite does an assignment (=).
     return (
          isFinite(d=this.convert(d).valueOf()) &&
          isFinite(start=this.convert(start).valueOf()) &&
          isFinite(end=this.convert(end).valueOf()) ?
          start <= d && d <= end :
          NaN
      );
  }
}


export const SeeksContainer = ({
  role,
  seeksAll,
  seeksIsLoading,
  seeksIsError,
  loadSeeksAll,
  accept,
  decline,
  selectedCity,
  selectedJobType,
}) => {

  const [acceptableSeeks, setAcceptableSeeks] = React.useState(null)
  const [acceptableSeeksBasic, setAcceptableSeeksBasic] = React.useState(null)
  const [price, setPrice] = React.useState(0)
  const [beginningDate, setBeginningDate] = React.useState("")
  const [endDate, setEndDate] = React.useState("")
  const [flexibileDates, setFlexibileDates] = React.useState(false)
  const [flexibileDays, setFlexibileDays] = React.useState(false)
  const [listJobDayHours, setListJobDayHours] = React.useState([])

  const { t } = useTranslation();

  React.useEffect(() => {
    if (seeksAll.length === 0)
      loadSeeksAll()
  }, [loadSeeksAll, seeksAll])

  React.useEffect(() => {
    if (selectedCity && selectedJobType) {
      // seeksAll.forEach(element => {
      //   // console.log(element.city)
      //   // console.log(element.latitude + " " + element.longitude)
      //   // console.log(element.jobType.jobTypeName)
      //   // console.log(countDistance(element.latitude, element.longitude, selectedCity.latitude, selectedCity.longitude, "KM"))
      // });
      const byCityAndJob = seeksAll.filter(o => o.jobType.jobTypeName === selectedJobType.jobTypeName 
                                                && ( (o.city === selectedCity.city && o.countryRegion === selectedCity.countryRegion && o.country === selectedCity.country)
                                                || (countDistance(o.latitude, o.longitude, selectedCity.latitude, selectedCity.longitude, "KM") <= o.distanceToJob) ))
      setAcceptableSeeksBasic(byCityAndJob)
      // console.log("byCityAndJob")
      // console.log(byCityAndJob)
    } else {
      setAcceptableSeeksBasic(seeksAll)
      // console.log("seeksAll")
      // console.log(seeksAll)
    }
  }, [seeksAll, selectedCity, selectedJobType])

  const handleFiltering = (price, dateStart, dateEnd, flexDates, flexDays, dayHours) => {
    // setPrice(price)
    if (acceptableSeeksBasic) {
      let filteredOffers = acceptableSeeksBasic
      if (price && price > 0 ) {
        filteredOffers = filteredOffers.filter(o => o.price <= price)
      }
      if (dateStart && dateStart !== "" && dateEnd && dateEnd !== "" ) {
        if (flexDates) {
          filteredOffers = filteredOffers.filter(o => ( o.flexibileDates === false && dates.inRange(o.beginningDate, dateStart, dateEnd) && dates.inRange(o.endDate, dateStart, dateEnd) )
                                                            || ( o.flexibileDates === true && 
                                                              ( ( dates.inRange(dateStart, o.beginningDate, o.endDate) && dates.inRange(dateEnd, o.beginningDate, o.endDate) ) 
                                                              || ( dates.compare(dateStart, o.beginningDate) === -1 && dates.inRange(dateEnd, o.beginningDate, o.endDate) )
                                                              || ( dates.compare(dateEnd, o.endDate) === 1 && dates.inRange(dateStart, o.beginningDate, o.endDate) ) 
                                                              || ( dates.inRange(o.beginningDate, dateStart, dateEnd) && dates.inRange(o.endDate, dateStart, dateEnd) ) ) ) )
        } else {
          filteredOffers = filteredOffers.filter(o => ( o.flexibileDates === false && dates.compare(dateStart, o.beginningDate) === 0 && dates.compare(dateEnd, o.endDate) === 0 )
                                                            || ( o.flexibileDates === true && dates.inRange(dateStart, o.beginningDate, o.endDate) && dates.inRange(dateEnd, o.beginningDate, o.endDate) ) )
        }
      }
      if ( dayHours && dayHours.length > 0 ) {
        if (flexDays) {
          filteredOffers = filteredOffers.filter( (o) => o.flexibileDays ? 
                                                        o.listJobDayHoursPostDto.map( (item) => dayHours.map( (day) => day.day === item.day ? 
                                                                  day.isMinMax === item.isMinMax ?
                                                                    day.flexibileHours ?
                                                                      item.flexibileHours ?
                                                                        ((day.fromHour <= item.fromHour &&
                                                                          day.fromHour < item.toHour &&
                                                                          day.toHour > item.fromHour &&
                                                                          day.toHour >= item.toHour)
                                                                        || (day.fromHour >= item.fromHour &&
                                                                          day.fromHour < item.toHour &&
                                                                          day.toHour <= item.toHour &&
                                                                          day.toHour > item.fromHour)
                                                                        || (day.fromHour < item.fromHour &&
                                                                          day.toHour <= item.toHour && 
                                                                          day.toHour >= item.fromHour)
                                                                        || (day.fromHour >= item.fromHour &&
                                                                          day.fromHour <= item.toHour &&
                                                                          day.toHour > item.toHour) )
                                                                      :
                                                                        (day.fromHour <= item.fromHour &&
                                                                        day.fromHour < item.toHour &&
                                                                        day.toHour > item.fromHour &&
                                                                        day.toHour >= item.toHour)
                                                                    : item.flexibileHours ?
                                                                        (day.fromHour >= item.fromHour &&
                                                                        day.fromHour < item.toHour &&
                                                                        day.toHour > item.fromHour &&
                                                                        day.toHour <= item.toHour)
                                                                      :
                                                                        (day.fromHour === item.fromHour &&
                                                                        day.toHour === item.toHour)
                                                                  : (day.isMinMax === true && item.isMinMax === false) ?
                                                                      day.flexibileHours ?
                                                                        (day.fromHour !== "" && day.toHour !== "")
                                                                        : (day.fromHour <= (item.toHour - item.fromHour) && day.toHour >= (item.toHour - item.fromHour))
                                                                    : (day.isMinMax === false && item.isMinMax === true) ? 
                                                                      item.flexibileHours ? 
                                                                        (day.fromHour !== "" && day.toHour !== "")
                                                                        : (item.fromHour <= (day.toHour - day.fromHour) && item.toHour >= (day.toHour - day.fromHour))
                                                                      : false
                                                                :
                                                                  false 
                                                              ).some( (e) => e === true ) 
                                                          ).some((el) => el === true ) 
                                                        : o.listJobDayHoursPostDto.map((item) => dayHours.map( (day) => day.day === item.day ? 
                                                                  day.isMinMax === item.isMinMax ?
                                                                      day.flexibileHours ?
                                                                        item.flexibileHours ?
                                                                          ((day.fromHour <= item.fromHour &&
                                                                            day.fromHour < item.toHour &&
                                                                            day.toHour > item.fromHour &&
                                                                            day.toHour >= item.toHour)
                                                                          || (day.fromHour >= item.fromHour &&
                                                                            day.fromHour < item.toHour &&
                                                                            day.toHour <= item.toHour &&
                                                                            day.toHour > item.fromHour)
                                                                          || (day.fromHour < item.fromHour &&
                                                                            day.toHour <= item.toHour && 
                                                                            day.toHour >= item.fromHour)
                                                                          || (day.fromHour >= item.fromHour &&
                                                                            day.fromHour <= item.toHour &&
                                                                            day.toHour > item.toHour) )
                                                                        :
                                                                          (day.fromHour <= item.fromHour &&
                                                                          day.fromHour < item.toHour &&
                                                                          day.toHour > item.fromHour &&
                                                                          day.toHour >= item.toHour)
                                                                      : item.flexibileHours ?
                                                                          (day.fromHour >= item.fromHour &&
                                                                          day.fromHour < item.toHour &&
                                                                          day.toHour > item.fromHour &&
                                                                          day.toHour <= item.toHour)
                                                                        :
                                                                          (day.fromHour === item.fromHour &&
                                                                          day.toHour === item.toHour)
                                                                  : (day.isMinMax === true && item.isMinMax === false) ?
                                                                      day.flexibileHours ?
                                                                        (day.fromHour !== "" && day.toHour !== "")
                                                                        : (day.fromHour <= (item.toHour - item.fromHour) && day.toHour >= (item.toHour - item.fromHour))
                                                                    : (day.isMinMax === false && item.isMinMax === true) ? 
                                                                      item.flexibileHours ? 
                                                                        (day.fromHour !== "" && day.toHour !== "")
                                                                        : (item.fromHour <= (day.toHour - day.fromHour) && item.toHour >= (day.toHour - day.fromHour))
                                                                      : false
                                                                :
                                                                  false 
                                                              ).some( (e) => e === true ) 
                                                          ).every((el) => el === true ) )
        } else {
          filteredOffers = filteredOffers.filter(o => o.flexibileDays ? 
                                                        dayHours.map( (day) => o.listJobDayHoursPostDto.map( (item) => day.day === item.day ? 
                                                                  day.isMinMax === item.isMinMax ?
                                                                      day.flexibileHours ?
                                                                        item.flexibileHours ?
                                                                          ((day.fromHour <= item.fromHour &&
                                                                            day.fromHour < item.toHour &&
                                                                            day.toHour > item.fromHour &&
                                                                            day.toHour >= item.toHour)
                                                                          || (day.fromHour >= item.fromHour &&
                                                                            day.fromHour < item.toHour &&
                                                                            day.toHour <= item.toHour &&
                                                                            day.toHour > item.fromHour)
                                                                          || (day.fromHour < item.fromHour &&
                                                                            day.toHour <= item.toHour && 
                                                                            day.toHour >= item.fromHour)
                                                                          || (day.fromHour >= item.fromHour &&
                                                                            day.fromHour <= item.toHour &&
                                                                            day.toHour > item.toHour) )
                                                                        :
                                                                          (day.fromHour <= item.fromHour &&
                                                                          day.fromHour < item.toHour &&
                                                                          day.toHour > item.fromHour &&
                                                                          day.toHour >= item.toHour)
                                                                      : item.flexibileHours ?
                                                                          (day.fromHour >= item.fromHour &&
                                                                          day.fromHour < item.toHour &&
                                                                          day.toHour > item.fromHour &&
                                                                          day.toHour <= item.toHour)
                                                                        :
                                                                          (day.fromHour === item.fromHour &&
                                                                          day.toHour === item.toHour)
                                                                  : (day.isMinMax === true && item.isMinMax === false) ?
                                                                      day.flexibileHours ?
                                                                        (day.fromHour !== "" && day.toHour !== "")
                                                                        : (day.fromHour <= (item.toHour - item.fromHour) && day.toHour >= (item.toHour - item.fromHour))
                                                                    : (day.isMinMax === false && item.isMinMax === true) ? 
                                                                      item.flexibileHours ? 
                                                                        (day.fromHour !== "" && day.toHour !== "")
                                                                        : (item.fromHour <= (day.toHour - day.fromHour) && item.toHour >= (day.toHour - day.fromHour))
                                                                      : false
                                                                :
                                                                  false 
                                                              ).some( (e) => e === true ) 
                                                          ).every((el) => el === true ) 
                                                        : dayHours.length === o.listJobDayHoursPostDto.length
                                                          && dayHours.map((day) => o.listJobDayHoursPostDto.map( (item) => day.day === item.day ? 
                                                                  day.isMinMax === item.isMinMax ?
                                                                      day.flexibileHours ?
                                                                        item.flexibileHours ?
                                                                          ((day.fromHour <= item.fromHour &&
                                                                            day.fromHour < item.toHour &&
                                                                            day.toHour > item.fromHour &&
                                                                            day.toHour >= item.toHour)
                                                                          || (day.fromHour >= item.fromHour &&
                                                                            day.fromHour < item.toHour &&
                                                                            day.toHour <= item.toHour &&
                                                                            day.toHour > item.fromHour)
                                                                          || (day.fromHour < item.fromHour &&
                                                                            day.toHour <= item.toHour && 
                                                                            day.toHour >= item.fromHour)
                                                                          || (day.fromHour >= item.fromHour &&
                                                                            day.fromHour <= item.toHour &&
                                                                            day.toHour > item.toHour) )
                                                                        :
                                                                          (day.fromHour <= item.fromHour &&
                                                                          day.fromHour < item.toHour &&
                                                                          day.toHour > item.fromHour &&
                                                                          day.toHour >= item.toHour)
                                                                      : item.flexibileHours ?
                                                                          (day.fromHour >= item.fromHour &&
                                                                          day.fromHour < item.toHour &&
                                                                          day.toHour > item.fromHour &&
                                                                          day.toHour <= item.toHour)
                                                                        :
                                                                          (day.fromHour === item.fromHour &&
                                                                          day.toHour === item.toHour)
                                                                  : (day.isMinMax === true && item.isMinMax === false) ?
                                                                      day.flexibileHours ?
                                                                        (day.fromHour !== "" && day.toHour !== "")
                                                                        : (day.fromHour <= (item.toHour - item.fromHour) && day.toHour >= (item.toHour - item.fromHour))
                                                                    : (day.isMinMax === false && item.isMinMax === true) ? 
                                                                      item.flexibileHours ? 
                                                                        (day.fromHour !== "" && day.toHour !== "")
                                                                        : (item.fromHour <= (day.toHour - day.fromHour) && item.toHour >= (day.toHour - day.fromHour))
                                                                      : false
                                                                :
                                                                  false 
                                                              ).some( (e) => e === true ) 
                                                          ).every((el) => el === true ) )
        }
      }
      setAcceptableSeeks(filteredOffers)
    }
  }


  return (
    <React.Fragment>
      <FilteringPosts price={price}
                priceType = {t('seeksContainer.priceType')}
                // priceType = "Maximum"
                beginningDate={beginningDate}
                endDate={endDate}
                flexibileDates={flexibileDates}
                flexibileDays={flexibileDays}
                listJobDayHours={listJobDayHours}
                handleChangePrice={(props) => { setPrice(props); handleFiltering(props, beginningDate, endDate, flexibileDates, flexibileDays, listJobDayHours); } }
                handleChangeBeginningDate={(props) => { setBeginningDate(props); handleFiltering(price, props, endDate, flexibileDates, flexibileDays, listJobDayHours); } }
                handleChangeEndDate={(props) => { setEndDate(props); handleFiltering(price, beginningDate, props, flexibileDates, flexibileDays, listJobDayHours); } }
                handleChangeFlexibileDates={(props) => { setFlexibileDates(!props); handleFiltering(price, beginningDate, endDate, !props, flexibileDays, listJobDayHours); } }
                handleChangeFlexibileDays={(props) => { setFlexibileDays(!props); handleFiltering(price, beginningDate, endDate, flexibileDates, !props, listJobDayHours); } }
                handleChangeDayHours={(props) => { setListJobDayHours(props); handleFiltering(price, beginningDate, endDate, flexibileDates, flexibileDays, props); } }
      />
      {
        (!seeksIsLoading && (acceptableSeeks || acceptableSeeksBasic)) &&
        <React.Fragment>
            <p>
              {t('seeksContainer.numberOfJobSeeksInDatabase')}
              {/* Number of job seeks in the database:  */}
              { (acceptableSeeks && acceptableSeeks.length < acceptableSeeksBasic.length ) ? acceptableSeeks.length : acceptableSeeksBasic.length}
            </p>
            <div style={{ padding: '16px' }}>
                <Grid container spacing={2} direction="row" justify="space-evenly" alignItems="flex-start" >
                    {( acceptableSeeks && acceptableSeeks.length < acceptableSeeksBasic.length ) ? acceptableSeeks.map(seek => {
                        return (
                        <Grid key={seek.id} item xs={12} sm={8} md={7} lg={7} xl={7} >
                          <SeekCard seek={seek} role={role} handleDeclineSeek={(declinedSeek) => decline(declinedSeek)} handleAcceptSeek={(acceptedSeek) => accept(acceptedSeek)} />
                        </Grid> )
                    }) : acceptableSeeksBasic.map(seek => {
                        return (
                        <Grid key={seek.id} item xs={12} sm={8} md={7} lg={7} xl={7} >
                          <SeekCard seek={seek} role={role} handleDeclineSeek={(declinedSeek) => decline(declinedSeek)} handleAcceptSeek={(acceptedSeek) => accept(acceptedSeek)} />
                        </Grid> )
                    })}     
                </Grid>
            </div>
            <ToTopButton />
        </React.Fragment>

      }
      {
        (!seeksIsLoading && seeksIsError) &&
        <p>
          {t('seeksContainer.errorAtLoadingJobSeeks')}
          {/* Error at loading job seeks. */}
        </p>
      }
      {
        seeksIsLoading &&
        <p>
          {t('seeksContainer.jobSeeksAreLoading')}
          {/* Job seeks are loading ...  */}
        </p>
      }
    </React.Fragment>

  )
}

const mapStateToProps = (state) => {
  return {
    seeksAll: seeksSelectors.getAll(state),
    seeksIsLoading: seeksSelectors.isFetching(state),
    seeksIsError: seeksSelectors.error(state),
    role: state.user.role
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    loadSeeksAll: () => dispatch(seeksActions.fetchAll()),
    decline: (seek) => dispatch(seeksActions.decline(seek)),
    accept: (seek) => dispatch(seeksActions.accept(seek))
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SeeksContainer)