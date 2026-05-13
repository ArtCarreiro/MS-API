package com.amc.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amc.api.dto.FileDTO;
import com.amc.api.entities.File;
import com.amc.api.interfaces.FileBO;


@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileBO fileBO;

    @PostMapping("/{uuid}")
    public ResponseEntity<File> uploadFile(@PathVariable("uuid") String productUuid, @RequestParam("file") MultipartFile data, @ModelAttribute FileDTO file) {
        File f = fileBO.save(productUuid, data, file);
        return f != null ? ResponseEntity.ok(f) : ResponseEntity.badRequest().build();
    }

}
