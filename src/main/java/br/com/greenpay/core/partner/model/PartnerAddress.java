package br.com.greenpay.core.partner.model;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
@Table(name = "PARTNER_ADDRESS")
public class PartnerAddress extends BaseEntity {

	private String number;
	private String street;
	private String complement;
	private String neighborhood;
	private String city;
	private String province;
	private String zip;
	private String country;

	@Column(precision=8, scale=2) 
	private Float lat;
	
	@Column(precision=8, scale=2) 	
	private Float lng;

	private String referencePoint;
	private Boolean defaults;

	
	@Transient
	public String getAddress() {
		return this.street + ", " + this.number + ", " + this.neighborhood + ", " + this.zip + this.city + ", "
				+ this.province;
	}

}
