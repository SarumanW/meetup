package com.meetup.meetup.dao.impl;

import com.meetup.meetup.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserDaoImplTest {

    @Autowired
    private UserDaoImpl userDaoImpl;

    private User testUser;

    @Before
    public void before() {
        //given
        testUser = new User();
        testUser.setName("testUser");
        testUser.setLogin("testLogin");
        testUser.setEmail("emailTest");
        testUser.setPassword("password");
        testUser.setLastname("lastname");
        testUser.setPhone("phone");
        testUser.setBirthDay("1995-10-10");
        testUser.setTimeZone(1);
        testUser.setImgPath("emptyPath");

        userDaoImpl.insert(testUser);
    }

    //TODO need to be tested. throws Invalid conversion requested (java.sql.sqlexception)

    @Test
    public void whenFindByLogin_thenReturnUser() {

        //when
        User found = userDaoImpl.findByLogin("testLogin");

        //then
        assertThat(found.getName(), is(testUser.getName()));
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {

        //when
        User found = userDaoImpl.findByEmail("emailTest");

        //then
        assertThat(found.getName(), is(testUser.getName()));
    }

    @Test
    public void whenUpdate_thenSaveAndReturnUpdatedUser() {

        //when
        testUser.setPassword("newTestPassword");
        userDaoImpl.update(testUser);

        //then
        User found = userDaoImpl.findByEmail("emailTest");
        assertThat(found.getName(), is(testUser.getName()));
    }

    @After
    public void after() {
        userDaoImpl.delete(testUser);
    }

}