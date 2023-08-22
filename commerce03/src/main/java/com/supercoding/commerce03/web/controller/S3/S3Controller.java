package com.supercoding.commerce03.web.controller.S3;

import com.supercoding.commerce03.service.S3.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("//v1/api/image")
public class S3Controller {

	private final S3Service s3Service;

	@PostMapping("/uploadFiles")
	public ResponseEntity<List<String>> uploadFiles(
			@RequestPart List<MultipartFile> multipartFile
	){
			return ResponseEntity.ok(
					s3Service.uploadFiles(multipartFile)
			);
	}

}
