package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.vm.DetailedProfile;
import com.meetup.meetup.service.vm.MinimalProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileService {
//    private final List<Profile> profiles;

    @Autowired
    private UserDao userDao;

//    private final Path PROFILES_FILE = Paths.get(this.getClass().getResource("/profiles.json").toURI());
//
//    public ProfileService() throws IOException, URISyntaxException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        profiles = objectMapper.readValue(PROFILES_FILE.toFile(), new TypeReference<List<Profile>>() {});
//    }

    public User get(String login) {
        return userDao.findByLogin(login);
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
