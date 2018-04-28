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

    @GetMapping
    public ResponseEntity<List<Folder>> getAllFolders(){
        return new ResponseEntity<>(folderService.getUserFolders(), HttpStatus.OK);
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<Folder> getFolderById(@PathVariable int folderId){
        return new ResponseEntity<>(folderService.getFolder(folderId), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Folder> addFolder(@Valid @RequestBody Folder folder) {
        return new ResponseEntity<>(folderService.addFolder(folder), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Folder> updateEvent(@Valid @RequestBody Folder folder) {
        return new ResponseEntity<>(folderService.updateFolder(folder), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Folder> deleteFolder(@Valid @RequestBody Folder folder) {
        return new ResponseEntity<>(folderService.deleteFolder(folder), HttpStatus.OK);
    }
}
