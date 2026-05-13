package com.amc.api.services;


import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
public class SupabaseStorageService {

    @Value("${supabase.storage.bucket}")
    private String bucket;

    @Value("${supabase.project-ref}")
    private String projectRef;

    private final S3Client s3Client;

    public SupabaseStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadProductImage(String productUuid, MultipartFile file) throws IOException {
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Arquivo precisa ser uma imagem.");
        }

        String extension = getExtension(file.getOriginalFilename());
        String key = "products/" + productUuid + "/" + UUID.randomUUID() + extension;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .cacheControl("public, max-age=31536000")
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return "https://" + projectRef + ".supabase.co/storage/v1/object/public/"
                + bucket + "/" + key;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf("."));
    }
}

