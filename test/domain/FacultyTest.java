package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import domain.*;

class FacultyTest {
	
	@Test
	void debt() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		Institute firstInstitute = admin.createInstitute("I1");
		Institute secondInstitute = admin.createInstitute("I2");

		faculty.debitInstituteCostCenter(firstInstitute.getCostCenter(), 1000);
		faculty.debitInstituteCostCenter(secondInstitute.getCostCenter(), 2000);
		faculty.debitInstituteCostCenter(faculty.getFacultyCostCenter(), 500);

		assertEquals(3000, faculty.getTotalDebt());
		assertTrue(faculty.instituteExists("I1"));
		assertSame(firstInstitute, faculty.getInstituteByName("I1"));
	}

	@Test
	void limit() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		Institute firstInstitute = admin.createInstitute("I1");
		Institute secondInstitute = admin.createInstitute("I2");

		assertTrue(faculty.debitInstituteCostCenter(firstInstitute.getCostCenter(), 3000));
		assertTrue(faculty.debitInstituteCostCenter(secondInstitute.getCostCenter(), 3000));
		assertTrue(faculty.debitFacultyCostCenter(3000));
		assertFalse(faculty.debitInstituteCostCenter(firstInstitute.getCostCenter(), 1));
		assertFalse(faculty.debitInstituteCostCenter(null, 1));
	}

	@Test
	void extraCostCenterLimit() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		Institute firstInstitute = admin.createInstitute("I1");
		Institute secondInstitute = admin.createInstitute("I2");
		CostCenter extraCostCenter = new CostCenter("Extra");

		assertTrue(faculty.debitInstituteCostCenter(firstInstitute.getCostCenter(), 3000));
		assertTrue(faculty.debitInstituteCostCenter(secondInstitute.getCostCenter(), 3000));
		assertTrue(faculty.debitFacultyCostCenter(3000));

		assertFalse(faculty.debitInstituteCostCenter(extraCostCenter, 1001));
		assertEquals(0, extraCostCenter.getBalance(), 0.001);
	}

	@Test
	void existingDebt() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		Institute institute = admin.createInstitute("I1");

		assertTrue(faculty.debitInstituteCostCenter(institute.getCostCenter(), 1000));
		assertTrue(faculty.debitInstituteCostCenter(institute.getCostCenter(), 500));

		assertEquals(1500, faculty.getTotalDebt(), 0.001);
		assertEquals(-1500, institute.getCostCenter().getBalance(), 0.001);
	}

	@Test
	void invalidAmount() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		Institute institute = admin.createInstitute("I1");

		assertFalse(faculty.debitInstituteCostCenter(institute.getCostCenter(), 0));
		assertFalse(faculty.debitInstituteCostCenter(institute.getCostCenter(), -1));
		assertEquals(0, institute.getCostCenter().getBalance(), 0.001);
	}

}
