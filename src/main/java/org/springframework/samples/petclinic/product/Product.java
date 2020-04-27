package org.springframework.samples.petclinic.product;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.samples.petclinic.system.BaseEntity;

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
	private Long partnerId;
	
	@ManyToOne
	private Price price;
	
	@ManyToOne
	private Rating ratings;
	
	@ManyToOne
	private Badge badge;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Tag> tags;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Feature> features;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Gallery> gallery;
	
}
