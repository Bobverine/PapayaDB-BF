package fr.umlv.papayadb.api;

public interface RequestInterface {
	public boolean createDatabase(String name);
	public boolean deleteDatabase(String name);
	public boolean insertFileFromLocal(String dbname, String filename);
	public boolean deleteFileFromDatabase(String dbname, String filename);
	public boolean selectFromDatabase(String dbname, String options);
	public boolean selectDatabases();
}
