package br.com.greenpay.core.upload.service;

import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

	public void save(MultipartFile file, Long productId);

}
