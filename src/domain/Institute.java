package domain;

import java.util.ArrayList;
import java.io.Serializable;

public class Institute implements Serializable {
	private String name;
	private Faculty faculty;
	private ArrayList<Professor> professors = new ArrayList<>();
	private InstituteHead instituteHead;
	private CostCenter instituteCostCenter;

	public Institute(String name) {
		this.name = name;
		this.instituteCostCenter = new CostCenter(name + "-Kostenstelle");
	}

	public String getName() {
		return name;
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public ArrayList<Professor> getProfessors() {
		return professors;
	}

	public InstituteHead getInstituteHead() {
		return instituteHead;
	}

	public void setInstituteHead(InstituteHead instituteHead) {
	    this.instituteHead = instituteHead;

	    if (instituteHead != null) {
	        instituteHead.setInstitute(this);
	    }
	}

	public CostCenter getCostCenter() {
		return instituteCostCenter;
	}

	public void addProfessor(Professor professor) {
		professors.add(professor);
		professor.setInstitute(this);
	}
}
