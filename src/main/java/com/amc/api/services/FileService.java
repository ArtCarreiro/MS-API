package com.amc.api.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private SupabaseStorageService supabaseService;

    @Override
    public File save(String productUuid, MultipartFile data, FileDTO file) {
        try {
            File f = new File();
            mapper.map(file, f);
            String supabaseUrl = supabaseService.uploadProductImage(productUuid, data);
            if (supabaseUrl != null) {
                f.setPath(supabaseUrl);
                return fileRepository.save(f);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
