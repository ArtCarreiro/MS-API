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
    public ResponseEntity<FileProduct> getFileByUuid(@PathVariable("uuid") String FileUuid ) {
        FileProduct FileProduct = fileProductService.findFileByUuid(FileUuid);
        return FileProduct != null ? ResponseEntity.ok(FileProduct) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<FileProduct> updateFile(@PathVariable("uuid") String FileUuid, @RequestBody FileProduct fileProductData) {
        FileProduct FileProduct = fileProductService.updateFile(FileUuid , fileProductData);
        return FileProduct != null ? ResponseEntity.ok(FileProduct) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<FileProduct> deleteFile(@PathVariable("uuid") String FileUuid ) {
        boolean File = fileProductService.deleteFile(FileUuid);
        return File ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
}
