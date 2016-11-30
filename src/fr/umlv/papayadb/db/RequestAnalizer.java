package fr.umlv.papayadb.db;

import java.util.Objects;

import io.vertx.core.json.JsonObject;

public class RequestAnalizer {
	public static void JsonToMethod(JsonObject json) {
		Objects.requireNonNull(json);
		switch(json.getString("db")) {
			case "":
				break;
			case "*":
				if(json.getString("method").equals("GET")) {
					//DatabaseManager.selectDatabases();
				} //sinon err
				break;
			default:
				//DatabaseManager.selectFromDatabase(,);
				break;
		}
		String method = json.getString("method");
		
	}
}
