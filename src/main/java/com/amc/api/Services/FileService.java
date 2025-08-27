package com.amc.api.Services;

import com.amc.api.Entities.File;
import com.amc.api.Repositories.FileRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ModelMapper modelMapper;

    public File findFileByUuid(String FileUuid) {
        try {
            return fileRepository.findByUuid(FileUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public File updateFile(String FileUuid, File newFile) {
        modelMapper.typeMap(File.class, File.class)
                .addMappings(mapper -> mapper.skip(File::setUuid));
        try {
            if (newFile != null) {
                File newFileData = fileRepository.findByUuid(FileUuid);
                modelMapper.map(newFile, newFileData);
                return fileRepository.save(newFileData);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Transactional
    public boolean deleteFile(String FileUuid) {
        try {
            File FileDeleted = fileRepository.findByUuid(FileUuid);
            fileRepository.delete(FileDeleted);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
