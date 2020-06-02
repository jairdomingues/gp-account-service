package org.springframework.samples.petclinic.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.product.request.ProductRequest;
import org.springframework.samples.petclinic.product.response.ProductResponse;
import org.springframework.samples.petclinic.product.response.UpdatePhotoResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping(path = "/products_partner/{idPartner}", produces = "application/json")
	public List<ProductResponse> findAllProductsByPartner(@PathVariable("idPartner") Long partnerId) {
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

}
