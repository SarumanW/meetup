package com.meetup.meetup.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetup.meetup.service.vm.DetailedProfile;
import com.meetup.meetup.service.vm.MinimalProfile;
import com.meetup.meetup.service.vm.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class ProfileService {
    private final List<Profile> profiles;


    private final Path PROFILES_FILE = Paths.get(this.getClass().getResource("/profiles.json").toURI());

    public ProfileService() throws IOException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper();
        profiles = objectMapper.readValue(PROFILES_FILE.toFile(), new TypeReference<List<Profile>>() {});
    }

    protected Profile get(String username) {
        for (Profile profile : profiles)
            if(profile.getUsername().equals(username))
                return profile;
        return null;
    }

    public MinimalProfile minimal(String username) {
        Profile profile = get(username);
        return profile == null ? null : new MinimalProfile(profile);
    }

    public DetailedProfile detailed(String username) {
        Profile profile = get(username);
        return profile == null ? null : new DetailedProfile(profile);
    }
}
