package domain;

public class InstituteHead extends Professor implements Approver {

	public InstituteHead(String username, String password, String firstname, String lastname, String personnelNumber) {
		super(username, password, firstname, lastname, personnelNumber);
	}

	@Override
	public void approveRequest(PurchaseRequest request, String password) {

		if (!login(password)) {
			return;
		}
		
		if(request == null || request.getRequester() == null ) {
			return;
		}

		if (getPersonnelNumber().equals(request.getRequester().getPersonnelNumber())) {
			return;
		}
		
		if(request.getRequester().getInstitute() != getInstitute()) {
			return;
		}
		
		if(request.getStatus() != RequestStatus.OPEN) {
			return;
		}

		if (getInstitute().getFaculty().debitInstituteCostCenter(request.getCostCenter(), request.getPrice())) {
			request.setApprover(this);
			request.setStatus(RequestStatus.APPROVED);
		}

	}

	@Override
	public void rejectRequest(PurchaseRequest request) {
		
		if(request == null || request.getRequester() == null ) {
			return;
		}
		
		if (getPersonnelNumber().equals(request.getRequester().getPersonnelNumber())) {
			return;
		}
		
		if(request.getRequester().getInstitute() != getInstitute()) {
			return;
		}
		
		if(request.getStatus() != RequestStatus.OPEN) {
			return;
		}

		request.setApprover(this);
		request.setStatus(RequestStatus.REJECTED);
	}

}
