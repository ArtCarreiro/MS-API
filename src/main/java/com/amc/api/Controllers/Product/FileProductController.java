package com.amc.api.Controllers.Product;

import com.amc.api.Entities.Product.FileProduct;
import com.amc.api.Services.Product.FileProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileProductController {

    @Autowired
    private FileProductService fileProductService;

    @GetMapping("/{uuid}")
    public ResponseEntity<FileProduct> getFileByUuid(@PathVariable("uuid") String fileUuid ) {
        FileProduct fileProduct = fileProductService.findFileByUuid(fileUuid);
        return fileProduct != null ? ResponseEntity.ok(fileProduct) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<FileProduct> updateFile(@PathVariable("uuid") String fileUuid, @RequestBody FileProduct fileProductData) {
        FileProduct fileProduct = fileProductService.updateFile(fileUuid , fileProductData);
        return fileProduct != null ? ResponseEntity.ok(fileProduct) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<FileProduct> deleteFile(@PathVariable("uuid") String fileUuid ) {
        boolean file = fileProductService.deleteFile(fileUuid);
        return file ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
    
}
