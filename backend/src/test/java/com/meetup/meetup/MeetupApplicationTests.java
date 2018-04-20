package com.meetup.meetup;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.dao.impl.UserDaoImpl;
import com.meetup.meetup.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MeetupApplicationTests {

    @Test
    public void contextLoads() {

//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
//
//        UserDaoImpl dao = context.getBean("userDaoImpl",UserDaoImpl.class);
//
//        Assert.assertEquals("configuration passed",dao.testMethod());
    }

}
