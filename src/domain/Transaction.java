package domain;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

	private double amount;
	private Date date;
	private String description;
	private TransactionType type;

	public Transaction(double amount, String description, TransactionType type) {
		this.amount = amount;
		this.description = description;
		this.type = type;
		this.date = new Date();
	}

	public double getAmount() {
		return amount;
	}

	public Date getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public TransactionType getTransactionType() {
		return type;
	}

}
