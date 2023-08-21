package com.supercoding.commerce03.service.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.supercoding.commerce03.service.S3.exception.ImageErrorCode;
import com.supercoding.commerce03.service.S3.exception.ImageException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.baseUrl}")
	private String baseUrl;

	public List<String> uploadFiles(List<MultipartFile> multipartFiles){

		List<String> urlList = new ArrayList<>();
		if (multipartFiles.isEmpty()) {
			throw new ImageException(ImageErrorCode.EMPTY_FILE);
		}
		multipartFiles.forEach(file -> {
			String fileName = createFileName(file.getOriginalFilename());
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(file.getSize());
			objectMetadata.setContentType(file.getContentType());

			try (InputStream inputStream = file.getInputStream()) {
				amazonS3.putObject(
						new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
								.withCannedAcl(CannedAccessControlList.PublicRead));
			} catch (IOException e) {
				throw new ImageException(ImageErrorCode.FAILED_UPLOAD);
			}
			urlList.add(baseUrl + fileName);
		});

		return urlList;
	}

	public void deleteFile(String url) {
		String fileName = url.replaceAll(baseUrl, "");
		amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
	}

	private String createFileName(String fileName) {
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	private String getFileExtension(String fileName) {
		try {
			String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg")) {
				return extension;
			}
			throw new ImageException(ImageErrorCode.NOT_IMAGE_EXTENSION);
		} catch (StringIndexOutOfBoundsException e) {
			throw new ImageException(ImageErrorCode.INVALID_FORMAT_FILE);
		}
	}

}
