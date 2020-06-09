package br.com.greenpay.core.product;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.greenpay.core.product.dto.ProductDTO;
import br.com.greenpay.core.product.request.ProductRequest;
import br.com.greenpay.core.product.response.ProductResponse;
import br.com.greenpay.core.product.response.UpdatePhotoResponse;

@RestController
@Validated
@CrossOrigin
public class ProductController {

	private static final Log LOGGER = LogFactory.getLog(ProductController.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private HttpServletRequest request;

	@PostMapping(path = "/products", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@Valid @RequestBody ProductRequest productRequest) {
		productService.create(productRequest);
	}

	@GetMapping(path = "/products", produces = "application/json")
	public List<ProductResponse> findAllProducts() {
		return productService.findAllProducts();
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<?> findProductById(@PathVariable("id") Long productId) {
		return ResponseEntity.ok(productService.findProductById(productId));
	}

	@GetMapping(path = "/products_partner/{partnerId}", produces = "application/json")
	public List<ProductResponse> findAllProductsByPartner(@PathVariable("partnerId") Long partnerId) {
		return productService.findAllProductsByPartner(partnerId);
	}

	@DeleteMapping("/products/{id}")
	public void deleteProduct(@PathVariable("id") Long productId) {
		productService.deleteById(productId);
	}

	@PostMapping("/products/image")
	public ResponseEntity<?> updateImage(@Valid @RequestBody UpdatePhotoResponse updatePhotoResponse) {
		productService.updateImage(updatePhotoResponse);
		return ResponseEntity.ok("Url imagem atualizado.");
	}

	@GetMapping(path = "/products/query", produces = "application/json")
	public Map<String, String[]> findAllQuery(@RequestParam(value = "id") String id,
			@RequestParam(value = "status") String status) {
		Map<String, String[]> queryParameters = Collections.list(request.getParameterNames()).stream()
				.collect(Collectors.toMap(parameterName -> parameterName, request::getParameterValues));
		Set<String> chaves = queryParameters.keySet();
		for (String chave : chaves) {
			System.out.println(chave + queryParameters.get(chave));
		}

		for (String[] elemento : queryParameters.values()) {
			for (int i = 0; i < elemento.length; i++) {
				System.out.println(elemento[i]);
			}
		}
		return queryParameters;
	}

	@PutMapping("/products/{id}/update")
	public void updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable("id") Long productId) {
		productService.updateProduct(productId, productDTO);
	}

	@PutMapping("/products/{id}/remove")
	public void deleteImage(@Valid @RequestBody String photo, @PathVariable("id") Long productId) {
		productService.deleteImage(productId, photo);
	}

}
