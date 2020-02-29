import React from 'react';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import CircularProgress from '@material-ui/core/CircularProgress';
import Geonames from 'geonames.js';
import { useTranslation } from 'react-i18next'

import PropTypes from 'prop-types'

/** 
 *  Container for the cities API interaction. Calling an Autocomplete inside.
 */
export default function CityAPIContainer(props) {
    // const [city, setCity] = React.useState(null)
    const [queryAPI, setQueryAPI] = React.useState([])

    const { t } = useTranslation();

    let loading = false

    const geonames = new Geonames({
        username: 'drax82',
        lan: 'en',
        encoding: 'JSON'
    });
    
    const handleCitiesLoad = (event) => {
        if (!event.target.value || event.target.value === "" || event.target.value === " " ) {
            // props.setCity({name: null, countryName: null, city: null, latitude: null, longitude: null, countryRegion: null, country: null, iso2Code: null}) 
            props.setCity(null) 
        } else {
            loading = true;
            if (!loading) {
                return undefined;
            }
            let utf8Name = encodeURIComponent(event.target.value).replace('%20',' ');
            geonames.search({q: utf8Name, maxRows: '10', style: 'FULL', featureClass: 'P', lang: 'en' }) //get cities
                .then(resp => {
                    setQueryAPI(resp.geonames);
                    loading = false;
                    })
                .catch(err => console.error(err));
        }
    }
    
    const handleCitySelect = (newValue) => {
        // console.log(newValue)
        if (newValue && newValue !== "" && newValue !== " ") {
            props.setCity({name: newValue.name, countryName: newValue.countryName, city: newValue.asciiName, latitude: newValue.lat, longitude: newValue.lng, countryRegion: newValue.adminName1, country: newValue.countryName, iso2Code: newValue.countryCode}) 
        } else {
            // props.setCity({name: null, countryName: null, city: null, latitude: null, longitude: null, countryRegion: null, country: null, iso2Code: null}) 
            props.setCity(null) 
        }
            setQueryAPI([]);
    }
        
  
    return (
        <div>
            <Autocomplete
                id="autocomplete"
                options={queryAPI}
                getOptionLabel={option => option.name + ", " + option.countryName}
                style={{ minWidth: 270 }}
                clearOnEscape
                disableClearable
                value={props.city}
                loading={loading}
                autoComplete
                onChange={(event, newValue) => {
                    handleCitySelect(newValue); 
                }}
                renderInput={params => {
                    return (
                    <TextField 
                        {...params} 
                        label= {t('profile.labelCity')}
                        // label= "City"
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
            />
        </div>
    )
}

CityAPIContainer.propTypes = {
    /**
     * Callback function for setting the current city.
     */
    setCity: PropTypes.func
}