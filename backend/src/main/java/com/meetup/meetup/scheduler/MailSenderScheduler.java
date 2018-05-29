package com.meetup.meetup.scheduler;

import com.meetup.meetup.dao.EventDao;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.MailService;
import com.meetup.meetup.service.PdfCreateService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.meetup.meetup.keys.Key.*;

@Component
public class MailSenderScheduler {

    private UserDao userDao;
    private EventDao eventDao;
    private PdfCreateService pdfCreateService;
    private MailService mailService;

    @Autowired
    public MailSenderScheduler(UserDao userDao, EventDao eventDao, PdfCreateService pdfCreateService,
                               MailService mailService) {
        this.userDao = userDao;
        this.eventDao = eventDao;
        this.pdfCreateService = pdfCreateService;
        this.mailService = mailService;
    }

    private static final Logger log = LoggerFactory.getLogger(EventUpdateScheduler.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

    @Scheduled(cron = "${cron.daily}")
    public void sendMails() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if (cal.get(Calendar.DAY_OF_MONTH) == 1) {
            sendMounthly(cal);
        }
        sendWeekly(cal);
        sendDaily(cal);
    }

    private void sendMounthly(Calendar cal) {
        Date addMonth = DateUtils.addMonths(cal.getTime(), 1);
        List<User> users = userDao.getByEmailPeriod(MAIL_MONTHLY);
        sendMailToUsers(users, dateFormat.format(cal.getTime()) + " 00:00:00",
                dateFormat.format(addMonth) + " 00:00:00", dateFormat.format(cal.getTime()));
    }

    private void sendWeekly(Calendar cal) {
        Date addWeek = DateUtils.addWeeks(cal.getTime(), 1);
        List<User> users = new ArrayList<>();
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                users = userDao.getByEmailPeriod(MAIL_EVERY_SUNDAY);
                break;
            case 2:
                users = userDao.getByEmailPeriod(MAIL_EVERY_MONDAY);
                break;
            case 3:
                users = userDao.getByEmailPeriod(MAIL_EVERY_TUESDAY);
                break;
            case 4:
                users = userDao.getByEmailPeriod(MAIL_EVERY_WEDNESDAY);
                break;
            case 5:
                users = userDao.getByEmailPeriod(MAIL_EVERY_THURSDATY);
                break;
            case 6:
                users = userDao.getByEmailPeriod(MAIL_EVERY_FRIDAY);
                break;
            case 7:
                users = userDao.getByEmailPeriod(MAIL_EVERY_SATURDAY);
                break;
        }
        sendMailToUsers(users, dateFormat.format(cal.getTime()) + " 00:00:00",
                dateFormat.format(addWeek) + " 00:00:00", dateFormat.format(cal.getTime()));
    }

    private void sendDaily(Calendar cal) {
        List<User> users = userDao.getByEmailPeriod(MAIL_DAILY);
        sendMailToUsers(users, dateFormat.format(cal.getTime()) + " 00:00:00",
                dateFormat.format(cal.getTime()) + " 23:59:59", dateFormat.format(cal.getTime()));
    }

    private void sendMailToUsers(List<User> users, String startDate, String endDate, String currentDate) {
        for (User user : users) {
            List<Event> events = eventDao.getPeriodEvents(user.getId(), startDate, endDate);
            if (events.size() > 0) {
                pdfCreateService.createPDF(events);
                mailService.sendMailWithEventPlan(user, new File("events.pdf"), currentDate);
            }
        }
    }

}
