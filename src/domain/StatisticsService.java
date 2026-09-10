package domain;

import java.util.*;

public class StatisticsService {

	public ArrayList<CostCenter> getInstitutesByBalanceDescending(Faculty faculty) {

		ArrayList<CostCenter> costCenters = new ArrayList<>();

		for (Institute institute : faculty.getInstitutes().values()) {
			costCenters.add(institute.getCostCenter());

		}

		costCenters.sort(new CostCenterBalanceComparator());
		return costCenters;

	}

	public ArrayList<Institute> getInstitutesByExpensesDescending(Faculty faculty) {

		ArrayList<Institute> institutes = new ArrayList<>();

		for (Institute institute : faculty.getInstitutes().values()) {
			institutes.add(institute);

		}

		institutes.sort(new InstituteExpensesComparator().reversed());
		return institutes;

	}

	public ArrayList<Professor> getProfessorsByExpensesDescending(Faculty faculty) {

		ArrayList<Professor> professors = new ArrayList<>();

		for (Institute institute : faculty.getInstitutes().values()) {
			professors.addAll(institute.getProfessors());

		}

		professors.sort(new ProfessorExpensesComparator());
		return professors;

	}

}
