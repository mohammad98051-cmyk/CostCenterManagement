package ui;
import domain.*;
import java.util.Scanner;


	public class ProfessorUI extends TerminalUI {

		private Professor professor;

		public ProfessorUI(User currentUser, Faculty faculty, Scanner scanner) {
			super(currentUser, faculty, scanner);
			professor = (Professor) currentUser;
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
					requestPurchase();
					waitForEnter();
					break;
				case 2:
					deposit();
					waitForEnter();
					break;
				case 3:
					showOwnRequests();
					waitForEnter();
					break;
				case 0:
					display();
					break;
				default:
					System.out.println("Ungültige Auswahl.");
				}

			} while (choice != 0);
		}

		private void printMenu() {
			System.out.println("(1) Anschaffung beantragen");
			System.out.println("(2) Geld einzahlen");
			System.out.println("(3) Eigene Anfragen anzeigen" + " [" + professor.getRequests().size() + "]");
			System.out.println("(0) Ausloggen");
		}

		public void requestPurchase() {
			display();

			System.out.println("\n--- Anschaffung beantragen ---");

			if (professor.getInstitute() == null) {
				System.out.println("Professor ist keinem Institut zugeordnet.");
				return;
			}

			if (professor.getInstitute().getInstituteHead() == null) {
				System.out.println("Es gibt keinen Institutsleiter für das Institut: " + professor.getInstitute().getName());
				System.out.println("Der Administrator muss einen Institutsleiter anlegen!");
				return;
			}

			String description = enterText("Beschreibung");
			double price = enterAmount("Preis");

			if (description == null || description.isBlank()) {
				System.out.println("Die Beschreibung darf nicht leer sein.");
				return;
			}

			if (price <= 0) {
				System.out.println("Der Preis muss groesser als 0 sein.");
				return;
			}

			PurchaseRequest request = professor.requestPurchase(description, price);

			if (request != null) {
				display();
				System.out.println("Anschaffungsanfrage wurde erstellt.");
				System.out.println("Beschreibung: " + request.getDescription());
				System.out.printf("Preis: %.2f EUR%n", request.getPrice());
				System.out.println("Status: " + request.getStatus());
				autoSave();
			} else {
				System.out.println("Anschaffungsanfrage konnte nicht erstellt werden.");
			}
		}

		public void deposit() {
			display();

			System.out.println("\n--- Geld einzahlen ---");

			if (professor.getInstitute() == null) {
				System.out.println("Professor ist keinem Institut zugeordnet.");
				return;
			}

			double amount;
			
			while(true) {
				amount = enterAmount("Betrag");

				if (amount <= 0) {
					System.out.println("Betrag muss groesser als 0 sein.");
					System.out.println("Bitte versuchen Sie nochmal.");
					continue;
				}
				break;
			}


			boolean depositSuccessful = professor.deposit(amount);

			if (!depositSuccessful) {
				System.out.println("Einzahlung konnte nicht durchgefuehrt werden.");
				System.out.println("Professor ist keinem Institut zugeordnet oder der Betrag ist ungültig.");
				return;
			}
			autoSave();

			System.out.println("Einzahlung wurde durchgeführt.");
		}

		public void showOwnRequests() {
			display();

			System.out.println("\n--- Eigene Anschaffungsanfragen ---");

			if (professor.getRequests().isEmpty()) {
				System.out.println("Es gibt noch keine eigenen Anfragen.");
				return;
			}

			for (int i = 0; i < professor.getRequests().size(); i++) {
				PurchaseRequest request = professor.getRequests().get(i);

				System.out.print("(" + (i + 1) + ")");
				System.out.println("\tBeschreibung: " + request.getDescription());
				System.out.printf("\tPreis: %.2f EUR%n", request.getPrice());
				System.out.println("\tDatum: " + request.getDate());
				System.out.println("\tStatus: " + request.getStatus());

				if (request.getApprover() != null) {
					System.out.println("\tGenehmiger: Institutsleiter " + professor.getInstitute().getInstituteHead().getFirstname() + " "+ professor.getInstitute().getInstituteHead().getLastname());
				} else {
					System.out.println("\tGenehmiger: noch keiner");
				}

				System.out.println();
			}
		}
	}

