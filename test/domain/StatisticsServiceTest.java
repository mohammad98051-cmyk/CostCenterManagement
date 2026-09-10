package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

class StatisticsServiceTest {

	
	@Test
	void balances() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		Institute poorInstitute = admin.createInstitute("Arm");
		Institute richInstitute = admin.createInstitute("Reich");
		poorInstitute.getCostCenter().deposit(10);
		richInstitute.getCostCenter().deposit(100);

		ArrayList<CostCenter> list =
				new StatisticsService().getInstitutesByBalanceDescending(faculty);

		assertSame(richInstitute.getCostCenter(), list.get(0));
		assertSame(poorInstitute.getCostCenter(), list.get(1));
	}

	@Test
	void institutes() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		Institute lowExpensesInstitute = admin.createInstitute("Wenig");
		Institute highExpensesInstitute = admin.createInstitute("Viel");
		faculty.debitInstituteCostCenter(lowExpensesInstitute.getCostCenter(), 100);
		faculty.debitInstituteCostCenter(highExpensesInstitute.getCostCenter(), 500);

		ArrayList<Institute> list =
				new StatisticsService().getInstitutesByExpensesDescending(faculty);

		assertSame(highExpensesInstitute, list.get(0));
		assertSame(lowExpensesInstitute, list.get(1));
	}

	@Test
	void professors() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		Institute institute = admin.createInstitute("Softwaretechnik");
		Professor lowExpensesProfessor =
				admin.createProfessor("wenig", "pw", "Wenig", "Ausgabe", institute);
		Professor highExpensesProfessor =
				admin.createProfessor("viel", "pw", "Viel", "Ausgabe", institute);
		InstituteHead instituteHead =
				admin.createInstituteHead("leiter", "pw", "Lisa", "Leiter", "30000", institute);

		institute.getCostCenter().deposit(1000);
		PurchaseRequest firstRequest = lowExpensesProfessor.requestPurchase("Maus", 100);
		PurchaseRequest secondRequest = highExpensesProfessor.requestPurchase("Laptop", 500);
		instituteHead.approveRequest(firstRequest, "pw");
		instituteHead.approveRequest(secondRequest, "pw");

		ArrayList<Professor> list =
				new StatisticsService().getProfessorsByExpensesDescending(faculty);

		assertSame(highExpensesProfessor, list.get(0));
		assertSame(lowExpensesProfessor, list.get(1));
	}


}
