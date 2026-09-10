package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class ApprovalTest {
	
	private Faculty faculty;
	private Institute institute;
	private Professor professor;
	private InstituteHead instituteHead;
	private Dean dean;

	@BeforeEach
	void setUp() {
		faculty = new Faculty("Informationstechnik");
		Admin admin = new Admin("admin", "pw", faculty);
		institute = admin.createInstitute("Softwaretechnik");
		professor = admin.createProfessor("prof", "pw", "Max", "Muster", institute);
		instituteHead = admin.createInstituteHead("leiter", "pw", "Lisa", "Muster", "20000", institute);
		dean = admin.createDean("dekan", "pw", "Erika", "Muster", "30000");
	}

	@Test
	void professorRequest() {
		PurchaseRequest request = professor.requestPurchase("Laptop", 500);

		assertNotNull(request);
		assertSame(professor, request.getRequester());
		assertSame(institute.getCostCenter(), request.getCostCenter());
		assertEquals(RequestStatus.OPEN, request.getStatus());
	}

	@Test
	void wrongPassword() {
		PurchaseRequest request = professor.requestPurchase("Laptop", 500);

		instituteHead.approveRequest(request, "falsch");

		assertEquals(RequestStatus.OPEN, request.getStatus());
		assertNull(request.getApprover());
	}

	@Test
	void approve() {
		professor.deposit(1000);
		PurchaseRequest request = professor.requestPurchase("Laptop", 500);

		instituteHead.approveRequest(request, "pw");

		assertEquals(RequestStatus.APPROVED, request.getStatus());
		assertSame(instituteHead, request.getApprover());
		assertEquals(500, institute.getCostCenter().getBalance(), 0.001);
	}

	@Test
	void ownRequest() {
		PurchaseRequest request = instituteHead.requestPurchase("Monitor", 200);

		instituteHead.approveRequest(request, "pw");

		assertEquals(RequestStatus.OPEN, request.getStatus());
		assertNull(request.getApprover());
	}

	@Test
	void deanOwnRequest() {
		PurchaseRequest request = dean.requestFacultyPurchase("Beamer", 300);

		assertEquals("Beamer", request.getDescription());
		assertEquals(300, request.getPrice());
		assertNotNull(request.getDate());
		assertSame(dean, request.getRequester());
		assertSame(faculty.getFacultyCostCenter(), request.getCostCenter());


		assertEquals(RequestStatus.APPROVED, request.getStatus());
	}

	@Test
	void deanWithoutFaculty() {
		Dean anotherDean = new Dean("d", "pw", "D", "Test", "1");

		PurchaseRequest request = anotherDean.requestFacultyPurchase("Beamer", 300);

		assertNull(request);
	}

	@Test
	void deanWrongPassword() {
		PurchaseRequest request = instituteHead.requestPurchase("Monitor", 200);

		dean.approveRequest(request, "falsch");

		assertEquals(RequestStatus.OPEN, request.getStatus());
		assertNull(request.getApprover());
	}

	@Test
	void deanApproves() {
		institute.getCostCenter().deposit(500);
		PurchaseRequest request = instituteHead.requestPurchase("Monitor", 200);

		dean.approveRequest(request, "pw");

		assertEquals(RequestStatus.APPROVED, request.getStatus());
		assertSame(dean, request.getApprover());
		assertEquals(300, institute.getCostCenter().getBalance(), 0.001);
	}

	@Test
	void deanOwnRequestIsAutomaticallyApproved() {
		dean.getFaculty().getFacultyCostCenter().deposit(1000);
		PurchaseRequest request = dean.requestFacultyPurchase("Beamer", 300);

		assertEquals(RequestStatus.APPROVED, request.getStatus());
		assertEquals(700, faculty.getFacultyCostCenter().getBalance(), 0.001);
	}


}
