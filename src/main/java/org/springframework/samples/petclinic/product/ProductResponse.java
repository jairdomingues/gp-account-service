package org.springframework.samples.petclinic.product;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

	private Long id;
	private String _id;
	private Long partnerId;
	private String partnerName;
	private String name;
	private String subtitle;
	private String description;
	private String category;
	private String photo;
	private PriceResponse price;
	private RatingResponse ratings;
	private BadgeResponse badge;
	private List<TagResponse> tags;
	private List<FeatureResponse> features;
	private List<GalleryResponse> gallery;

}
