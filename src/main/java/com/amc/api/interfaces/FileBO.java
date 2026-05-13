package com.amc.api.interfaces;

import org.springframework.web.multipart.MultipartFile;

import com.amc.api.dto.FileDTO;
import com.amc.api.entities.File;

public interface FileBO {
    
    File save(String productUuid, MultipartFile data, FileDTO file);


}
