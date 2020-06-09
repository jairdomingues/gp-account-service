package br.com.greenpay.core.upload.service;

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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;

import br.com.greenpay.core.product.model.Gallery;
import br.com.greenpay.core.product.model.Product;
import br.com.greenpay.core.product.repository.ProductRepository;
import br.com.greenpay.core.product.response.UpdatePhotoResponse;
import br.com.greenpay.core.system.exception.CustomGenericNotFoundException;

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

		InputStream is = null;
		try {
			is = getClass().getResourceAsStream("/green-pay-v1-63f74ec603a0.json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		StorageOptions storageOptions = StorageOptions.newBuilder().setProjectId("green-pay-v1 ")
				.setCredentials(GoogleCredentials.fromStream(is)).build();
		Storage storage = storageOptions.getService();

		try {
			checkFileExtension(file.getName());
		} catch (ServletException e) {
			e.printStackTrace();
		}
		DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
		DateTime dt = DateTime.now(DateTimeZone.UTC);
		String dtString = dt.toString(dtf);
		final String fileName = file.getName() + dtString;

		@SuppressWarnings("deprecation")
		BlobInfo blobInfo = storage.create(
				BlobInfo.newBuilder("green-pay-v1.appspot.com", fileName).setContentType("image/png")
						// Modify access list to allow all users with link to read file
						.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build(),
				file.getInputStream());

		System.out.println(blobInfo.getMediaLink() + " / " + productId);
		UpdatePhotoResponse updatePhotoResponse = new UpdatePhotoResponse();
		updatePhotoResponse.setProductId(productId);
		updatePhotoResponse.setUrlPhoto(blobInfo.getMediaLink());
		this.updateImage(updatePhotoResponse);
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
