package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UserTest {
	
	@Test
	void login() {
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");

		assertEquals("prof", professor.getUsername());
		assertEquals("pw", professor.getPassword());
		assertTrue(professor.login("pw"));
		assertFalse(professor.login("falsch"));
	}

	@Test
	void newPassword() {
		Professor professor = new Professor("prof", "pw", "Max", "Muster", "1");

		professor.setPassword("neu");

		assertEquals("neu", professor.getPassword());
		assertTrue(professor.login("neu"));
		assertFalse(professor.login("pw"));
	}


}
