package org.springframework.samples.petclinic.system;

import java.beans.Transient;

import javax.persistence.Entity;
import javax.persistence.Table;

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
@Table(name = "ADDRESS")
public class Address extends BaseEntity {

	private String number;
	private String street;
	private String complement;
	private String neighborhood;
	private String city;
	private String province;
	private String zip;
	private String country;
	private String lat;
	private String lon;
	private String referencePoint;
	private Boolean defaults;

	@Transient
	public String getAddress() {
		return this.street + ", " + this.number + ", " + this.neighborhood + ", " + this.zip + this.city + ", "
				+ this.province;
	}
	
}
