package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PropertySource("classpath:strings.properties")
@RequestMapping("/api/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @GetMapping("/")
    public List<Folder> getAllFolders(){
        return folderService.getUserFolders();
    }

    @GetMapping("/{folderId}")
    public Folder getFolderById(@PathVariable int folderId){
        return folderService.getFolder(folderId);
    }

    @PostMapping("/add")
    public ResponseEntity<Folder> addFolder(@Valid @RequestBody Folder folder) {
        return new ResponseEntity<>(folderService.addFolder(folder), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Folder> updateEvent(@Valid @RequestBody Folder folder) {
        return new ResponseEntity<>(folderService.updateFolder(folder), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Folder> deleteFolder(@Valid @RequestBody Folder folder) {
        return new ResponseEntity<>(folderService.deleteFolder(folder), HttpStatus.OK);
    }
}
