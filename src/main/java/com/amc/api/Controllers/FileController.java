package com.amc.api.Controllers;

import com.amc.api.Entities.File;
import com.amc.api.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/{uuid}")
    public ResponseEntity<File> getFileByUuid(@PathVariable("uuid") String FileUuid ) {
        File File = fileService.findFileByUuid(FileUuid);
        return File != null ? ResponseEntity.ok(File) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<File> updateFile(@PathVariable("uuid") String FileUuid, @RequestBody File FileData) {
        File File = fileService.updateFile(FileUuid ,FileData);
        return File != null ? ResponseEntity.ok(File) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<File> deleteFile(@PathVariable("uuid") String FileUuid ) {
        boolean File = fileService.deleteFile(FileUuid);
        return File ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
}
