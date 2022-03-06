package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook {

	private ArrayList<Folder> folders;

	public NoteBook() {
		folders = new ArrayList<Folder>();
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

}
