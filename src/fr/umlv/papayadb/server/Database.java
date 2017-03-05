package fr.umlv.papayadb.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Database {
	private RandomAccessFile raf;
	private FileChannel filechannel;
	private MappedByteBuffer buffer;
	private HashMap<Integer, String> files = new HashMap<>(); //address, name
	
	public Database(String name) {
		Path path = Paths.get(name + ".db");
		//checks if path exists, otherwise creates it
		if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.createFile(path);
				System.out.println("Creating database " + name);
			} catch (IOException e) {
				System.out.println("Failed database " + name);
			}
		}
		//opens file
		try {
			raf = new RandomAccessFile(path.toFile(), "rw");
		} catch (FileNotFoundException e) {
			System.out.println("Failed opening database " + name);
		}
		//file to filechannel
		filechannel = raf.getChannel();
		//filechannel to buffer
		try {
			buffer = filechannel.map(MapMode.READ_WRITE, 0, filechannel.size());
		} catch (IOException e) {
			System.out.println("failed mapping database " + name);
			e.printStackTrace();
		}
		
		updateFiles();
	}
	
	public void updateFiles(){
		buffer.position(0);
		while(buffer.hasRemaining()) {
			int address = buffer.position();
			int size = buffer.getInt(); //on get la taille du fichier
			if(size < 44) { //si aucune taille indiquée, aucun fichier
				//avancez quand même ?
				return;
			} else {
				byte[] name = new byte[40];
				buffer.get(name, 0, 40); //on get le nom du premier fichier
				System.out.println(address + " : " + new String(name));
				files.put(address, new String(name)); //on l'ajoute à la hashmap
				buffer.position(address + size); //on set la position du buffer à la prochaine bdd
			}
		}
		System.out.println("Number of files : " + files.size());
	}
	
	public void insertFile(String db, String file, String data) {
		//CHECK SI LE NOM DU FICHIER EXISTE DEJA DANS LA BDD
		int size = 4 + 40 + data.getBytes().length; //taille + nom du fichier + fichier		
		int address = buffer.capacity();
		try {
			//adjust size of papaya.db & remap buffer
			raf.setLength(raf.length() + size);
			filechannel = raf.getChannel();
			buffer = filechannel.map(MapMode.READ_WRITE, 0, filechannel.size());
			//set position to former capacity
			buffer.position(address);
			//put size and name
			buffer.putInt(size);
			buffer.put(file.getBytes());
			//fill gap
			for(int i = file.length(); i < 20; i++){
				buffer.putChar(' ');
			}
			files.put(address, file);
			System.out.println("Created file " + file);
		} catch (IOException e) {
			System.out.println("Failed creating file " + file);
			//e.printStackTrace();
		}
	}

	public void DeleteFile(String name) {
		
	}

	public void DeleteDatabase() {
		
	}

	public HashMap<Integer, String> getFiles(String db, String filter) {
		files.forEach((address, name) -> {
			System.out.println(address + " " + name);
		});
		return files;
	}

	public File getFile(String header) {
		// TODO Auto-generated method stub
		
	}

}