package ui;
import domain.*;
import java.util.*;

public class AdminUI extends TerminalUI {
	private ArrayList<User> userList;
	private Admin admin;

	public AdminUI(User currentUser, Faculty faculty, Scanner scanner,
			ArrayList<User> userList) {
		super(currentUser, faculty, scanner);
		this.userList = userList;
		admin = (Admin) currentUser;
	}

	@Override
	public void mainMenu() {
		int choice;

		do {
			display();
			printHeader();
			printMenu();

			choice = enterNumber("Auswahl");

			switch (choice) {
			case 1:
				createInstitute();
				waitForEnter();
				break;
			case 2:
				createProfessor();
				waitForEnter();
				break;
			case 3:
				createDean();
				waitForEnter();
				break;
			case 4:
				createInstituteHead();
				waitForEnter();
				break;
			case 5:
				display();
				admin.printDashboard();
				waitForEnter();
				break;
			case 0:
				display();
				break;
			default:
				System.out.println("Ungueltige Auswahl.");
			}
		} while (choice != 0);
	}

	public void printMenu() {
		System.out.println("(1) Institut anlegen");
		System.out.println("(2) Professor anlegen");
		System.out.println("(3) Einem Professor die Rolle des Dekans geben");
		System.out.println("(4) Einem Professor die Rolle des Institutsleiters geben");
		System.out.println("(5) Dashboard ausgeben");
		System.out.println("(0) Ausloggen");
	}

	private void createInstitute() {
		display();
		System.out.println("\n--- Institut anlegen ---");
		String name = enterText("Institutsname");

		Institute institute = admin.createInstitute(name);

		if (institute == null) {
			System.out.println("Institut existiert bereits.");
			return;
		}
		autoSave();

		System.out.println("Institut wurde angelegt.");
	}

	public void createDean() {
		display();
		if (faculty.getDean() == null) {
			System.out.println("\n---Dekan anlegen---");
			String username = "dekan";
			System.out.println("\n Benutzername: " + username);
			String password = enterText("Passwort");
			System.out.println("\n Wählen Sie einen Professor aus.");
			Professor deanCandidate = selectProfessor();
			if (deanCandidate == null) {
			    System.out.println("Dekan wurde nicht angelegt.");
			    System.out.println("Es gibt keinen Professor, der zum Dekan gemacht werden kann.");
			    return;
			}

			Dean dean = admin.assignDean(deanCandidate, password, userList);

			if (dean == null) {
			    System.out.println("Dekan konnte nicht angelegt werden.");
			    return;
			}

			System.out.println("Dekan wurde angelegt.");
			autoSave();
		} else {
			System.out.println("Es gibt bereits einen Dekan: " + faculty.getDean().getFirstname() + " "
					+ faculty.getDean().getLastname() + " (" + faculty.getDean().getUsername() + ")");
			System.out.println("Möchten Sie einen neuen Dekan anlegen? (0:Nein/1:Ja)");
			int selection;
			do {
				selection = enterNumber("Auswahl");
				if (!(selection == 0 || selection == 1)) {
					System.out.println("Ungueltige Auswahl.");
				}
			} while (!(selection == 0 || selection == 1));

			if (selection == 0) {
				System.out.println("Dekan wurde nicht verändert.");
				return;
			}
			System.out.println("\n Wählen Sie einen Professor aus.");
			Professor deanCandidate = selectProfessor();
			if (deanCandidate == null) {
			    System.out.println("Dekan wurde nicht verändert.");
			    System.out.println("Es gibt keinen Professor, der zum Dekan gemacht werden kann.");
			    return;
			}

			String password = enterText("Setzen Sie ein neues Passwort");

			Dean newDean = admin.assignDean(
			        deanCandidate,
			        password,
			        userList);

			if (newDean == null) {
			    System.out.println("Dekan konnte nicht verändert werden.");
			    return;
			}

			System.out.println("Dekan wurde geändert.");
			autoSave();
		}
	}

	public void createProfessor() {
		display();
		if (faculty.getInstitutes().values().isEmpty()) {
			System.out.println("Es gibt noch keine Institute.");
			System.out.println("Professor wurde nicht angelegt.");
			return;
		}
		System.out.println("\n---Professor anlegen---");
		String username = enterText("Benutzername");

		if (userExists(username)) {
			System.out.println("Es gibt bereits einen Benutzer mit diesem Benutzernamen.");
			return;
		}
		String password = enterText("Passwort");
		String firstname = enterText("Vorname");
		String lastname = enterText("Nachname");

		System.out.println("\nWelches Institut?");
		Institute institute = selectInstitute();

		Professor professor = admin.createProfessor(username, password, firstname, lastname, institute);
		userList.add(professor);
		autoSave();

		System.out.println("Professor wurde angelegt.");
	}

	public void createInstituteHead() {
		display();
		if (faculty.getInstitutes().values().isEmpty()) {
			System.out.println("Es gibt noch keine Institute.");
			System.out.println("Institutsleiter wurde nicht angelegt.");
			return;
		}
		System.out.println("\n--- Institutsleiter setzen oder ändern ---");

		System.out.println("\nWähle zuerst das Institut aus.");
		Institute institute = selectInstitute();

		System.out.println("\nInstitut: " + institute.getName());
		if (institute.getInstituteHead() == null) {
			System.out.println("Aktueller Institutsleiter: Noch nicht gesetzt.");
		} else {
			System.out.println("Aktueller Institutsleiter: " + getUserDisplayName(institute.getInstituteHead()));
		}

		System.out.println("\nWählen Sie den neuen Institutsleiter aus:");
		Professor instituteHeadCandidate = selectProfessorFromInstitute(institute);
		if (instituteHeadCandidate == null) {
			System.out.println("Institutsleiter wurde nicht verändert.");
			return;
		}

		if (institute.getInstituteHead() != null) {
			System.out.println("\nSoll " + getUserDisplayName(institute.getInstituteHead()) + " durch "
					+ getUserDisplayName(instituteHeadCandidate) + " ersetzt werden?");
			System.out.println("(0) Nein");
			System.out.println("(1) Ja");
			int selection;
			do {
				selection = enterNumber("Auswahl");
				if (!(selection == 0 || selection == 1)) {
					System.out.println("Ungueltige Auswahl.");
				}
			} while (!(selection == 0 || selection == 1));

			if (selection == 0) {
				System.out.println("Institutsleiter wurde nicht verändert.");
				return;
			}
		}
		
		InstituteHead newInstituteHead = admin.assignInstituteHead(instituteHeadCandidate, institute, userList);
		
		if(newInstituteHead == null) {
			System.out.println("Institutsleiter konnte nicht gesetzt werden.");
		    return;
		}
		autoSave();

		System.out.println(getUserDisplayName(newInstituteHead) + " wurde als Institutsleiter von "
				+ institute.getName() + " gesetzt.");
	}

	public Institute selectInstitute() {
		ArrayList<Institute> institutes = new ArrayList<>(faculty.getInstitutes().values());
		institutes.sort(Comparator.comparing(Institute::getName));

		if (institutes.isEmpty()) {
			System.out.println("Es gibt noch keine Institute.");
			return null;
		}

		for (int i = 0; i < institutes.size(); i++) {
			System.out.println("(" + (i + 1) + ") " + institutes.get(i).getName());
		}

		int selection;
		do {
			selection = enterNumber("Auswahl");
			if (selection < 1 || selection > institutes.size()) {
				System.out.println("Ungueltige Auswahl.");
			}
		} while (selection < 1 || selection > institutes.size());

		return institutes.get(selection - 1);
	}

	private boolean userExists(String username) {
		for (User user : userList) {
			if (user.getUsername().equals(username)) {
				return true;
			}
		}

		return false;
	}

	public Professor selectProfessor() {
		ArrayList<Professor> professors = new ArrayList<>();
		String deanPersonnelNumber = null;
		if (faculty.getDean() != null) {
			deanPersonnelNumber = faculty.getDean().getPersonnelNumber();
		}
		for (Institute institute : faculty.getInstitutes().values()) {
			for (Professor professor : institute.getProfessors()) {
				if (deanPersonnelNumber == null || !professor.getPersonnelNumber().equals(deanPersonnelNumber)) {
					professors.add(professor);
				}
			}
		}
		professors.sort(Comparator.comparing(Professor::getUsername));

		if (professors.isEmpty()) {
			System.out.println("Es gibt noch keine Professoren.");
			return null;
		}

		for (int i = 0; i < professors.size(); i++) {
			System.out.println("(" + (i + 1) + ") " + professors.get(i));
		}

		int selection;
		do {
			selection = enterNumber("Auswahl");
			if (selection < 1 || selection > professors.size()) {
				System.out.println("Ungueltige Auswahl.");
			}
		} while (selection < 1 || selection > professors.size());

		return professors.get(selection - 1);
	}

	private Professor selectProfessorFromInstitute(Institute institute) {
		ArrayList<Professor> professors = new ArrayList<>();
		for (Professor professor : institute.getProfessors()) {
			if (!(professor instanceof InstituteHead)) {
				professors.add(professor);
			}
		}
		professors.sort(Comparator.comparing(Professor::getUsername));

		if (professors.isEmpty()) {
			System.out.println("Es gibt noch keine Professoren in diesem Institut.");
			return null;
		}

		for (int i = 0; i < professors.size(); i++) {
			System.out.println("(" + (i + 1) + ") " + professors.get(i));
		}

		int selection;
		do {
			selection = enterNumber("Auswahl");
			if (selection < 1 || selection > professors.size()) {
				System.out.println("Ungueltige Auswahl.");
			}
		} while (selection < 1 || selection > professors.size());

		return professors.get(selection - 1);
	}

	private String getUserDisplayName(Professor professor) {
		return professor.getFirstname() + " " + professor.getLastname() + " (" + professor.getUsername() + ")";
	}

}
