package org.springframework.samples.petclinic.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping(path = "/products", produces = "application/json")
	public List<ProductResponse> findAllProducts() {
		return productService.findAllProducts();
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<?> findProductById(@PathVariable("id") Long productId) {
		return ResponseEntity.ok(productService.findProductById(productId));
	}
	
}
