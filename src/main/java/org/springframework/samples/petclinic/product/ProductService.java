package org.springframework.samples.petclinic.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.partner.repository.PartnerRepository;
import org.springframework.samples.petclinic.product.model.Gallery;
import org.springframework.samples.petclinic.product.model.Product;
import org.springframework.samples.petclinic.product.repository.ProductRepository;
import org.springframework.samples.petclinic.product.request.ProductRequest;
import org.springframework.samples.petclinic.product.response.ProductResponse;
import org.springframework.samples.petclinic.product.response.UpdatePhotoResponse;
import org.springframework.samples.petclinic.system.exception.CustomGenericNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

	private static final Log LOGGER = LogFactory.getLog(ProductService.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PartnerRepository partnertRepository;

	public void create(ProductRequest productRequest) {
		Product product = this.convertToProduct(productRequest);
		productRepository.save(product);
	}

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

	public void deleteById(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Product is not found."));
		productRepository.delete(product);
	}

	private ProductResponse convertToProductResponse(Product product) {
		ModelMapper modelMapper = new ModelMapper();
		String partnerName = partnertRepository.findById(product.getPartnerId()).get().getFantasia();
		ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
		productResponse.setPartnerName(partnerName);
		return productResponse;
	}

	private Product convertToProduct(ProductRequest productRequest) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(productRequest, Product.class);
	}

	public void updateImage(UpdatePhotoResponse updatePhotoResponse) {
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
