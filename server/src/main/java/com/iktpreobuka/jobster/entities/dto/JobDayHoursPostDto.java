package com.iktpreobuka.jobster.entities.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.iktpreobuka.jobster.enumerations.EDay;

public class JobDayHoursPostDto {
	
			@Enumerated(EnumType.STRING)
			@NotNull (message = "Day must be provided.")
			private EDay day;
			
			@NotNull (message = "From/minimum hour/s must be provided.")
			@Min(value=0, message = "From/minimum hour/s must be {value} or higher!")
			@Max(value=24, message = "From/minimum hour/s must be {value} or lower!")
			private Integer fromHour;
			
			@NotNull (message = "To/maximum hour/s must be provided.")
			@Min(value=0, message = "To/maximum hour/s must be {value} or higher!")
			@Max(value=24, message = "To/maximum hour/s must be {value} or lower!")
			private Integer toHour;
			
			private Boolean flexibileHours;
			
			private Boolean isMinMax;

			public JobDayHoursPostDto() {};
			
			public JobDayHoursPostDto(EDay day,
					@Min(value = 0, message = "From/minimum hour/s must be {value} or higher!") @Max(value = 24, message = "From/minimum hour/s must be {value} or lower!") Integer fromHour,
					@Min(value = 0, message = "To/maximum hour/s must be {value} or higher!") @Max(value = 24, message = "To/maximum hour/s must be {value} or lower!") Integer toHour,
					Boolean flexibileHours, Boolean isMinMax) {
				super();
				this.day = day;
				this.fromHour = fromHour;
				this.toHour = toHour;
				this.flexibileHours = flexibileHours;
				this.isMinMax = isMinMax;
			}

			public EDay getDay() {
				return day;
			}

			public void setDay(EDay day) {
				this.day = day;
			}

			public Integer getFromHour() {
				return fromHour;
			}

			public void setFromHour(Integer fromHour) {
				this.fromHour = fromHour;
			}

			public Integer getToHour() {
				return toHour;
			}

			public void setToHour(Integer toHour) {
				this.toHour = toHour;
			}

			public Boolean getFlexibileHours() {
				return flexibileHours;
			}

			public void setFlexibileHours(Boolean flexibileHours) {
				this.flexibileHours = flexibileHours;
			}

			public Boolean getIsMinMax() {
				return isMinMax;
			}

			public void setIsMinMax(Boolean isMinMax) {
				this.isMinMax = isMinMax;
			}
			

}
