package com.meetup.meetup.scheduler;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.exception.runtime.ParseDateException;
import com.meetup.meetup.service.EventService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.meetup.meetup.keys.Key.EXCEPTION_PARSE_DATE;

@Component
public class EventUpdateScheduler {

    @Autowired
    private EventService eventService;

    @Autowired
    private Environment env;

    private static final Logger log = LoggerFactory.getLogger(EventUpdateScheduler.class);

    private static final SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
    private static final SimpleDateFormat hoursDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");

    @Scheduled(cron="${cron.hourly}")
    public void changeEventDate() {
        log.debug("Getting current date");
        Date lastHour = DateUtils.addHours(new Date(),-1);
        String currentDate = hoursDateFormat.format(lastHour);
        log.debug("Current date and hours '{}'",currentDate);
        log.debug("Try to get events with time from '{}' till '{}'",currentDate+":00:00",currentDate+":59:59");
        List<Event> events = eventService.getEventsByPeriodForAllUsers(currentDate+":00:00",currentDate+":59:59");
        log.debug("Got {} events", events.size());
        for(Event e : events){
                updateEvent(e);
        }
    }

    private void updateEvent(Event event){
        log.debug("Updating event '{}'",event);
        Date eventDate = null;
        try {
            log.debug("Try to parse event date");
            eventDate = fullDateFormat.parse(event.getEventDate());
        } catch (ParseException e) {
            log.error("Error with updating event",e);
            throw new ParseDateException(env.getProperty(EXCEPTION_PARSE_DATE));
        }

        switch (event.getPeriodicityId()){
            case 1:
                log.debug("Adding 1 hour to date");
                event.setEventDate(fullDateFormat.format(DateUtils.addHours(eventDate,1)));
                break;
            case 2:
                log.debug("Adding 1 day to date");
                event.setEventDate(fullDateFormat.format(DateUtils.addDays(eventDate,1)));
                break;
            case 3:
                log.debug("Adding 1 week to date");
                event.setEventDate(fullDateFormat.format(DateUtils.addWeeks(eventDate,1)));
                break;
            case 4:
                log.debug("Adding 1 month to date");
                event.setEventDate(fullDateFormat.format(DateUtils.addMonths(eventDate,1)));
                break;
            case 5:
                log.debug("Adding 1 year to date");
                event.setEventDate(fullDateFormat.format(DateUtils.addYears(eventDate,1)));
                break;
        }
        eventService.updateEvent(event);
    }

}
