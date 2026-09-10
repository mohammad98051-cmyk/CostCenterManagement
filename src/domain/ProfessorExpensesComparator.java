package domain;

import java.util.Comparator;

public class ProfessorExpensesComparator implements Comparator<Professor> {

	@Override
	public int compare(Professor p1, Professor p2) {
		return Double.compare(p2.getTotalExpenses(), p1.getTotalExpenses());
	}

}
