package ui;
import domain.*;
import java.util.*;

public class InstituteHeadUI extends TerminalUI{

	private InstituteHead instituteHead;

	public InstituteHeadUI(User currentUser, Faculty faculty, Scanner scanner) {
		super(currentUser, faculty, scanner);
		instituteHead = (InstituteHead) currentUser;
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
				showOwnRequests();
				waitForEnter();
				break;
			case 3:
				showRequests();
				waitForEnter();
				break;
			case 4:
				deposit();
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

	private void printMenu() {
		System.out.println("(1) Anschaffung beantragen");
		System.out.println("(2) Eigene Anfragen anzeigen" + " [" + instituteHead.getRequests().size() + "]");
		System.out.println("(3) Offene Anfragen bearbeiten" + " [" + showOpenRequests().size() + "]");
		System.out.println("(4) Geld einzahlen");
		System.out.println("(0) Ausloggen");
	}
	
	public void requestPurchase() {
		display();

		System.out.println("\n--- Anschaffung beantragen ---");

		if (instituteHead.getInstitute() == null) {
			System.out.println("Institutsleiter ist keinem Institut zugeordnet.");
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

		PurchaseRequest request = instituteHead.requestPurchase(description, price);

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

	public void showOwnRequests() {
		display();

		System.out.println("\n--- Eigene Anschaffungsanfragen ---");

		if (instituteHead.getRequests().isEmpty()) {
			System.out.println("Es gibt noch keine eigenen Anfragen.");
			return;
		}

		for (int i = 0; i < instituteHead.getRequests().size(); i++) {
			PurchaseRequest request = instituteHead.getRequests().get(i);

			System.out.print("(" + (i + 1) + ")");
			System.out.println("\tBeschreibung: " + request.getDescription());
			System.out.printf("\tPreis: %.2f EUR%n", request.getPrice());
			System.out.println("\tDatum: " + request.getDate());
			System.out.println("\tStatus: " + request.getStatus());

			if (request.getApprover() != null) {
				Professor approver = (Professor) request.getApprover();
				System.out.println("\tGenehmiger: " + request.getApprover().getClass().getSimpleName() + " – " + approver.getFirstname() + " " + approver.getLastname());
			} else {
				System.out.println("\tGenehmiger: noch keiner");
			}

			System.out.println();
		}
	}

	public void showRequests() {
		display();
		ArrayList<PurchaseRequest> openRequests = showOpenRequests();

		System.out.println("\n--- Offene Anfragen des Instituts ---");

		if (openRequests.isEmpty()) {
			System.out.println("Es gibt keine offenen Anfragen.");
			return;
		}

		printOpenRequests(openRequests);

		int selection = enterNumber("Welche Anfrage bearbeiten (0 zurueck)");
		if (selection == 0) {
			return;
		}
		if (selection < 1 || selection > openRequests.size()) {
			System.out.println("Ungueltige Auswahl.");
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
			System.out.println("Ungueltige Auswahl.");
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
			System.out.println();
		}
	}

	private void approveRequest(PurchaseRequest request) {
		System.out.println();
		String password = enterText("Passwort zur Bestätigung");

		instituteHead.approveRequest(request, password);

		if (request.getStatus() == RequestStatus.APPROVED) {
			System.out.println("\nAnfrage wurde bestätigt.");
			autoSave();
		} else {
			System.out.println("\nAnfrage konnte nicht bestätigt werden.");
		}
	}

	private void rejectRequest(PurchaseRequest request) {
		instituteHead.rejectRequest(request);

		if (request.getStatus() == RequestStatus.REJECTED) {
			System.out.println("\nAnfrage wurde abgelehnt.");
			autoSave();
		} else {
			System.out.println("\nAnfrage konnte nicht abgelehnt werden.");
		}
	}

	public ArrayList<PurchaseRequest> showOpenRequests() {
		ArrayList<PurchaseRequest> openRequests = new ArrayList<>();

		Institute institute = instituteHead.getInstitute();

		if (institute == null) {
			return openRequests;
		}

		for (Professor professor : institute.getProfessors()) {
			for (PurchaseRequest request : professor.getRequests()) {
				if (request.getStatus() == RequestStatus.OPEN && request.getRequester() != null
						&& !instituteHead.getPersonnelNumber().equals(request.getRequester().getPersonnelNumber())) {
					openRequests.add(request);
				}
			}
		}

		if (faculty.getDean() != null) {
			for (PurchaseRequest request : faculty.getDean().getRequests()) {
				if (request.getStatus() == RequestStatus.OPEN && request.getRequester() != null
						&& !instituteHead.getPersonnelNumber().equals(request.getRequester().getPersonnelNumber())) {
					openRequests.add(request);
				}
			}
		}

		return openRequests;
	}

	public void deposit() {
		display();

		System.out.println("\n--- Geld einzahlen ---");
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

		boolean depositSuccessful = instituteHead.deposit(amount);

		if (!depositSuccessful) {
			System.out.println("Einzahlung konnte nicht durchgeführt werden.");
			System.out.println("Institutsleiter ist keinem Institut zugeordnet oder der Betrag ist ungültig.");
			return;
		}
		autoSave();
		System.out.println("Einzahlung wurde durchgefuehrt.");
	}

}
