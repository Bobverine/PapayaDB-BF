package fr.umlv.papayadb.db;

import java.util.Objects;

import io.vertx.core.json.JsonObject;

public class RequestAnalizer {
	
	/**
	 * Permet d'appliquer la bonne méthode en fonction du JSON en paramètre
	 * 
	 * @param json
	 *            json à traiter
	 */
	public static void JsonToMethod(JsonObject json) {
		Objects.requireNonNull(json);
		String method = json.getString("method");
		switch(json.getString("db")) {
			case "":
				break;
			case "*":
				if(method.equals("GET")) {
					//DatabaseManager.selectDatabases();
				} //sinon err
				break;
			default:
				//DatabaseManager.selectFromDatabase(,);
				break;
		}
		
	}
}
