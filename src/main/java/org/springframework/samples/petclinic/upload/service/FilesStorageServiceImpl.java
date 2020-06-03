package org.springframework.samples.petclinic.upload.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.product.model.Gallery;
import org.springframework.samples.petclinic.product.model.Product;
import org.springframework.samples.petclinic.product.repository.ProductRepository;
import org.springframework.samples.petclinic.product.response.UpdatePhotoResponse;
import org.springframework.samples.petclinic.system.exception.CustomGenericNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

	@Autowired
	ProductRepository productRepository;
	
	@Override
	public void save(MultipartFile file, Long productId) {

		try {
			storage(file, productId);
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}

	}

	private void storage(MultipartFile file, Long productId) throws IOException {

	}

	private void checkFileExtension(String fileName) throws ServletException {
		if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
			String[] allowedExt = { ".jpg", ".jpeg", ".png", ".gif" };
			for (String ext : allowedExt) {
				if (fileName.endsWith(ext)) {
					return;
				}
			}
			throw new ServletException("file must be an image");
		}
	}
	
	private void updateImage(UpdatePhotoResponse updatePhotoResponse) {
		Product product = productRepository.findById(updatePhotoResponse.getProductId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Product is not found."));
		List<Gallery> lista = product.getGallery() != null && !product.getGallery().isEmpty()?product.getGallery():new ArrayList<Gallery>();
		if (product.getPhoto()==null) {
			product.setPhoto(updatePhotoResponse.getUrlPhoto());
		} else {
			Gallery g = new Gallery();
			g.setPhoto(updatePhotoResponse.getUrlPhoto());
			lista.add(g);
			product.setGallery(lista);
		}
		productRepository.save(product);
	}
	
}
