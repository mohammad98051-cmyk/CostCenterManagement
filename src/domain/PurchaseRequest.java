package domain;

import java.io.Serializable;
import java.util.Date;

public class PurchaseRequest implements Serializable {

	private String description;
	private double price;
	private Date date;
	private RequestStatus status = RequestStatus.OPEN;
	private Professor requester;
	private Approver approver;
	private CostCenter costCenter;

	public PurchaseRequest(String description, double price, Professor requester, CostCenter costCenter, RequestStatus status) {
		this.description = description;
		this.price = price;
		this.requester = requester;
		this.costCenter = costCenter;
		this.status = status;
		this.date = new Date();
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}

	public Date getDate() {
		return date;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public Professor getRequester() {
		return requester;
	}

	public Approver getApprover() {
		return approver;
	}

	public CostCenter getCostCenter() {
		return costCenter;
	}

	public void setApprover(Approver approver) {
		this.approver = approver;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

}
