package com.meetup.meetup.dao.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.not;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoImplTest {

    @Autowired
    private UserDaoImpl userDao;

    @Test
    public void getFriendsIdsTest() {

        Assert.assertThat(userDao.getFriendsIds(1).size(),not(0));
    }

    @Test
    public void printTestValues(){
        userDao.getFriendsIds(1).forEach(System.out::println);
    }
}