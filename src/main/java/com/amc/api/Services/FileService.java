package com.amc.api.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amc.api.dto.FileDTO;
import com.amc.api.entities.File;
import com.amc.api.interfaces.FileBO;
import com.amc.api.repositories.FileRepository; 

@Service
public class FileService implements FileBO {
    
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${aws.s3.bucket-name}")
    private String path;

    @Override
    public File save(FileDTO data) {
        try {
            File file = mapper.map(data, File.class);
            validateFile(file);
            file.setPath(path + "/" + file.getName());
            return fileRepository.save(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void validateFile(File file) {
        if (file == null)
            throw new IllegalArgumentException("File data cannot be null");
        if (fileRepository.findByUuid(file.getUuid()) == null) 
             throw new RuntimeException("File not found");
    }

}
