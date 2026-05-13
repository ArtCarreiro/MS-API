package com.amc.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ExtendWith(MockitoExtension.class)
class SupabaseStorageServiceTest {

    @Mock
    private S3Client s3Client;

    @Test
    void uploadProductImageShouldSaveTestImageOnS3() throws Exception {
        SupabaseStorageService service = new SupabaseStorageService(s3Client);
        ReflectionTestUtils.setField(service, "bucket", "test-bucket");
        ReflectionTestUtils.setField(service, "projectRef", "test-project");

        byte[] pngImage = new byte[] {
                (byte) 0x89, 0x50, 0x4E, 0x47,
                0x0D, 0x0A, 0x1A, 0x0A
        };
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "product-test.png",
                "image/png",
                pngImage);

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        String result = service.uploadProductImage("product-uuid", image);

        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client).putObject(requestCaptor.capture(), any(RequestBody.class));

        PutObjectRequest request = requestCaptor.getValue();
        assertEquals("test-bucket", request.bucket());
        assertTrue(request.key().startsWith("products/product-uuid/"));
        assertTrue(request.key().endsWith(".png"));
        assertEquals("image/png", request.contentType());
        assertEquals("public, max-age=31536000", request.cacheControl());

        assertEquals(
                "https://test-project.supabase.co/storage/v1/object/public/test-bucket/" + request.key(),
                result);
    }
}
