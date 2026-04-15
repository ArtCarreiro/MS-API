package com.amc.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amc.api.interfaces.FileBO;
import com.amc.api.repositories.FileRepository;


@RestController
@RequestMapping("/files")
public class FileController {
    
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileBO fileBO;

    

}
