package com.iktpreobuka.jobster.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;

import com.iktpreobuka.jobster.entities.JobDayHoursEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.dto.JobDayHoursDto;
import com.iktpreobuka.jobster.entities.dto.JobSeekDto;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.JobDayHoursRepository;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
import com.iktpreobuka.jobster.repositories.JobTypeRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;

@Service
public class JobSeekDaoImpl implements JobSeekDao{
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository	;
	
	@Autowired
	private JobDayHoursRepository jobDayHoursRepository;
	
	@Autowired
	private JobSeekRepository jobSeekRepository;
 
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Override //da bi mogao da vidim kako izgleda JobSeekDto bez dana
	public JobSeekDto empty(){	
		return new JobSeekDto();
	}
	
	@Override //da bi mogao da vidim kako izgleda JobSeekDto sa dva dodata dana
	public JobSeekDto emptyWithEmptyDayHours(){
		JobSeekDto emptyWith = new JobSeekDto();
		JobDayHoursDto empty1 = new JobDayHoursDto();
		JobDayHoursDto empty2 = new JobDayHoursDto();
		List<JobDayHoursDto> list = new ArrayList<JobDayHoursDto>();
		list.add(empty1);
		list.add(empty2);
		emptyWith.setListJobDayHoursDto(list);
		return emptyWith;
	}
	
	@Override
	public ResponseEntity<?> addNewSeek(Principal principal, @Valid @RequestBody JobSeekDto seek, BindingResult result){
		
		JobSeekEntity newSeek = new JobSeekEntity();
		
		newSeek.setEmployee(userAccountRepository.findUserByUsername(principal.getName()));
		newSeek.setCity(cityRepository.getByCityName(seek.getCityName()));//ako nema napraviti cityDaoIml.addNewCityWithLoggedUser()
		newSeek.setType(jobTypeRepository.getByJobTypeName(seek.getJobTypeName()));
			
		newSeek.setDistanceToJob(seek.getDistanceToJob());//da vidimo da li radi
		newSeek.setBeginningDate(seek.getEndDate());
		newSeek.setEndDate(seek.getEndDate());
		newSeek.setFlexibileDates(seek.getFlexibileDates());
		newSeek.setPrice(seek.getPrice());
		newSeek.setDetailsLink(seek.getDetailsLink());
		newSeek.setFlexibileDays(seek.getFlexibileDates());
		newSeek.setCreatedById(userAccountRepository.getUserIdByUsername(principal.getName()));
		
		List<JobDayHoursDto> listJobDayHoursDto = new ArrayList<JobDayHoursDto>();
		listJobDayHoursDto = seek.getListJobDayHoursDto();
		
		JobDayHoursEntity newDaysAndHours = new JobDayHoursEntity();
		List<JobDayHoursEntity> listJobDaysAndHours = new ArrayList<JobDayHoursEntity>();
		
		for(JobDayHoursDto i : listJobDayHoursDto) {// ovo nisam siguran da li ce dodavati nove clanove ili ce samo zalepiti poslednji
			newDaysAndHours.setDay(i.getDay());
			newDaysAndHours.setFromHour(i.getFromHour());
			newDaysAndHours.setToHour(i.getToHour());
			newDaysAndHours.setFlexibileHours(i.getFlexibileHours());
			newDaysAndHours.setIsMinMax(i.getIsMinMax());
			listJobDaysAndHours.add(newDaysAndHours);
		}
		
		jobDayHoursRepository.saveAll(listJobDaysAndHours);
		newSeek.setDaysAndHours(listJobDaysAndHours);
		jobSeekRepository.save(newSeek);
		
		
		
		
		
		/*
		logger.info("Adding new seek.");
		try {
			logger.info("Checking errors.'");
			if (result.hasErrors()) {
				logger.info("Errors exist.");
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			logger.info("Error occured during 'Checking errors.'");
			return new ResponseEntity<String>("Error occured during 'Checking errors.'", HttpStatus.BAD_REQUEST);
		}

		JobSeekEntity newSeek = new JobSeekEntity();
		
		
		
		
		
		try {
			logger.info("Mapping atributs for seek.");
			newSeek.setCreatedById(principal.getName());
			
			
			
			
			
			pupil.setActivity("A");
			pupil.setName(someone.getName());
			pupil.setSurname(someone.getSurname());
			pupil.setEmail(someone.getEmail());
			pupilRepository.save(pupil);
		} catch (Exception e) {
			logger.info("Error occured during 'Mapping atributs for parent'.");
			return new ResponseEntity<String>("Error occured during 'Mapping atributs for parent'.",
					HttpStatus.CONFLICT);
		}

		logger.info("New pupil added.");
		return new ResponseEntity<PupilEntity>(pupil, HttpStatus.OK);

	}
		
		
		
		
		
		
		
		
		
		
		*/
		
		
		
		
		return new ResponseEntity<JobSeekDto>(seek, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

}
