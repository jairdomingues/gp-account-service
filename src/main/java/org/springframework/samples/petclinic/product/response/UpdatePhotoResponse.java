package org.springframework.samples.petclinic.product.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePhotoResponse {

	private Long productId;
	private String urlPhoto;

}
