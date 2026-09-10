package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TransactionTest {

	@Test
	void getters() {
		Transaction transaction = new Transaction(100, "Einzahlung", TransactionType.DEPOSIT);

		assertEquals(100, transaction.getAmount(), 0.001);
		assertEquals("Einzahlung", transaction.getDescription());
		assertEquals(TransactionType.DEPOSIT, transaction.getTransactionType());
		assertNotNull(transaction.getDate());
	}


}
