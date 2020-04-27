package org.springframework.samples.petclinic.product;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.system.CustomGenericNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

	private static final Log LOGGER = LogFactory.getLog(ProductService.class);

	@Autowired
	private ProductRepository productRepository;

	public List<ProductResponse> findAllProducts() {
		List<Product> products = (List<Product>) productRepository.findAll();
		return products.stream().sorted(Comparator.comparing(Product::getCreateDate).reversed())
				.map(this::convertToProductResponse).collect(Collectors.toList());
	}

	public List<ProductResponse> findAllProductsByPartner(Long partnerId) {
		List<Product> products = (List<Product>) productRepository.findAllProductsByPartner(partnerId);
		return products.stream().sorted(Comparator.comparing(Product::getCreateDate).reversed())
				.map(this::convertToProductResponse).collect(Collectors.toList());
	}

	public ProductResponse findProductById(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Product is not found."));
		return convertToProductResponse(product);
	}

	private ProductResponse convertToProductResponse(Product product) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(product, ProductResponse.class);
	}

}
