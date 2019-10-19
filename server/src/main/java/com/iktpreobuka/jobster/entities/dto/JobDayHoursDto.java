package com.iktpreobuka.jobster.entities.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.iktpreobuka.jobster.enumerations.EDay;

public class JobDayHoursDto {
	
	//@JsonView(Views.Admin.class)
			@Enumerated(EnumType.STRING)
			@NotNull (message = "Day must be provided.") //???????????????????????????????????
			private EDay day;
			
			//@JsonView(Views.Admin.class)
			@NotNull (message = "From/minimum hour/s must be provided.") //???????????????????????
			@Min(value=0, message = "From/minimum hour/s must be {value} or higher!")
			@Max(value=24, message = "From/minimum hour/s must be {value} or lower!")
			private Integer fromHour;
			
			//@JsonView(Views.Admin.class)
			@NotNull (message = "To/maximum hour/s must be provided.") //???????????????????????????
			@Min(value=0, message = "To/maximum hour/s must be {value} or higher!")
			@Max(value=24, message = "To/maximum hour/s must be {value} or lower!")
			private Integer toHour;
			
			//@JsonView(Views.Admin.class)
			private Boolean flexibileHours;
			
			//@JsonView(Views.Admin.class)
			private Boolean isMinMax;//DRAKULICEV ATRIBUT AKO JE TRU ONDA JE FROMHOUR MINHOUR DNEVNO A TOHOUR MAKSIMALNO SATI TOGDANA, TKO TUMACIM, A AKO JE TO FALSE ONDA HOCU DA RADIM OD FROMHOUR DO TOHOUR

			public JobDayHoursDto() {};
			
			public JobDayHoursDto(EDay day,
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
