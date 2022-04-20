package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NoteBook implements java.io.Serializable{

	private ArrayList<Folder> folders;
	private static final long serialVersionUID = 1L;

	public NoteBook() {
		folders = new ArrayList<Folder>();
	}

	public NoteBook(String file) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			NoteBook n = (NoteBook)in.readObject();
			this.folders = n.folders;
			in.close();
			fis.close();
		} catch(Exception e) {
				e.printStackTrace();
		}
	}

	public boolean createTextNote(String folderName, String title) {
		TextNote note = new TextNote(title);
		return insertNote(folderName, note);
	}

	public boolean createTextNote(String folderName, String title, String content) {
		TextNote note = new TextNote(title, content);
		return insertNote(folderName, note);
	}

	public boolean createImageNote(String folderName, String title) {
		ImageNote note = new ImageNote(title);
		return insertNote(folderName, note);
	}

	public ArrayList<Folder> getFolders() {
		return this.folders;
	}

	public boolean insertNote(String folderName, Note note) {
		//Step 1
		Folder targetFolder = null;

		for(Folder f1: folders) {	//check if the folder already exists
			if (f1.getName().equals(folderName)) {
				targetFolder = f1;
			}
		}

		if (targetFolder == null) {	//if the folder doesn't exists, create a new folder
			targetFolder = new Folder(folderName);
			folders.add(targetFolder);	//and add to the NoteBook
		}

		//Step 2
		boolean sameTitle = false;

		for(Note n: targetFolder.getNotes()) {	//check if note with the same title exists
			if (n.equals(note)) {
				sameTitle = true;
			}
		}

		if (sameTitle == true) {
			System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed");
			return false;
		}
		else {
			targetFolder.addNote(note);
			return true;
		}
	}

	public void sortFolders() {
		for (Folder f: folders) {
			f.sortNotes();
		}

		Collections.sort(folders);
	}

	public List<Note> searchNotes(String keywords) {
		List<Note> toBeReturn = new ArrayList<Note>();
		for (Folder f: folders) {
			toBeReturn.addAll(f.searchNotes(keywords));
		}
		return toBeReturn;
	}

	public boolean save(String file) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try{
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
			fos.close();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void addFolder(String folderName) {	//Lab 8
		Folder f = new Folder(folderName);
		folders.add(f);
	}

}
