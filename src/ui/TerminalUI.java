package ui;
import domain.*;
import persistence.*;
import java.util.*;
	
	public abstract class TerminalUI {
		protected User currentUser;
		protected Faculty faculty;
		protected Scanner scanner;
		
		private static final String FACULTY_FILE = "fakultaet.ser";

		public TerminalUI(User currentUser, Faculty faculty, Scanner scanner) {
			this.currentUser = currentUser;
			this.faculty = faculty;
			this.scanner = scanner;
		}

		public abstract void mainMenu();

		protected void printHeader() {
			System.out.println();
			System.out.println("Fakultät: " + faculty.getName());
			System.out.println("Eingeloggt als: " + currentUser.getUsername());
			System.out.println("Rolle: " + currentUser.getClass().getSimpleName());
			System.out.println();
		}

		protected String enterText(String prompt) {
			return enterLine(prompt);
		}

		protected int enterNumber(String prompt) {
			while (true) {
				String input = enterLine(prompt);
				try {
					return Integer.parseInt(input);
				} catch (NumberFormatException e) {
					System.out.println("Bitte eine Zahl eingeben.");
				}
			}
		}

		protected double enterAmount(String prompt) {
			while (true) {
				String input = enterLine(prompt);
				try {
					return Double.parseDouble(input.replace(',', '.'));
				} catch (NumberFormatException e) {
					System.out.println("Bitte einen gueltigen Betrag eingeben.");
				}
			}
		}

		protected String enterLine(String prompt) {
			while (true) {
				System.out.print(prompt + ": ");
				String input = scanner.nextLine().trim();

				if (!input.isEmpty()) {
					return input;
				}

				System.out.println("Die Eingabe darf nicht leer sein.");
			}
		}

		public void display() {
			System.out.print("\033[2J"); // clear display
			System.out.print("\033[1;1H"); // move cursor up left
		}

		protected void waitForEnter() {
			System.out.println("\nWeiter mit Enter...");
			scanner.nextLine();
		}

		protected void autoSave() {
			SerializedFacultyDAO dao = new SerializedFacultyDAO();
			dao.save(faculty, FACULTY_FILE);
		}
	}

