package br.com.greenpay.core.product.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.greenpay.core.partner.dto.PartnerDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String _id;
	private String name;
	private String subtitle;
	private String description;
	private String category;
	private String photo;
	private PartnerDTO partner;
	private PriceDTO price;
	private RatingDTO ratings;
	private BadgeDTO badge;
	private List<TagDTO> tags;
	private List<FeatureDTO> features;
	private List<GalleryDTO> gallery;
	
	private Integer version;
	protected String createUser;
	private String changeUser;
	protected Date createDate;
	private Date changeDate;
	private String uuid;
	

}
