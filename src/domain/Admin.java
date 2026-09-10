package domain;

import java.util.ArrayList;


public class Admin extends User {
	private int personnelNumber = 10000;
	private Faculty faculty;

	public Admin(String username, String password, Faculty faculty) {
		super(username, password);
		this.faculty = faculty;
	}

	public Professor createProfessor(String username, String password, String firstname, String lastname,
			Institute institute) {
		if (institute == null) {
		    return null;
	    }

		Professor professor = new Professor(username, password, firstname, lastname, Integer.toString(personnelNumber++));
		institute.addProfessor(professor);
		return professor;
	}

	public Dean createDean(String username, String password, String firstname, String lastname,
			String personnelNumber) {
		if (faculty.getDean() == null) {
			Dean dean = new Dean(username, password, firstname, lastname, personnelNumber);
			faculty.setDean(dean);
			return dean;
		}
		return null;
	}

	public Institute createInstitute(String name) {
		if (!faculty.instituteExists(name)) {
			Institute institute = new Institute(name);
			faculty.addInstitute(institute);
			return institute;
		}
		return null;
	}

	public InstituteHead createInstituteHead(String username, String password, String firstname, String lastname,
			String personnelNumber, Institute institute) {

		if (institute.getInstituteHead() == null) {
			InstituteHead instituteHead = new InstituteHead(username, password, firstname, lastname,
					personnelNumber);

			institute.addProfessor(instituteHead);
			institute.setInstituteHead(instituteHead);

			return instituteHead;
		}

		return null;
	}

	public void printDashboard() {
		System.out.println();
		System.out.println("-".repeat(80));
		System.out.println("Fakultät: " + faculty.getName());
		Dean dean = faculty.getDean();
		if (dean != null)
			System.out.println(
			        "\tDekan: "
			        + dean.getFirstname() + " "
			        + dean.getLastname()
			        + ", PersonalNR: " + dean.getPersonnelNumber()
			        + " | Benutzername: " + dean.getUsername() + "|"
			);
		else
			System.out.println("\tDekan: Noch nicht gesetzt.");

		for (Institute institute : faculty.getInstitutes().values()) {
			System.out.println("-".repeat(80));
			System.out.println("Institut: " + institute.getName());
			InstituteHead instituteHead = institute.getInstituteHead();
			if (instituteHead != null)
				System.out.println(
				        "\tInstitutsleiter: "
				        + instituteHead.getFirstname() + " "
				        + instituteHead.getLastname()
				        + ", PersonalNR: " + instituteHead.getPersonnelNumber()
				        + " | Benutzername: " + instituteHead.getUsername() + "|"
				);
			else
				System.out.println("\tInstitutsleiter: Noch nicht gesetzt.");
			for (Professor professor : institute.getProfessors()) {
				if (professor instanceof InstituteHead) {
					continue;
				}
				System.out.println("\t" + professor);
			}

		}
		System.out.println("-".repeat(80));
	}

	public void updateNextPersonnelNumber() {
		int max = 9999;

		if (faculty.getDean() != null) {
			max = Math.max(max, Integer.parseInt(faculty.getDean().getPersonnelNumber()));
		}

		for (Institute institute : faculty.getInstitutes().values()) {
			for (Professor professor : institute.getProfessors()) {
				max = Math.max(max, Integer.parseInt(professor.getPersonnelNumber()));
			}
		}

		personnelNumber = max + 1;
	}

	public Faculty getFaculty() {
		return faculty;
	}
	
	public InstituteHead assignInstituteHead(Professor instituteHeadCandidate, Institute institute, ArrayList<User> userList) {
		
		if (instituteHeadCandidate == null || institute == null || userList == null || instituteHeadCandidate.getInstitute() != institute) {
		    return null;
		}
		
		InstituteHead oldInstituteHead = institute.getInstituteHead();
		InstituteHead newInstituteHead = new InstituteHead(instituteHeadCandidate.getUsername(), instituteHeadCandidate.getPassword(), instituteHeadCandidate.getFirstname(),
				                                           instituteHeadCandidate.getLastname(), instituteHeadCandidate.getPersonnelNumber());

	    
		institute.getProfessors().remove(instituteHeadCandidate);
		userList.remove(instituteHeadCandidate);

		if (oldInstituteHead != null) {
			institute.getProfessors().remove(oldInstituteHead);
			userList.remove(oldInstituteHead);

			Professor oldProfessor = new Professor(oldInstituteHead.getUsername(),
					oldInstituteHead.getPassword(), oldInstituteHead.getFirstname(),
					oldInstituteHead.getLastname(), oldInstituteHead.getPersonnelNumber());
			institute.addProfessor(oldProfessor);
			userList.add(oldProfessor);
		}

		institute.addProfessor(newInstituteHead);
		institute.setInstituteHead(newInstituteHead);
		userList.add(newInstituteHead);
		return newInstituteHead;
	}
	
	public Dean assignDean(Professor deanCandidate, String password, ArrayList<User> userList) {

	    if (deanCandidate == null || password == null || password.isBlank() || userList == null) {
	        return null;
	    }
	    
	    if (deanCandidate instanceof InstituteHead) {
	        Institute institute = deanCandidate.getInstitute();
	        institute.setInstituteHead(null);
	    }


	    Institute candidateInstitute = deanCandidate.getInstitute();

	    if (candidateInstitute == null) {
	        return null;
	    }

	    Dean oldDean = faculty.getDean();

	    if (oldDean != null) {

	        Institute oldInstitute = oldDean.getInstitute();

	        userList.remove(oldDean);

	        if (oldInstitute != null) {

	            Professor oldProfessor = new Professor(
	                    oldDean.getUsername(),
	                    oldDean.getPassword(),
	                    oldDean.getFirstname(),
	                    oldDean.getLastname(),
	                    oldDean.getPersonnelNumber()
	            );

	            oldInstitute.addProfessor(oldProfessor);
	            userList.add(oldProfessor);
	        }
	    }

	    candidateInstitute.getProfessors().remove(deanCandidate);
	    userList.remove(deanCandidate);

	    
	    Dean newDean = new Dean(
	            deanCandidate.getUsername(),
	            password,
	            deanCandidate.getFirstname(),
	            deanCandidate.getLastname(),
	            deanCandidate.getPersonnelNumber());
	    newDean.setInstitute(candidateInstitute);

	    faculty.setDean(newDean);
	    userList.add(newDean);

	    return newDean;
	    }
}