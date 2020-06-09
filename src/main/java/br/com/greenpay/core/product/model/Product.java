package br.com.greenpay.core.product.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.greenpay.core.partner.model.Partner;
import br.com.greenpay.core.system.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "PRODUCT")
public class Product extends BaseEntity {

	private String _id;
	private String name;
	private String subtitle;
	private String description;
	private String category;
	private String photo;

	@ManyToOne
	private Partner partner;

	@ManyToOne(cascade = CascadeType.ALL)
	private Price price;

	@ManyToOne(cascade = CascadeType.ALL)
	private Rating ratings;

	@ManyToOne(cascade = CascadeType.ALL)
	private Badge badge;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Tag> tags;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Feature> features;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Gallery> gallery;

}
