package domain;

import java.util.ArrayList;
import java.io.Serializable;

public class CostCenter implements Serializable, StatisticsCapable{

	private String description;
	private double balance;
	private static final double MAX_OVERDRAFT = -3000.00;
	private ArrayList<Transaction> transactions = new ArrayList<>();

	public CostCenter(String description) {
		this.description = description;
		this.balance = 0;
	}

	public String getDescription() {
		return description;
	}

	public double getBalance() {
		return balance;
	}

	public void deposit(double amount) {
		if (amount > 0) {
			balance += amount;
			balance = Math.round(balance * 100.0) / 100.0;
			transactions.add(new Transaction(amount, "Einzahlung", TransactionType.DEPOSIT));
			System.out.printf("\nNeuer Kontostand von %s: %.2f EUR (Eingezahlter Betrag: %.2f EUR)%n", description, balance, amount);
		}
		

	}

	public boolean canDebit(double amount) {
		boolean may = amount > 0 && balance - amount >= MAX_OVERDRAFT;
		if (may)
			return true;
		System.out.printf("Abbuchung würde die Grenze von %.2f überschreiten.", MAX_OVERDRAFT);
		return false;
	}

	public boolean instituteDebit(double amount) {
		if (canDebit(amount)) {
			balance -= amount;
			balance = Math.round(balance * 100.0) / 100.0;
			transactions.add(new Transaction(amount, "Ausgabe", TransactionType.EXPENSE));
			System.out.printf("\nNeuer Kontostand von %s: %.2f EUR (Abgebuchter Betrag: %.2f EUR)%n", description, balance, amount);
			return true;
		}

		return false;
	}
	
	public boolean facultyDebit(double amount) {
		if (amount <= 0) {
		    return false;
		}
		balance -= amount;
		balance = Math.round(balance * 100.0) / 100.0; 
		transactions.add(new Transaction(amount, "Ausgabe", TransactionType.EXPENSE));
		System.out.printf("\nNeuer Kontostand von %s: %.2f EUR (Abgebuchter Betrag: %.2f EUR)%n", description, balance, amount);
		return true;
	}
	
	public double getTotalExpenses() {
		double total = 0;

		for (Transaction transaction : transactions) {
			if (transaction.getTransactionType() == TransactionType.EXPENSE) {
				total += transaction.getAmount();
			}
		}

		return total;
	}

}
