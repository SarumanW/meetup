package com.meetup.meetup.scheduler;

import com.meetup.meetup.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UnsuccessfulRegistrationScheduler {

    private static final Logger log = LoggerFactory.getLogger(UnsuccessfulRegistrationScheduler.class);

    @Autowired
    private UserDao userDao;

    @Scheduled(cron="${cron.daily}")
    public void changeEventDate() {
        log.debug("Try to delete unconfirmed users from dao");

        int count = userDao.deleteUnconfirmedAccounts();

        log.debug("Successfully deleted {} accounts", count);
    }

}