package br.com.greenpay.core.product.request;

import java.util.List;

import br.com.greenpay.core.product.response.BadgeResponse;
import br.com.greenpay.core.product.response.FeatureResponse;
import br.com.greenpay.core.product.response.GalleryResponse;
import br.com.greenpay.core.product.response.PriceResponse;
import br.com.greenpay.core.product.response.RatingResponse;
import br.com.greenpay.core.product.response.TagResponse;
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
