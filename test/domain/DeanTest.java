package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import domain.*;

class DeanTest {
	
	@Test
	void deposit() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		Institute firstInstitute = admin.createInstitute("I1");
		Institute secondInstitute = admin.createInstitute("I2");
		Dean dean = admin.createDean("dekan", "pw", "Erika", "Muster", "20000");

		boolean successful = dean.facultyDeposit(100);

		assertTrue(successful);
		assertEquals(50, faculty.getFacultyCostCenter().getBalance(), 0.001);
		assertEquals(25, firstInstitute.getCostCenter().getBalance(), 0.001);
		assertEquals(25, secondInstitute.getCostCenter().getBalance(), 0.001);
	}

	@Test
	void noInstitutes() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		Dean dean = admin.createDean("dekan", "pw", "Erika", "Muster", "20000");

		boolean successful = dean.facultyDeposit(100);

		assertFalse(successful);
		assertEquals(0, faculty.getFacultyCostCenter().getBalance(), 0.001);
	}

	@Test
	void invalidAmount() {
		Faculty faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		admin.createInstitute("I1");
		Dean dean = admin.createDean("dekan", "pw", "Erika", "Muster", "20000");

		assertFalse(dean.facultyDeposit(0));
		assertFalse(dean.facultyDeposit(-10));
		assertEquals(0, faculty.getFacultyCostCenter().getBalance(), 0.001);
	}

	@Test
	void noFaculty() {
		Dean dean = new Dean("dekan", "pw", "Erika", "Muster", "20000");

		assertFalse(dean.facultyDeposit(100));
		assertNull(dean.getFaculty());
	}

}
