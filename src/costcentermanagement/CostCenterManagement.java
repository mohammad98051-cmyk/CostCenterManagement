package costcentermanagement;

import domain.*;
import ui.*;
import persistence.*;
import java.util.*;
/*
Run program in the console:
javac -d bin $(find src -name "*.java")
java -cp bin de.hma.soe.CostCenterManager
*/

public class CostCenterManagement {

	public static void main(String[] args) {

		ArrayList<User> userList = new ArrayList<>();

		SerializedFacultyDAO facultyDAO = new SerializedFacultyDAO();
		Faculty faculty = facultyDAO.load("fakultaet.ser");

		if (faculty == null) {
			faculty = new Faculty("Informationstechnik");
		}

		Admin admin = new Admin("admin", "123456", faculty);
		admin.updateNextPersonnelNumber();
		userList.add(admin);

		// Fill user list
		if (faculty.getDean() != null) {
			userList.add(faculty.getDean());
		}

		for (Institute institute : faculty.getInstitutes().values()) {
			for (Professor professor : institute.getProfessors()) {
				userList.add(professor);
			}
		}

		// Start login screen
		Scanner scanner = new Scanner(System.in);
		LoginUI loginUI = new LoginUI(scanner);

		boolean programRunning = true;

		while (programRunning) {

			int selection = getStartMenuSelection(scanner);

			if (selection == 0) {
				programRunning = false;
				continue;
			}

			if (selection != 1) {
				System.out.println("Ungueltige Auswahl.");
				continue;
			}

			User loggedInUser = loginUI.login(userList);

			if (loggedInUser == null) {
				continue;

			} else if (loggedInUser instanceof Admin) {

				AdminUI adminUI = new AdminUI(loggedInUser, faculty, scanner, userList);

				adminUI.mainMenu();

			} else if (loggedInUser instanceof Dean) {

				DeanUI deanUI = new DeanUI(loggedInUser, faculty, scanner);

				deanUI.mainMenu();

			} else if (loggedInUser instanceof InstituteHead) {

				InstituteHeadUI instituteHeadUI = new InstituteHeadUI(loggedInUser, faculty, scanner);

				instituteHeadUI.mainMenu();

			} else if (loggedInUser instanceof Professor) {

				ProfessorUI professorUI = new ProfessorUI(loggedInUser, faculty, scanner);

				professorUI.mainMenu();
			}
		}
		
		System.out.println("Sie wurden ausgeloggt.");
	}

	private static int getStartMenuSelection(Scanner scanner) {

		while (true) {

			System.out.print("\033[2J"); // clear display
			System.out.print("\033[1;1H"); // move cursor up left

			System.out.println("\n--- Kostenstellenverwaltung der Fakultät für Informationstechnik ---");
			System.out.println("\nWählen Sie eine der folgenden Optionen aus:");
			System.out.println("(1) Einloggen");
			System.out.println("(0) Programm beenden");
			System.out.print("\nAuswahl: ");

			try {

				int selection = Integer.parseInt(scanner.nextLine());

				if (selection == 0 || selection == 1) {
					return selection;
				}

			} catch (NumberFormatException e) {
				// Error message is displayed below
			}

			System.out.println("Ungueltige Auswahl.");
		}
	}
}

