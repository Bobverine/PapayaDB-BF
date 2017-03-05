package fr.umlv.papayadb.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;

/**
 * Ensemble de m�thode permettant:
 * <br>- D'executer des commandes shell
 * <br>- De cr�er des fichiers de log
 * <br>- De copier des fichiers
 * <br>- De vider un dossier
 */
public class Log
{
	private static final String LOGPATH = "/PapayaDB/log/Log_";

	/**
	 * Cr�e un fichier de log contenant un message
	 * 
	 * @param message Message souhait� dans le log
	 */
	public static void createLog(Object message)
	{
		Date time = new Date();
		File pathDirs = new File("/PapayaDB/log/");
		pathDirs.mkdirs();
		String path = LOGPATH + "_Message_" +time +".log";
		new File(path);
		try
		{
			PrintWriter file = new PrintWriter(new FileOutputStream(path));
			file.println(message);
			file.close();
		} catch (Exception e)
		{
			createLog("Impossible de créer le fichier de log : \n", e);
		}
	}
	
	/**
	 * Crée un fichier de log contenant les exceptions levée
	 * 
	 * @param name Nom de l'exception
	 * @param error Description de l'exception
	 */
	public static void createLog(String name, Exception error)
	{
		Date time = new Date();
		File pathDirs = new File("/PapayaDB/log/");
		pathDirs.mkdirs();
		StackTraceElement[] stackTrace = error.getStackTrace();
		String thread = "Exception in thread \"";
		String lastTrace = stackTrace[stackTrace.length - 1].toString();
		lastTrace = lastTrace.substring(0, stackTrace[stackTrace.length - 1].toString().indexOf("("));
		lastTrace = lastTrace.substring(lastTrace.lastIndexOf(".") + 1, lastTrace.length());
		thread += lastTrace;
		thread += "\" ";
		System.out.println(thread + error.getClass().getName());
		for (StackTraceElement trace : stackTrace)
			System.out.println("\tat " + trace);
		String path = LOGPATH + "_Exception_" +time +".log";
		new File(path);
		try
		{
			PrintWriter file = new PrintWriter(new FileOutputStream(path));
			file.println("Nom:");
			file.println(name);
			file.println();
			file.println("Message:");
			file.println(error.getMessage());
			file.println();
			file.println("Trace:");
			file.println(thread + error.getClass().getName());
			for (StackTraceElement trace : stackTrace)
				file.println("\tat " + trace);
			file.close();
		} catch (Exception e) {
			createLog();
		}
	}
	
}
