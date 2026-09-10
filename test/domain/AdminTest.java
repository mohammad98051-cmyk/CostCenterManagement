package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

class AdminTest {
	private static String facultyName;
	private static String adminUsername;
	private static String adminPassword;
	private static String instituteName;

	@BeforeAll
	static void setUpBeforeClass() {
		facultyName = "Informationstechnik";
		adminUsername = "admin";
		adminPassword = "123456";
		instituteName = "Softwaretechnik";
	}

	@Test
	void institute() {
		Faculty faculty = new Faculty(facultyName);
		Admin admin = new Admin(adminUsername, adminPassword, faculty);

		Institute institute = admin.createInstitute(instituteName);

		assertSame(faculty, admin.getFaculty());
		assertNotNull(institute);
		assertSame(institute, faculty.getInstituteByName(instituteName));
		assertSame(faculty, institute.getFaculty());
	}

	@Test
	void duplicateInstitute() {
		Faculty faculty = new Faculty(facultyName);
		Admin admin = new Admin(adminUsername, adminPassword, faculty);

		admin.createInstitute(instituteName);
		Institute secondInstitute = admin.createInstitute(instituteName);

		assertNull(secondInstitute);
		assertEquals(1, faculty.getInstitutes().size());
	}

	@Test
	void professor() {
		Faculty faculty = new Faculty(facultyName);
		Admin admin = new Admin(adminUsername, adminPassword, faculty);
		Institute institute = admin.createInstitute(instituteName);

		Professor professor = admin.createProfessor("prof", "pw", "Max", "Muster", institute);

		assertNotNull(professor);
		assertTrue(institute.getProfessors().contains(professor));
		assertSame(institute, professor.getInstitute());
		assertEquals("10000", professor.getPersonnelNumber());
	}

	@Test
	void professorWithoutInstitute() {
		Faculty faculty = new Faculty(facultyName);
		Admin admin = new Admin(adminUsername, adminPassword, faculty);

		Professor professor = admin.createProfessor("prof", "pw", "Max", "Muster", null);

		assertNull(professor);
	}

	@Test
	void dean() {
		Faculty faculty = new Faculty(facultyName);
		Admin admin = new Admin(adminUsername, adminPassword, faculty);

		Dean dean = admin.createDean("dekan", "pw", "Erika", "Muster", "20000");

		assertNotNull(dean);
		assertSame(dean, faculty.getDean());
		assertSame(faculty, dean.getFaculty());
	}

	@Test
	void duplicateDean() {
		Faculty faculty = new Faculty(facultyName);
		Admin admin = new Admin(adminUsername, adminPassword, faculty);

		admin.createDean("dekan", "pw", "Erika", "Muster", "20000");
		Dean secondDean = admin.createDean("dekan2", "pw", "Max", "Muster", "20001");

		assertNull(secondDean);
		assertEquals("dekan", faculty.getDean().getUsername());
	}

	@Test
	void instituteHead() {
		Faculty faculty = new Faculty(facultyName);
		Admin admin = new Admin(adminUsername, adminPassword, faculty);
		Institute institute = admin.createInstitute(instituteName);

		InstituteHead instituteHead =
				admin.createInstituteHead("leiter", "pw", "Lisa", "Muster", "30000", institute);

		assertNotNull(instituteHead);
		assertSame(instituteHead, institute.getInstituteHead());
		assertSame(institute, instituteHead.getInstitute());
		assertTrue(institute.getProfessors().contains(instituteHead));
	}

	@Test
	void duplicateInstituteHead() {
		Faculty faculty = new Faculty(facultyName);
		Admin admin = new Admin(adminUsername, adminPassword, faculty);
		Institute institute = admin.createInstitute(instituteName);

		InstituteHead firstInstituteHead =
				admin.createInstituteHead("leiter", "pw", "Lisa", "Muster", "30000", institute);
		InstituteHead secondInstituteHead =
				admin.createInstituteHead("leiter2", "pw", "Max", "Muster", "30001", institute);

		assertNotNull(firstInstituteHead);
		assertNull(secondInstituteHead);
		assertSame(firstInstituteHead, institute.getInstituteHead());
	}

	@Test
	void nextPersonnelNumber() {
		Faculty faculty = new Faculty(facultyName);
		Admin admin = new Admin(adminUsername, adminPassword, faculty);
		Institute institute = admin.createInstitute(instituteName);
		admin.createProfessor("prof", "pw", "Max", "Muster", institute);
		admin.createDean("dekan", "pw", "Erika", "Muster", "20000");

		admin.updateNextPersonnelNumber();
		Professor professor = admin.createProfessor("prof2", "pw", "Lisa", "Muster", institute);

		assertEquals("20001", professor.getPersonnelNumber());
	}

}
