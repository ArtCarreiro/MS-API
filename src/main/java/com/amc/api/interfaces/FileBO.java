package com.amc.api.interfaces;

import com.amc.api.dto.FileDTO;
import com.amc.api.entities.File;

public interface FileBO {
    
    File save(FileDTO data);

    void validateFile(File file);

}
