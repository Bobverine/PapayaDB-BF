package fr.umlv.papayadb.db;

import fr.umlv.papayadb.api.RequestInterface;

public class DatabaseManager implements RequestInterface {

	@Override
	public boolean createDatabase(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteDatabase(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertFileFromLocal(String dbname, String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFileFromDatabase(String dbname, String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean selectFromDatabase(String dbname, String options) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean selectDatabases() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
