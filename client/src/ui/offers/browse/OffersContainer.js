import React from 'react'
import { connect } from 'react-redux'
import Grid from '@material-ui/core/Grid'

import { actions as offersActions } from '../../../redux/actions/offers'
import { offersSelectors } from '../../../redux/selectors/offers'
import OfferCard from './OfferCard'
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


export const OffersContainer = ({
  role,
  offersAll,
  offersIsLoading,
  offersIsError,
  loadOffersAll,
  accept,
  decline,
  selectedCity,
  selectedJobType,
  distance
}) => {

  const [acceptableOffers, setAcceptableOffers] = React.useState(null)
  const [acceptableOffersBasic, setAcceptableOffersBasic] = React.useState(null)
  const [price, setPrice] = React.useState(0)
  const [beginningDate, setBeginningDate] = React.useState("")
  const [endDate, setEndDate] = React.useState("")
  const [flexibileDates, setFlexibileDates] = React.useState(false)
  const [flexibileDays, setFlexibileDays] = React.useState(false)
  const [listJobDayHours, setListJobDayHours] = React.useState([])

  const { t } = useTranslation();

  React.useEffect(() => {
    if (offersAll.length === 0)
      loadOffersAll()
  }, [loadOffersAll, offersAll])

  React.useEffect(() => {
    if (selectedCity && selectedJobType && distance >= 0 ) {
      const byCityAndJob = offersAll.filter(o => o.jobType.jobTypeName === selectedJobType.jobTypeName
                                                && ( (o.city === selectedCity.city && o.countryRegion === selectedCity.countryRegion && o.country === selectedCity.country)
                                                || (countDistance(o.latitude, o.longitude, selectedCity.latitude, selectedCity.longitude, "KM") <= distance) ))
      setAcceptableOffersBasic(byCityAndJob)
    } else {
      setAcceptableOffersBasic(offersAll)
    }
  }, [offersAll, selectedCity, selectedJobType, distance])

  // let windowScrolled = 0

  const handleFiltering = (price, dateStart, dateEnd, flexDates, flexDays, dayHours) => {
    // setPrice(price)
    if (acceptableOffersBasic) {
      let filteredOffers = acceptableOffersBasic
      if (price && price > 0 ) {
        filteredOffers = filteredOffers.filter(o => o.price >= price)
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
      setAcceptableOffers(filteredOffers)
    }
  }

  // const handleChangePrice = (price) => {
  //   // setPrice(price)
  //   if (acceptableOffersBasic && price && price > 0 ) {
  //     const byPrice = acceptableOffersBasic.filter(o => o.price <= price)
  //     setAcceptableOffers(byPrice)
  //   } else if (acceptableOffersBasic && price === 0 ) {
  //     setAcceptableOffers(acceptableOffersBasic)
  //   }
  // }

  // const handleChangeDates = (dateStart, dateEnd, flexDates) => {
  //   if (acceptableOffersBasic && dateStart && dateStart !== "" && dateEnd && dateEnd !== "" ) {
  //     let byDates
  //     if (flexDates) {
  //       byDates = acceptableOffersBasic.filter(o => ( o.flexibileDates === false && dates.inRange(o.beginningDate, dateStart, dateEnd) && dates.inRange(o.endDate, dateStart, dateEnd) )
  //                                                         || ( o.flexibileDates === true && 
  //                                                           ( ( dates.inRange(dateStart, o.beginningDate, o.endDate) && dates.inRange(dateEnd, o.beginningDate, o.endDate) ) 
  //                                                           || ( dates.compare(dateStart, o.beginningDate) === -1 && dates.inRange(dateEnd, o.beginningDate, o.endDate) )
  //                                                           || ( dates.compare(dateEnd, o.endDate) === 1 && dates.inRange(dateStart, o.beginningDate, o.endDate) ) 
  //                                                           || ( dates.inRange(o.beginningDate, dateStart, dateEnd) && dates.inRange(o.endDate, dateStart, dateEnd) ) ) ) )
  //     } else {
  //       byDates = acceptableOffersBasic.filter(o => ( o.flexibileDates === false && dates.compare(dateStart, o.beginningDate) === 0 && dates.compare(dateEnd, o.endDate) === 0 )
  //                                                         || ( o.flexibileDates === true && dates.inRange(dateStart, o.beginningDate, o.endDate) && dates.inRange(dateEnd, o.beginningDate, o.endDate) ) )
  //     }
  //     setAcceptableOffers(byDates)
  //   } else if (acceptableOffersBasic && (dateStart === "" || dateEnd === "") ) {
  //     setAcceptableOffers(acceptableOffersBasic)
  //   }
  // }

  // const handleChangeBeginningDate = (date) => {
  //   setBeginningDate(date)
  //   if (acceptableOffersBasic && date && endDate && endDate !== "" ) {
  //     let byBeginningDate
  //     if (flexibileDates) {
  //       // byBeginningDate = acceptableOffersBasic.filter(o => dates.inRange(date, o.beginningDate, o.endDate) )
  //       byBeginningDate = acceptableOffersBasic.filter(o => ( o.flexibileDates === false && dates.inRange(o.beginningDate, date, endDate) && dates.inRange(o.endDate, date, endDate) )
  //                                                         || ( o.flexibileDates === true && 
  //                                                           ( ( dates.inRange(date, o.beginningDate, o.endDate) && dates.inRange(endDate, o.beginningDate, o.endDate) ) 
  //                                                           || ( dates.compare(date, o.beginningDate) === -1 && dates.inRange(endDate, o.beginningDate, o.endDate) )
  //                                                           || ( dates.compare(endDate, o.endDate) === 1 && dates.inRange(date, o.beginningDate, o.endDate) ) 
  //                                                           || ( dates.inRange(o.beginningDate, date, endDate) && dates.inRange(o.endDate, date, endDate) ) ) ) )
  //     } else {
  //       // byBeginningDate = acceptableOffersBasic.filter(o => dates.compare(date, o.beginningDate) === 0 )
  //       byBeginningDate = acceptableOffersBasic.filter(o => ( o.flexibileDates === false && dates.compare(date, o.beginningDate) === 0 && dates.compare(endDate, o.endDate) === 0 )
  //                                                         || ( o.flexibileDates === true && dates.inRange(date, o.beginningDate, o.endDate) && dates.inRange(endDate, o.beginningDate, o.endDate) ) )
  //     }
  //     setAcceptableOffers(byBeginningDate)
  //   } else if (acceptableOffersBasic && date === "" ) {
  //     setAcceptableOffers(acceptableOffersBasic)
  //   }
  // }

  // const handleChangeEndDate = (date) => {
  //   setEndDate(date)
  //   if (acceptableOffersBasic && date && beginningDate && beginningDate !== "" ) {
  //     let byEndDate
  //     if (flexibileDates) {
  //       // byEndDate = acceptableOffersBasic.filter(o => dates.inRange(date, o.beginningDate, o.endDate) )
  //       byEndDate = acceptableOffersBasic.filter(o => ( o.flexibileDates === false && dates.inRange(o.beginningDate, beginningDate, date) && dates.inRange(o.endDate, beginningDate, date) )
  //                                                   || ( o.flexibileDates === true && 
  //                                                     ( ( dates.inRange(beginningDate, o.beginningDate, o.endDate) && dates.inRange(date, o.beginningDate, o.endDate) ) 
  //                                                     || ( dates.compare(beginningDate, o.beginningDate) === -1 && dates.inRange(date, o.beginningDate, o.endDate) )
  //                                                     || ( dates.compare(date, o.endDate) === 1 && dates.inRange(beginningDate, o.beginningDate, o.endDate) ) 
  //                                                     || ( dates.inRange(o.beginningDate, beginningDate, date) && dates.inRange(o.endDate, beginningDate, date) ) ) ) )
  //     } else {
  //       // byEndDate = acceptableOffersBasic.filter(o => dates.compare(date, o.endDate) === 0 )
  //       byEndDate = acceptableOffersBasic.filter(o => ( o.flexibileDates === false && dates.compare(beginningDate, o.beginningDate) === 0 && dates.compare(date, o.endDate) === 0 )
  //                                                   || ( o.flexibileDates === true && dates.inRange(beginningDate, o.beginningDate, o.endDate) && dates.inRange(date, o.beginningDate, o.endDate) ) )
  //     }
  //     setAcceptableOffers(byEndDate)
  //   } else if (acceptableOffersBasic && date === "" ) {
  //     setAcceptableOffers(acceptableOffersBasic)
  //   }
  // }

  // const handleChangeFlexibileDates = (flDate) => {
  //   setFlexibileDates(!flDate)
  //   console.log(flDate)
  //   if (acceptableOffersBasic && !flDate === true ) {
  //     const byFlexDates = acceptableOffersBasic.filter(o => o.flexibileDates === true)
  //     setAcceptableOffers(byFlexDates)
  //   } else if (acceptableOffersBasic && !flDate === false ) {
  //     setAcceptableOffers(acceptableOffersBasic)
  //   }
  // }

  // const handleChangeFlexibileDays = (flDay) => {
  //   setFlexibileDays(!flDay)
  //   console.log(flDay)
  //   if (acceptableOffersBasic && !flDay === true ) {
  //     const byFlexDays = acceptableOffersBasic.filter(o => o.flexibileDays === true)
  //     setAcceptableOffers(byFlexDays)
  //   } else if (acceptableOffersBasic && !flDay === false ) {
  //     setAcceptableOffers(acceptableOffersBasic)
  //   }
  // }

  // const handleChangeDayHours = (dayHours) => {
  //   console.log(dayHours)
  //   // setListJobDayHours(dayHours)

  //   if (acceptableOffersBasic && dayHours ) {
  //     let byDaysHours
  //     if (flexibileDays) {
  //       byDaysHours = acceptableOffersBasic.filter(o => o.listJobDayHoursPostDto.map((item) => { return dayHours.map((day) => {
  //                                                                             return (day.day === item.day &&
  //                                                                             item.flexibileHours === true ? 
  //                                                                               (day.fromHour >= item.fromHour &&
  //                                                                               day.fromHour <= item.toHour &&
  //                                                                               day.toHour >= item.fromHour &&
  //                                                                               day.toHour <= item.toHour)
  //                                                                             :
  //                                                                               (day.fromHour === item.fromHour &&
  //                                                                               day.toHour === item.toHour) 
  //                                                                             && day.isMinMax === item.isMinMax) }
  //                                                                         ).some((e) => e === true ) }).some((el) => el === true ) )
  //     } else {
  //       byDaysHours = acceptableOffersBasic.filter(o => o.listJobDayHoursPostDto.map((item) => { return dayHours.map((day) => {
  //                                                                             return (day.day === item.day &&
  //                                                                             item.flexibileHours === true ? 
  //                                                                               (day.fromHour >= item.fromHour &&
  //                                                                               day.fromHour <= item.toHour &&
  //                                                                               day.toHour >= item.fromHour &&
  //                                                                               day.toHour <= item.toHour)
  //                                                                             :
  //                                                                               (day.fromHour === item.fromHour &&
  //                                                                               day.toHour === item.toHour) 
  //                                                                             && day.isMinMax === item.isMinMax) }
  //                                                                         ).every((e) => e === true ) }).every((el) => el === true ) )
  //     }
  //     setAcceptableOffers(byDaysHours)
  //   } else if (acceptableOffersBasic && dayHours === "" ) {
  //     setAcceptableOffers(acceptableOffersBasic)
  //   }
  // }


  return (
    <React.Fragment>
      <FilteringPosts price={price}
                      priceType = {t('offersContainer.priceType')}
                      // priceType = "Minimum"
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
        (!offersIsLoading && (acceptableOffers || acceptableOffersBasic)) &&
        <React.Fragment>
            <p>
              {t('offersContainer.totalNumberOfJobOffersInDatabase')}
              {/* Total number of job offers in the database:  */}
              { (acceptableOffers && acceptableOffers.length < acceptableOffersBasic.length ) ? acceptableOffers.length : acceptableOffersBasic.length}
            </p>
            <div style={{ padding: '16px' }}>
              <Grid container spacing={2} direction="row" justify="space-evenly" alignItems="flex-start" >
                    {( acceptableOffers && acceptableOffers.length < acceptableOffersBasic.length ) ? acceptableOffers.map(offer => {
                        return (
                        <Grid key={offer.id} item xs={12} sm={8} md={7} lg={7} xl={7} >
                          <OfferCard offer={offer} role={role} handleDeclineOffer={(declinedOffer) => decline(declinedOffer)} handleAcceptOffer={(acceptedOffer) => accept(acceptedOffer)} />
                        </Grid> )
                    }) : acceptableOffersBasic.map(offer => {
                      return (
                      <Grid key={offer.id} item xs={12} sm={8} md={7} lg={7} xl={7} >
                        <OfferCard offer={offer} role={role} handleDeclineOffer={(declinedOffer) => decline(declinedOffer)} handleAcceptOffer={(acceptedOffer) => accept(acceptedOffer)} />
                      </Grid> )
                  })}     
                </Grid>
            </div>
            <ToTopButton />
        </React.Fragment>

      }
      {
        (!offersIsLoading && offersIsError) &&
        <p>
          {t('offersContainer.errorAtLoadingJobOffers')}
          {/* Error at loading job offers. */}
        </p>
      }
      {
        offersIsLoading &&
        <p>
          {t('offersContainer.jobOffersAreLoading')}
          Job offers are loading ... 
        </p>
      }
    </React.Fragment>

  )
}

const mapStateToProps = (state) => {
  return {
    offersAll: offersSelectors.getAll(state),
    offersIsLoading: offersSelectors.isFetching(state),
    offersIsError: offersSelectors.error(state),
    role: state.user.role
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    loadOffersAll: () => dispatch(offersActions.fetchAll()),
    decline: (offer) => dispatch(offersActions.decline(offer)),
    accept: (offer) => dispatch(offersActions.accept(offer))
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OffersContainer)