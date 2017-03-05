package fr.umlv.papayadb.client;

import java.util.Scanner;

public class MainClient {

	/** Permet d'afficher d'instencier la classe Client et permet à l'utilisateur de saisir ses commandes
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("Entrez l'adresse suivi du port ex : http://127.0.0.1:8080");
		}
		System.out.println(args[0]);
		Client client;
		client = new Client(args[0]);
		System.out.println("Tapez votre commande ou tapez help pour afficher les commandes et la syntaxe");
		Scanner sc = new Scanner(System.in);
		String command = sc.nextLine();
		while (!command.contentEquals("exit")) {
			if (!client.executeCommand(command)) {
				System.out.println("Bad request !");
			}
			command = sc.nextLine();
		}
		sc.close();
	}

}
