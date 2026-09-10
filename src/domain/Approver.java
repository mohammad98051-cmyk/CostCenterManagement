package domain;

import java.io.Serializable;

public interface Approver extends Serializable {
	public void approveRequest(PurchaseRequest request, String password);

	public void rejectRequest(PurchaseRequest request);
}
