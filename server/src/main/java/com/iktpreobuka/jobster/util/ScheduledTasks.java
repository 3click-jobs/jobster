package com.iktpreobuka.jobster.util;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iktpreobuka.jobster.services.EmailDao;

//probna metoda (radi):
@Component
public class ScheduledTasks {
	
	@Autowired
	EmailDao emailDao;
	
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(cron = "0 0 12 ? * *", zone="Europe/Belgrade" /*fixedRate = 5000*/)
	public void reportCurrentTime() {
		log.info("The time is now {}", dateFormat.format(new Date()));
	}
	
	@Scheduled(cron = "0 0 12 ? * *", zone="Europe/Belgrade" /*fixedRate = 5000*/)
	public void emailTaskTest() throws Exception {
		log.info(" Midnight emails sent : ", dateFormat.format(new Date()));
		emailDao.testEmailSending();
	}
	
	
}
