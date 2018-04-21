package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PropertySource("classpath:strings.properties")
@RequestMapping("/api/profile/{id}")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @GetMapping("/folders")
    public List<Folder> getAllFolders(@PathVariable int id){
        List<Folder> userFolders = folderService.getUserFolders(id);
        return userFolders;
    }
}
