package domain;

public class Dean extends Professor implements Approver {
	private Faculty faculty;

	public Dean(String username, String password, String firstname, String lastname, String personnelNumber) {
		super(username, password, firstname, lastname, personnelNumber);
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public boolean facultyDeposit(double amount) {
		if (faculty == null || amount <= 0) {
			return false;
		}

		if (faculty.getInstitutes().isEmpty()) {
			return false;
		}

		double facultyShare = amount / 2;
		double institutesShare = amount / 2;
		double sharePerInstitute = institutesShare / faculty.getInstitutes().size();

		faculty.getFacultyCostCenter().deposit(facultyShare);

		for (Institute institute : faculty.getInstitutes().values()) {
			institute.getCostCenter().deposit(sharePerInstitute);
		}
		return true;
	}

	public PurchaseRequest requestFacultyPurchase(String description, double price) {
		if (faculty == null || price <= 0 || description == null || description.isBlank()) {
			return null;
		}
		
		if (!faculty.debitFacultyCostCenter(price)) {
			return null;
		}
		PurchaseRequest request = new PurchaseRequest(description, price, this, faculty.getFacultyCostCenter(), RequestStatus.APPROVED);
		this.getRequests().add(request);
		return request;
	}

	@Override
	public void approveRequest(PurchaseRequest request, String password) {
		if (!login(password)) {
			return;
		}
		
		if(request == null || request.getRequester() == null) {
			return;
		}

		if (getPersonnelNumber().equals(request.getRequester().getPersonnelNumber())) {
			return;
		}
		
		if(!(request.getRequester() instanceof InstituteHead)) {
			return;
		}
		
		if(request.getStatus() != RequestStatus.OPEN) {
			return;
		}

		if (faculty.debitInstituteCostCenter(request.getCostCenter(), request.getPrice())) {
			request.setApprover(this);
			request.setStatus(RequestStatus.APPROVED);
		}

	}

	@Override
	public void rejectRequest(PurchaseRequest request) {
		
		if(request == null || request.getRequester() == null) {
			return;
		}
		
		if (getPersonnelNumber().equals(request.getRequester().getPersonnelNumber())) {
			return;
		}
		
		if(!(request.getRequester() instanceof InstituteHead)) {
			return;
		}
		
		if(request.getStatus() != RequestStatus.OPEN) {
			return;
		}

		request.setApprover(this);
		request.setStatus(RequestStatus.REJECTED);
	}
}
