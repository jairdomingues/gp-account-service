package br.com.greenpay.core.product;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import br.com.greenpay.core.partner.model.Partner;
import br.com.greenpay.core.partner.repository.PartnerRepository;
import br.com.greenpay.core.product.dto.ProductDTO;
import br.com.greenpay.core.product.model.Gallery;
import br.com.greenpay.core.product.model.Product;
import br.com.greenpay.core.product.repository.ProductRepository;
import br.com.greenpay.core.product.request.ProductRequest;
import br.com.greenpay.core.product.response.ProductResponse;
import br.com.greenpay.core.product.response.UpdatePhotoResponse;
import br.com.greenpay.core.system.exception.CustomGenericNotFoundException;

@Service
@Transactional
public class ProductService {

	private static final Log LOGGER = LogFactory.getLog(ProductService.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PartnerRepository partnerRepository;

	public void create(ProductRequest productRequest) {
		Partner partner = partnerRepository.findById(productRequest.getPartnerId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner is not found."));
		Product product = this.convertToProduct(productRequest);
		product.setPartner(partner);
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

	public ProductDTO findProductById(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Product is not found."));
		return convertToProductDTO(product);
	}

	public void deleteById(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Product is not found."));
		productRepository.delete(product);
	}

	private ProductResponse convertToProductResponse(Product product) {
		ModelMapper modelMapper = new ModelMapper();
		ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
		productResponse.setPartnerName(product.getPartner().getFantasia());
		return productResponse;
	}

	private ProductDTO convertToProductDTO(Product product) {
		ModelMapper modelMapper = new ModelMapper();
		// String partnerName =
		// partnertRepository.findById(product.getPartnerId()).get().getFantasia();
		ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
		// productDTO.setPartnerName(partnerName);
		return productDTO;
	}

	private Product convertToProduct(ProductRequest productRequest) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(productRequest, Product.class);
	}

	public void updateImage(UpdatePhotoResponse updatePhotoResponse) {
		Product product = productRepository.findById(updatePhotoResponse.getProductId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Product is not found."));
		List<Gallery> lista = product.getGallery() != null && !product.getGallery().isEmpty() ? product.getGallery()
				: new ArrayList<Gallery>();
		if (product.getPhoto() == null) {
			product.setPhoto(updatePhotoResponse.getUrlPhoto());
		} else {
			Gallery g = new Gallery();
			g.setPhoto(updatePhotoResponse.getUrlPhoto());
			lista.add(g);
			product.setGallery(lista);
		}
		productRepository.save(product);
	}
	
	@Autowired
	ObjectMapper objectMapper;

	public void updateProduct(Long productId, ProductDTO productDTO)  {
		Partner partner = partnerRepository.findById(productDTO.getPartner().getId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner is not found."));

		Product product = this.convertToEntity(productDTO);
		product.setId(productId);
		product.setPartner(partner);
//		product = productRepository.findById(productId)
//				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Product is not found."));

		productRepository.save(product);
		
		
//		ObjectReader objectReader = objectMapper.readerForUpdating(productOld);
//		Product updatedEmployee = objectReader.readValue(productDTO);
//		
////		ModelMapper modelMapper = new ModelMapper();
////		productOld = modelMapper.map(productDTO, Product.class);
//
//		Product updatedUser = objectMapper.readerForUpdating(productOld).readValue(productDTO);
//		
//		Product product = this.convertToEntity(productDTO);
////		productOld.set_id(productDTO.get_id());
//		product.setPartner(partner);
//		productRepository.save(product);
	}

	public void deleteImage(long productId, String photo) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Product is not found."));

		if (photo == null) {
			// TODO remover a imagem no storage do google storage
			product.setPhoto(null);
			productRepository.save(product);
		} else {
			product.getGallery().parallelStream().filter(x -> x.getPhoto().equalsIgnoreCase(photo)).findFirst()
					.map(p -> {
						product.getGallery().remove(p);
						return p;
					});
		}
	}

	private Product convertToProduct(ProductDTO productDTO) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(productDTO, Product.class);
	}
	
	private Product convertToEntity(ProductDTO productDTO)  {
		ModelMapper modelMapper = new ModelMapper();
		Product product = modelMapper.map(productDTO, Product.class);
//	    if (productDTO.getId() != null) {
//	        Product oldPost = productRepository.findById(productDTO.getId())
//					.orElseThrow(() -> new CustomGenericNotFoundException("Error: Product is not found."));
//	        product.setId(oldPost.getId());
//	    }
	    return product;
	}
	
}
