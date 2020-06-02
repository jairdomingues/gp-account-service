package org.springframework.samples.petclinic.product.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.system.model.BaseEntity;

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
@Table(name = "TAG")
public class Tag extends BaseEntity {

	@Column(name = "text")
	private String  text;

}

/*
{
    '_id': '5a9ae2106518248b68251fdf',
    'name': 'Wireless Bluetooth V4.0 Portable Speaker with HD Sound and Bass',
    'subtitle': 'Estabelecimento Numero Um',
    'description': 'Lorem ipsum dolor sit amet, et nec putent quodsi, admodum assentior ad duo. Pri ad sapientem ocurreret incorrupte',
    'category': 'speaker',
    'tags': [
      'sunt',
      'sunt',
      'culpa'
    ],
    'price': {
      'sale': 32,
      'previous': 54
    },
    'ratings': {
      'rating': 3.86,
      'ratingCount': 26
    },
    'features': [
      'aliquip aliquip',
      'nulla laboris',
      'pariatur consequat'
    ],
    'photo': '../../../../assets/images/products/speaker-1.jpg',
    'gallery': [
      '../../../../assets/images/products/speaker-1.jpg',
      '../../../../assets/images/products/speaker-2.jpg'
    ],
    'badge': {
      'text': '20% TPK',
      'color': '#0D47A1'
    }
  },
*/

