package domain;

import java.util.HashMap;
import java.io.Serializable;

public class Faculty implements Serializable {
	private String name;
	private HashMap<String, Institute> institutes;
	private Dean dean;
	private CostCenter facultyCostCenter;
	private static final double MAX_TOTAL_DEBT = 10000.00;

	public Faculty(String name) {
		this.name = name;
		this.facultyCostCenter = new CostCenter("Fakultätskostenstelle");
		institutes = new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public HashMap<String, Institute> getInstitutes() {
		return institutes;
	}

	public Dean getDean() {
		return dean;
	}

	public void setDean(Dean dean) {
		this.dean = dean;
		this.dean.setFaculty(this);
	}

	public CostCenter getFacultyCostCenter() {
		return facultyCostCenter;
	}

	public void addInstitute(Institute institute) {
		institutes.put(institute.getName(), institute);
		institute.setFaculty(this);
	}

	public Institute getInstituteByName(String name) {
		return institutes.get(name);
	}

	public boolean instituteExists(String name) {
		return institutes.containsKey(name);
	}

	public double getTotalDebt() {
		double debt = 0;
		
		for(Institute institute: institutes.values()) {
			double accountBalance = institute.getCostCenter().getBalance();
			
			if(accountBalance < 0) {
				debt += -accountBalance;
			}
		}

		return debt;
	}

	public boolean debitInstituteCostCenter(CostCenter instituteCostCenter, double amount) {
		
		if (instituteCostCenter == null || !instituteCostCenter.canDebit(amount)) {
			return false;
		}
		
		if(!containsInstituteCostCenter(instituteCostCenter)) {
			return false;
		}

		double newAccountBalance = instituteCostCenter.getBalance() - amount;
		double additionalDebt = 0;

		if (newAccountBalance < 0) {
			additionalDebt = -newAccountBalance;   
		}

		if (instituteCostCenter.getBalance() < 0) {
			additionalDebt -= -instituteCostCenter.getBalance();
		}

		if ((getTotalDebt() + additionalDebt) > MAX_TOTAL_DEBT) {
			System.out.printf("Abbuchung würde die Gesamtschulden-Grenze von %.2f überschreiten.", MAX_TOTAL_DEBT);
			return false;
		}

		return instituteCostCenter.instituteDebit(amount);
	}
	
	public boolean debitFacultyCostCenter(double amount) {
		if(amount <= 0) {
			return false;
		}
		return facultyCostCenter.facultyDebit(amount);
	}
	
	private boolean containsInstituteCostCenter(CostCenter costCenter) {

	    for (Institute institute : institutes.values()) {
	        if (institute.getCostCenter() == costCenter) {
	            return true;
	        }
	    }

	    return false;
	}
	
	

}
