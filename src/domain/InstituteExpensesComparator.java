package domain;

import java.util.Comparator;

public class InstituteExpensesComparator implements Comparator<Institute> {

	@Override
	public int compare(Institute i1, Institute i2) {
		return Double.compare(i1.getCostCenter().getTotalExpenses(), i2.getCostCenter().getTotalExpenses());
	}

}
