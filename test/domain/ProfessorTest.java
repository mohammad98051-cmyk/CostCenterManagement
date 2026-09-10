package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ProfessorTest {
	

	@Test
	void data() {
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");
		Institute institute = new Institute("Softwaretechnik");

		professor.setFirstname("Lisa");
		professor.setLastname("Maier");
		professor.setPersonnelNumber("99");
		professor.setInstitute(institute);

		assertEquals("Lisa", professor.getFirstname());
		assertEquals("Maier", professor.getLastname());
		assertEquals("99", professor.getPersonnelNumber());
		assertSame(institute, professor.getInstitute());
	}

	@Test
	void noInstitute() {
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");

		assertNull(professor.requestPurchase("Laptop", 500));
	}

	@Test
	void request() {
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");
		Institute institute = new Institute("Softwaretechnik");
		professor.setInstitute(institute);

		PurchaseRequest request = professor.requestPurchase("Laptop", 500);

		assertNotNull(request);
		assertEquals("Laptop", request.getDescription());
		assertEquals(500, request.getPrice(), 0.001);
		assertNotNull(request.getDate());
		assertSame(professor, request.getRequester());
		assertSame(institute.getCostCenter(), request.getCostCenter());
		assertTrue(professor.getRequests().contains(request));
	}

	@Test
	void invalidPrice() {
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");
		professor.setInstitute(new Institute("Softwaretechnik"));

		assertNull(professor.requestPurchase("Laptop", 0));
		assertNull(professor.requestPurchase("Laptop", -10));
		assertTrue(professor.getRequests().isEmpty());
	}

	@Test
	void depositWithoutInstitute() {
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");

		assertFalse(professor.deposit(100));
	}

	@Test
	void invalidDeposit() {
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");
		Institute institute = new Institute("Softwaretechnik");
		professor.setInstitute(institute);

		assertFalse(professor.deposit(0));
		assertFalse(professor.deposit(-10));
		assertEquals(0, institute.getCostCenter().getBalance(), 0.001);
	}

	@Test
	void deposit() {
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");
		Institute institute = new Institute("Softwaretechnik");
		professor.setInstitute(institute);

		assertTrue(professor.deposit(100));
		assertEquals(100, institute.getCostCenter().getBalance(), 0.001);
	}

	@Test
	void totalExpenses() {
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");
		PurchaseRequest firstRequest =
				new PurchaseRequest("Laptop", 500, professor, null, RequestStatus.OPEN);
		PurchaseRequest secondRequest =
				new PurchaseRequest("Maus", 50, professor, null, RequestStatus.OPEN);

		firstRequest.setStatus(RequestStatus.APPROVED);
		professor.getRequests().add(firstRequest);
		professor.getRequests().add(secondRequest);

		assertEquals(500, professor.getTotalExpenses(), 0.001);
	}


}
