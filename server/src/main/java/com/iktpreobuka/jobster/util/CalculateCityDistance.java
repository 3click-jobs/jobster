package com.iktpreobuka.jobster.util;

public class CalculateCityDistance {

	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::                                                                         :*/
	/*::  This routine calculates the distance between two points (given the     :*/
	/*::  latitude/longitude of those points). It is being used to calculate     :*/
	/*::  the distance between two locations using GeoDataSource (TM) prodducts  :*/
	/*::                                                                         :*/
	/*::  Definitions:                                                           :*/
	/*::    South latitudes are negative, east longitudes are positive           :*/
	/*::                                                                         :*/
	/*::  Passed to function:                                                    :*/
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	/*::    unit = the unit you desire for results                               :*/
	/*::           where: 'MI' is statute miles (default)                        :*/
	/*::                  'KM' is kilometers                                     :*/
	/*::                  'NM' is nautical miles                                 :*/
	/*::  Worldwide cities and other features databases with latitude longitude  :*/
	/*::  are available at https://www.geodatasource.com                         :*/
	/*::                                                                         :*/
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


	public static double calculateCityDistance(double lat1, double lon1, double lat2, double lon2, String unit) throws Exception {
		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		} else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515; //Miles
			if (unit == "KM") { 
				dist = dist * 1.609344; //Kilometers
			} else if (unit == "NM") {
				dist = dist * 0.8684; //Nautical Miles
			}
			return (dist);
		}
	}
}
