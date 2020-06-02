package org.springframework.samples.petclinic.product.request;

import java.util.List;

import org.springframework.samples.petclinic.product.response.BadgeResponse;
import org.springframework.samples.petclinic.product.response.FeatureResponse;
import org.springframework.samples.petclinic.product.response.GalleryResponse;
import org.springframework.samples.petclinic.product.response.PriceResponse;
import org.springframework.samples.petclinic.product.response.RatingResponse;
import org.springframework.samples.petclinic.product.response.TagResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

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
