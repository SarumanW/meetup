package com.meetup.meetup.scheduler;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class EventUpdateScheduler {

    @Autowired
    private EventService eventService;

    private static final Logger log = LoggerFactory.getLogger(EventUpdateScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

    @Scheduled(cron="0 54 17 * * *")
    public void reportCurrentTime() {
        String currentDate = dateFormat.format(new Date());
        List<Event> events = eventService.getEventsByPeriodForAllUsers("2018-05-01 00:00:00.00","2018-05-30 23:59:59.00");
        for(Event e : events){
            System.out.println(e);
        }
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

}
