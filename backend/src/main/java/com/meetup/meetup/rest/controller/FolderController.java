package com.meetup.meetup.rest.controller;

import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.service.FolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/folders")
public class FolderController {

    private static Logger log = LoggerFactory.getLogger(FolderController.class);

    @Autowired
    private FolderService folderService;

    @GetMapping
    @PreAuthorize("@folderAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<List<Folder>> getAllFolders(@PathVariable Integer userId) {
        log.debug("Trying to get all user folders");

        List<Folder> folders = folderService.getUserFolders(userId);

        log.debug("Send response body folders '{}' and status OK", folders.toString());

        return new ResponseEntity<>(folders, HttpStatus.OK);
    }

    @GetMapping("/{folderId}")
    @PreAuthorize("@folderAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<Folder> getFolderById(@PathVariable Integer userId, @PathVariable int folderId) {
        log.debug("Trying to get folder by folderId {}", folderId);

        Folder folder = folderService.getFolder(userId, folderId);

        log.debug("Send response body folder '{}' and status OK", folder.toString());

        return new ResponseEntity<>(folder, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("@folderAuthorization.isFolderCorrect(#userId, #folder)")
    public ResponseEntity<Folder> addFolder(@PathVariable Integer userId, @Valid @RequestBody Folder folder) {
        log.debug("Trying to save folder {}", folder.toString());

        Folder addedFolder = folderService.addFolder(folder);

        log.debug("Send response body saved folder '{}' and status OK", addedFolder.toString());

        return new ResponseEntity<>(addedFolder, HttpStatus.CREATED);
    }

    @PutMapping("/{folderId}")
    @PreAuthorize("@folderAuthorization.isFolderCorrect(#userId, #folderId, #folder)")
    public ResponseEntity<Folder> updateFolder(@PathVariable Integer userId, @PathVariable Integer folderId, @Valid @RequestBody Folder folder) {
        log.debug("Trying to update folder {}", folder.toString());

        Folder updatedFolder = folderService.updateFolder(folder);

        log.debug("Send response body updated folder '{}' and status OK", updatedFolder.toString());

        return new ResponseEntity<>(updatedFolder, HttpStatus.OK);
    }

    @DeleteMapping("/{folderId}")
    @PreAuthorize("@folderAuthorization.isUserCorrect(#userId)")
    public ResponseEntity<Folder> deleteFolder(@PathVariable Integer userId, @PathVariable Integer folderId) {
        log.debug("Trying to delete folder by id '{}'", folderId);

        Folder deletedFolder = folderService.deleteFolder(userId, folderId);

        log.debug("Send response body deleted folder '{}' and status OK", deletedFolder.toString());

        return new ResponseEntity<>(deletedFolder, HttpStatus.OK);
    }
}
