package br.com.greenpay.core.webhook;

import java.util.ArrayList;

public class Payment {

	_links _linksObject;
	AcquirerDetails AcquirerDetailsObject;
	Amount AmountObject;
	private String createdAt;
	private boolean delayCapture;
	ArrayList<Object> events = new ArrayList<Object>();
	ArrayList<Object> fees = new ArrayList<Object>();
	FundingInstrument FundingInstrumentObject;
	private String id;
	private float installmentCount;
	ArrayList<Object> receivers = new ArrayList<Object>();
	private String status;
	private String updatedAt;

	// Getter Methods

	public _links get_links() {
		return _linksObject;
	}

	public AcquirerDetails getAcquirerDetails() {
		return AcquirerDetailsObject;
	}

	public Amount getAmount() {
		return AmountObject;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public boolean getDelayCapture() {
		return delayCapture;
	}

	public FundingInstrument getFundingInstrument() {
		return FundingInstrumentObject;
	}

	public String getId() {
		return id;
	}

	public float getInstallmentCount() {
		return installmentCount;
	}

	public String getStatus() {
		return status;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	// Setter Methods

	public void set_links(_links _linksObject) {
		this._linksObject = _linksObject;
	}

	public void setAcquirerDetails(AcquirerDetails acquirerDetailsObject) {
		this.AcquirerDetailsObject = acquirerDetailsObject;
	}

	public void setAmount(Amount amountObject) {
		this.AmountObject = amountObject;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setDelayCapture(boolean delayCapture) {
		this.delayCapture = delayCapture;
	}

	public void setFundingInstrument(FundingInstrument fundingInstrumentObject) {
		this.FundingInstrumentObject = fundingInstrumentObject;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInstallmentCount(float installmentCount) {
		this.installmentCount = installmentCount;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

}