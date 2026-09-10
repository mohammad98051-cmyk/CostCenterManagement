package ui;
import domain.*;
import java.util.*;

public class LoginUI {
	private Scanner scanner;

	public LoginUI(Scanner scanner) {
		this.scanner = scanner;
	}

	public User login(ArrayList<User> userList) {
		display();
		System.out.println("\n--- LOGIN ---");
		String username = enterLine("Benutzername");

		String password = enterLine("Passwort");

		for (User user : userList) {
			if (user.getUsername().equals(username) && user.login(password)) {
				return user;
			}
		}
		System.out.println("Benutzername oder Passwort falsch.");
		System.out.println("\nWeiter mit Enter...");
		scanner.nextLine();
		return null;
	}

	private String enterLine(String prompt) {
		while (true) {
			System.out.print(prompt + ": ");
			String input = scanner.nextLine().trim();

			if (!input.isEmpty()) {
				return input;
			}

			System.out.println("Die Eingabe darf nicht leer sein.");
		}
	}

	private void display() {
		System.out.print("\033[2J"); // clear display
		System.out.print("\033[1;1H"); // move cursor up left
	}

}
