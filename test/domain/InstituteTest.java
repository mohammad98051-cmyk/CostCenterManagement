package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import domain.*;

class InstituteTest {

	@Test
	void professor() {
		Institute institute = new Institute("Softwaretechnik");
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");

		institute.addProfessor(professor);

		assertEquals("Softwaretechnik", institute.getName());
		assertTrue(institute.getProfessors().contains(professor));
		assertSame(institute, professor.getInstitute());
		assertEquals("Softwaretechnik-Kostenstelle", institute.getCostCenter().getDescription());
	}

	@Test
	void instituteHead() {
		Institute institute = new Institute("Softwaretechnik");
		Faculty faculty = new Faculty("Informationstechnik");
		InstituteHead instituteHead = new InstituteHead("leiter", "pw", "Lisa", "Muster", "2");

		institute.setFaculty(faculty);
		institute.setInstituteHead(instituteHead);

		assertSame(faculty, institute.getFaculty());
		assertSame(instituteHead, institute.getInstituteHead());
		assertSame(institute, instituteHead.getInstitute());
	}

}
