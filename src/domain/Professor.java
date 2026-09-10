package domain;

import java.util.ArrayList;

public class Professor extends User implements StatisticsCapable {
	private String firstname;
	private String lastname;
	private String personnelNumber;
	private Institute institute;
	private ArrayList<PurchaseRequest> requests = new ArrayList<>();

	public Professor(String username, String password, String firstname, String lastname, String personnelNumber) {
		super(username, password);
		this.firstname = firstname;
		this.lastname = lastname;
		this.personnelNumber = personnelNumber;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getPersonnelNumber() {
		return personnelNumber;
	}

	public Institute getInstitute() {
		return institute;
	}

	public void setInstitute(Institute institute) {
		this.institute = institute;
	}

	public ArrayList<PurchaseRequest> getRequests() {
		return requests;
	}

	public PurchaseRequest requestPurchase(String description, double price) {
		if (institute == null || price <= 0 || description == null || description.isBlank()) {
			return null;
		}

		PurchaseRequest request = new PurchaseRequest(description, price, this, institute.getCostCenter(), RequestStatus.OPEN);
		requests.add(request);
		return request;
	}

	public boolean deposit(double amount) {
		if (institute == null || amount <= 0) {
			return false;
		}

		institute.getCostCenter().deposit(amount);
		return true;
	}

	public double getTotalExpenses() {
		double total = 0;

		for (PurchaseRequest request : requests) {
			if (request.getStatus() == RequestStatus.APPROVED) {
				total += request.getPrice();
			}
		}

		return total;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setPersonnelNumber(String personnelNumber) {
	    this.personnelNumber = personnelNumber;
	}

	@Override
	public String toString() {
	    return getClass().getSimpleName()
	            + ": " + firstname + " " + lastname
	            + ", PersonalNR: " + personnelNumber
	            + " | Benutzername: " + getUsername() + "|";
	}
}
