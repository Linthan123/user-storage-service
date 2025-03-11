package com.example.user_storage_service;

import com.example.user_storage_service.service.StorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class StorageServiceTest {

	@Test
	void testSearchFiles() {
		// ✅ Mock S3Client
		S3Client mockS3Client = Mockito.mock(S3Client.class);
		String bucketName = "test-bucket";

		// ✅ Pass correct parameters to StorageService
		StorageService service = new StorageService(mockS3Client, bucketName);

		ListObjectsV2Response response = ListObjectsV2Response.builder()
				.contents(
						S3Object.builder().key("sandy/logistics-report.pdf").build(),
						S3Object.builder().key("sandy/logistics-data.csv").build())
				.build();

		// ✅ Use explicit matcher for ListObjectsV2Request
		when(mockS3Client.listObjectsV2(Mockito.any(ListObjectsV2Request.class)))
				.thenReturn(response);

		List<String> result = service.searchFiles("sandy", "logistics");

		assertEquals(2, result.size());
		assertEquals("sandy/logistics-report.pdf", result.get(0));
	}
}

// package com.example.user_storage_service;

// import com.example.user_storage_service.service.StorageService;

// import software.amazon.awssdk.services.s3.S3Client;
// import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.test.context.SpringBootTest;

// import java.io.IOException;
// import java.nio.file.*;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// @SpringBootTest
// class StorageServiceTest {

// private StorageService storageService;

// @Value("${file.upload-dir}") // Load from application.properties
// private String uploadDirPath;

// private Path uploadDir;

// @BeforeEach
// void setUp() throws IOException {
// uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
// Files.createDirectories(uploadDir);
// storageService = new StorageService(uploadDirPath);

// // Create some test files
// Files.write(uploadDir.resolve("logistics-report.pdf"), "Test
// Data".getBytes());
// Files.write(uploadDir.resolve("logistics-data.csv"), "Test Data".getBytes());
// }

// @Test
// void testSearchFiles() {
// S3Client mockS3Client = Mockito.mock(S3Client.class);
// String bucketName = "test-bucket";
// StorageService storageService = new StorageService(mockS3Client, bucketName);
// // Ensure constructor matches

// ListObjectsV2Response response = ListObjectsV2Response.builder()
// .contents(
// S3Object.builder().key("sandy/logistics-report.pdf").build(),
// S3Object.builder().key("sandy/logistics-data.csv").build())
// .build();

// when(mockS3Client.listObjectsV2(Mockito.any(ListObjectsV2Request.class)))
// .thenReturn(response);

// List<String> result = storageService.searchFiles("sandy", "logistics"); //
// Ensure correct method call

// assertEquals(2, result.size());
// assertTrue(result.get(0).contains("logistics-report.pdf"));
// assertTrue(result.get(1).contains("logistics-data.csv"));
// }

// }
