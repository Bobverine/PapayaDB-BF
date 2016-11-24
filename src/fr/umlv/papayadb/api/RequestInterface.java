package fr.umlv.papayadb.api;

public interface RequestInterface {
	public boolean createDatabase(String name);
	public boolean deleteDatabase(String name);
	public boolean insertFileFromLocal(String name);
	public boolean deleteFileFromDb(String name);
	public boolean selectFromDatabase(String name, String options);
}
