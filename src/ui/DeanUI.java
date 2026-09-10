package ui;
import domain.*;
import java.util.*;

public class DeanUI extends TerminalUI {
	private StatisticsService statisticsService;
	private Dean dean;

	public DeanUI(User currentUser, Faculty faculty, Scanner scanner) {
		super(currentUser, faculty, scanner);
		statisticsService = new StatisticsService();
		dean = (Dean) currentUser;
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
				showOwnRequests();
				waitForEnter();
				break;
			case 2:
				showRequests();
				waitForEnter();
				break;
			case 3:
				requestFacultyPurchase();
				waitForEnter();
				break;
			case 4:
				facultyDeposit();
				waitForEnter();
				break;
			case 5:
				display();
				showStatistics();
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

	public void printMenu() {
		System.out.println("(1) Eigene Anfragen anzeigen" + " [" + dean.getRequests().size() + "]");
		System.out.println("(2) Offene Anfragen bearbeiten" + " [" + showOpenRequests().size() + "]");
		System.out.println("(3) Fakultätsanschaffung beantragen");
		System.out.println("(4) Geld einzahlen");
		System.out.println("(5) Statistiken anzeigen");
		System.out.println("(0) Ausloggen");
	}

	public void showOwnRequests() {
		display();

		System.out.println("\n--- Eigene Anschaffungsanfragen ---");

		if (dean.getRequests().isEmpty()) {
			System.out.println("Es gibt noch keine eigenen Anfragen.");
			return;
		}

		for (int i = 0; i < dean.getRequests().size(); i++) {
			PurchaseRequest request = dean.getRequests().get(i);

			System.out.print("(" + (i + 1) + ")");
			System.out.println("\tBeschreibung: " + request.getDescription());
			System.out.printf("\tPreis: %.2f EUR%n", request.getPrice());
			System.out.println("\tDatum: " + request.getDate());
			System.out.println("\tStatus: " + request.getStatus());
			System.out.println();
	}
}

	public void showRequests() {
		display();
		ArrayList<PurchaseRequest> openRequests = showOpenRequests();

		System.out.println("\n--- Offene Anfragen von Institutsleitern ---");

		if (openRequests.isEmpty()) {
			System.out.println("Es gibt keine offenen Anfragen von Institutsleitern.");
			return;
		}

		printOpenRequests(openRequests);

		int selection = enterNumber("Welche Anfrage bearbeiten (0 zurueck)");
		if (selection == 0) {
			return;
		}
		if (selection < 1 || selection > openRequests.size()) {
			System.out.println("Ungültige Auswahl.");
			return;
		}

		PurchaseRequest request = openRequests.get(selection - 1);
		System.out.println("(1) Anfrage bestätigen");
		System.out.println("(2) Anfrage ablehnen");
		System.out.println("(0) Abbrechen");

		int decision = enterNumber("Auswahl");

		if (decision == 1) {
			approveRequest(request);
		} else if (decision == 2) {
			rejectRequest(request);
		} else if (decision == 0) {
			System.out.println("Anfrage wurde nicht verändert.");
		} else {
			System.out.println("Ungültige Auswahl.");
		}
	}

	private void printOpenRequests(ArrayList<PurchaseRequest> openRequests) {
		for (int i = 0; i < openRequests.size(); i++) {
			PurchaseRequest request = openRequests.get(i);
			Professor requester = request.getRequester();

			System.out.print("(" + (i + 1) + ")");
			System.out.println("\tAntragsteller: " + requester.getFirstname() + " " + requester.getLastname());
			System.out.println("\tBeschreibung: " + request.getDescription());
			System.out.printf("\tPreis: %.2f EUR%n", request.getPrice());
			System.out.println("\tDatum: " + request.getDate());
			System.out.println("\tStatus: " + request.getStatus());
			System.out.println("\t" + request.getCostCenter().getDescription());
			System.out.println();
		}
	}

	private void approveRequest(PurchaseRequest request) {
		System.out.println();
		String password = enterText("Passwort zur Bestätigung");

		dean.approveRequest(request, password);

		if (request.getStatus() == RequestStatus.APPROVED) {
			System.out.println("\nAnfrage wurde bestätigt.");
			autoSave();
		} else {
			System.out.println("\nAnfrage konnte nicht bestätigt werden.");
		}
	}

	private void rejectRequest(PurchaseRequest request) {
		dean.rejectRequest(request);

		if (request.getStatus() == RequestStatus.REJECTED) {
			System.out.println("\nAnfrage wurde abgelehnt.");
			autoSave();
		} else {
			System.out.println("\nAnfrage konnte nicht abgelehnt werden.");
		}
	}

	public void requestFacultyPurchase() {
		display();

		System.out.println("\n--- Anschaffung beantragen ---");

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

		PurchaseRequest request = dean.requestFacultyPurchase(description, price);

		if (request != null) {
			System.out.println("Anschaffungsanfrage wurde erstellt.");
			System.out.println("Beschreibung: " + request.getDescription());
			System.out.printf("Preis: %.2f EUR%n", request.getPrice());
			System.out.println("Status: " + request.getStatus());
			autoSave();
		} else {
			System.out.println("Anschaffungsanfrage konnte nicht erstellt werden.");
		}
	}

	public void facultyDeposit() {
		display();

		System.out.println("\n--- Geld einzahlen ---");
		System.out.println(
				"50% des eingezahlten Betrags geht an die Fakultät. Der Rest wird unter den Instituten aufgeteilt.");
		
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
		boolean depositSuccessful = dean.facultyDeposit(amount);

		if (!depositSuccessful) {
			System.out.println("Einzahlung konnte nicht durchgeführt werden.");
			System.out.println("Es muss mindestens ein Institut existieren.");
			return;
		}
		autoSave();

		System.out.println("\nEinzahlung wurde durchgeführt.");
	}

	public ArrayList<PurchaseRequest> showOpenRequests() {
		ArrayList<PurchaseRequest> openRequests = new ArrayList<>();

		for (Institute institute : faculty.getInstitutes().values()) {
			for (Professor professor : institute.getProfessors()) {
				for (PurchaseRequest request : professor.getRequests()) {

					if (request.getStatus() == RequestStatus.OPEN && request.getRequester() != null
							&& !dean.getPersonnelNumber().equals(request.getRequester().getPersonnelNumber())
							&& request.getRequester() instanceof InstituteHead) {
						openRequests.add(request);
					}
				}
			}
		}

		return openRequests;
	}

	public void showStatistics() {

		System.out.println("\n--- Kontostände der Institute: ---");
		ArrayList<CostCenter> instituteBalances = statisticsService.getInstitutesByBalanceDescending(faculty);
		for (CostCenter costCenter : instituteBalances)
			System.out.printf("%s: %.2f EUR%n", costCenter.getDescription(), costCenter.getBalance());

		System.out.println("\n--- Ausgaben der Institute: ---");

		ArrayList<Institute> instituteExpenses = statisticsService.getInstitutesByExpensesDescending(faculty);
		for (Institute institute : instituteExpenses)
			System.out.printf("%s: %.2f EUR%n", institute.getName(), institute.getCostCenter().getTotalExpenses());

		System.out.println("\n--- Ausgaben der Professoren: ---");

		ArrayList<Professor> professorExpenses = statisticsService.getProfessorsByExpensesDescending(faculty);
		for (Professor professor : professorExpenses)
			System.out.printf("%s %s (%s): %.2f EUR%n", professor.getFirstname(), professor.getLastname(), professor.getUsername(),
					professor.getTotalExpenses());
	}

}
