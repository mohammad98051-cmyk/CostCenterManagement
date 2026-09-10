package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CostCenterTest {
	

	@Test
	void roundedDeposit() {
		CostCenter costCenter = new CostCenter("Test-Kostenstelle");

		costCenter.deposit(100.555);

		assertEquals("Test-Kostenstelle", costCenter.getDescription());
		assertEquals(100.56, costCenter.getBalance(), 0.001);
	}

	@Test
	void invalidDeposit() {
		CostCenter costCenter = new CostCenter("Test-Kostenstelle");

		costCenter.deposit(0);
		costCenter.deposit(-10);

		assertEquals(0, costCenter.getBalance());
	}

	@Test
	void limit() {
		CostCenter costCenter = new CostCenter("Test-Kostenstelle");

		assertTrue(costCenter.canDebit(3000));
		assertTrue(costCenter.instituteDebit(3000));
		assertEquals(-3000, costCenter.getBalance(), 0.001);
		assertFalse(costCenter.canDebit(0.01));
		assertFalse(costCenter.instituteDebit(3000));
		assertEquals(-3000, costCenter.getBalance(), 0.001);
	}

	@Test
	void invalidDebit() {
		CostCenter costCenter = new CostCenter("Test-Kostenstelle");

		assertFalse(costCenter.instituteDebit(0));
		assertFalse(costCenter.instituteDebit(-10));
		assertEquals(0, costCenter.getBalance(), 0.001);
	}

	@Test
	void expenses() {
		CostCenter costCenter = new CostCenter("Test-Kostenstelle");

		costCenter.deposit(100);
		costCenter.instituteDebit(30);
		costCenter.instituteDebit(20);

		assertEquals(50, costCenter.getTotalExpenses(), 0.001);
	}

}
