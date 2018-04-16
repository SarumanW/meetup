package com.meetup.meetup.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.vm.DetailedProfile;
import com.meetup.meetup.service.vm.FictUser;
import com.meetup.meetup.service.vm.MinimalProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileService {
    private final List<User> users;

//    @Autowired
//    private UserDao userDao;

    private final Path PROFILES_FILE = Paths.get(this.getClass().getResource("/profiles.json").toURI());

    public ProfileService() throws IOException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper();
        //List<FictUser> fictUsers = objectMapper.readValue(PROFILES_FILE.toFile(), objectMapper.getTypeFactory().constructCollectionType(List.class, FictUser.class));
        List<FictUser> fictUsers = objectMapper.readValue(PROFILES_FILE.toFile(), new TypeReference<List<FictUser>>() {});
        users = new ArrayList<>();
        for (FictUser fict : fictUsers)
            users.add(new User(fict));
    }

    public User get(String login) {
        for (User user : users)
            if (login.equals(user.getLogin()))
                return user;
        return null;
        //return userDao.findByLogin(login);
    }

    public MinimalProfile minimal(String username) {
        User user = get(username);
        return user == null ? null : new MinimalProfile(user);
    }

    public DetailedProfile detailed(String username) {
        User user = get(username);
        return user == null ? null : new DetailedProfile(user);
    }
}
