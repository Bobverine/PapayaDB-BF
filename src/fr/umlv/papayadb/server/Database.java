package fr.umlv.papayadb.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Set;

public class Database {
	private FileChannel filechannel;
	private HashMap<Integer, String> databases = new HashMap<>();
	private HashMap<Integer, String> files = new HashMap<>(); //adress, name
	
	public Database() {
		Path path = Paths.get("./papaya.db");
		Set<OpenOption> options = Set.of(StandardOpenOption.CREATE, StandardOpenOption.SYNC, StandardOpenOption.READ, StandardOpenOption.WRITE);
		try {
			filechannel = FileChannel.open(path, options);
		} catch (IOException e) {
			System.out.println("failed creating/opening papaya.db file");
			e.printStackTrace();
		}
		updateDatabases();
		//updateFiles();
	}

	public void updateDatabases(){
		try {
			MappedByteBuffer buffer = filechannel.map(MapMode.READ_WRITE, 0, filechannel.size());
			while(buffer.hasRemaining()) {
				int size = buffer.getInt(); //on get la taille de la première bdd
				if(size <= 0) { //si aucune taille indiquée, aucune bdd
					return;
				} else {
					byte[] name = new byte[40];
					buffer.get(name, 0, 40); //on get le nom de la première bdd
					System.out.println(size + " : " + new String(name, StandardCharsets.UTF_8));
					databases.put(size, new String(name, StandardCharsets.UTF_8)); //on l'ajoute à la hashmap
					buffer.position(buffer.position() + size); //on set la position du buffer à la prochaine bdd
				}
			}
			//System.out.println("On essaye de faire une phrase assez longue".getBytes().length + " IIIIICCCCCIIIII");
		} catch (IOException e) {
			System.out.println("Failed updating databases");
			e.printStackTrace();
		}
		System.out.println("Number of databases : " + databases.size());
	}
	
	public void updateFiles(){
		
	}
	
	public void insertFile(String db, String file) {//type surement à chaner...
		//Trouver la base de données passée en argument et insérer à la suite
		//4 octets soit 32 bits pour stocker la taille du fichier
		ByteBuffer buffer = ByteBuffer.allocate(4 + file.getBytes().length);
		buffer.putInt(file.getBytes().length);
		buffer.put(file.getBytes());
		try {
			filechannel.write(buffer);
		} catch (IOException e) {
			System.out.println("Failed inserting record");
			e.printStackTrace();
		}
		//valeur de retour ?
	}

	public void createDatabase(String name) {
		//mettre le buffer à la bonne position d'abord !!!
		ByteBuffer buffer = ByteBuffer.allocate(4 + 40); //taille + nom de la bdd
		buffer.putInt(44);
		for(char c : name.toCharArray()){
			buffer.putChar(c);
		}
		for(int i = buffer.position(); i < 40; i++){
			buffer.putChar(' ');
		}
		try {
			filechannel.write(buffer, filechannel.size());
			databases.put(44, name);
			System.out.println("Created database " + name);
		} catch (IOException e) {
			System.out.println("Failed creating database " + name);
			e.printStackTrace();
		}
		//updateDatabases();
	}

	public void DeleteFile(String string, String string2) {
		// TODO Auto-generated method stub
		
	}

	public void DeleteDatabase(String string) {
		// TODO Auto-generated method stub
		
	}

	public void getDatabases() {
		// TODO Auto-generated method stub
		
	}

	public void getFiles(String db, String filter) {
		// TODO Auto-generated method stub
		
	}

}