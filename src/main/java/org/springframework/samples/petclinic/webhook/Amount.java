package org.springframework.samples.petclinic.webhook;

public class Amount {
	
	 private String currency;
	 private float fees;
	 private float gross;
	 private float liquid;
	 private float refunds;
	 private float total;


	 // Getter Methods 

	 public String getCurrency() {
	  return currency;
	 }

	 public float getFees() {
	  return fees;
	 }

	 public float getGross() {
	  return gross;
	 }

	 public float getLiquid() {
	  return liquid;
	 }

	 public float getRefunds() {
	  return refunds;
	 }

	 public float getTotal() {
	  return total;
	 }

	 // Setter Methods 

	 public void setCurrency(String currency) {
	  this.currency = currency;
	 }

	 public void setFees(float fees) {
	  this.fees = fees;
	 }

	 public void setGross(float gross) {
	  this.gross = gross;
	 }

	 public void setLiquid(float liquid) {
	  this.liquid = liquid;
	 }

	 public void setRefunds(float refunds) {
	  this.refunds = refunds;
	 }

	 public void setTotal(float total) {
	  this.total = total;
	 }
	}
