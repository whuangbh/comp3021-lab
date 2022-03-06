package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder implements Comparable<Folder> {

	private ArrayList<Note> notes;
	private String name;

	public Folder(String name) {
		this.name = name;
		notes = new ArrayList<Note>();
	}

	public void addNote(Note note) {
		notes.add(note);
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<Note> getNotes() {
		return this.notes;
	}

	public String toString() {
		int nText = 0;
		int nImage = 0;

		for(Note n: notes) {
			if (n instanceof TextNote) {
				nText++;
			}
			else{
				nImage++;
			}
		}

		return name + ":" + nText + ":" + nImage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Folder)) {
			return false;
		}
		Folder other = (Folder) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Folder o) {
		if (this.name.compareTo(o.name) == 0) {	//same name
			return 0;
		}
		else if (this.name.compareTo(o.name) > 0) { //this's name is larger than argument's name
			return 1;
		}
		else {
			return -1; //this's name is smaller than argument's name
		}
	}

	public void sortNotes() {
		Collections.sort(this.notes);
	}

	public List<Note> searchNotes(String keywords) {
		List<Note> toBeReturn = new ArrayList<Note>();
		String[] tokens = keywords.split(" ", 0);	//read the keywords and split them into tokens

		/*
		for (int i = 0; i < tokens.length; i++)
			System.out.print(tokens[i]+" ");
		*/

		for (Note n: notes) {	//search through all the notes

			//System.out.println("now searching: "+ n);

			boolean satisfied = true;	//set up a flag

			for (int i = 0; i < tokens.length && satisfied != false ; i++) {
				if (tokens[i+1].equals("OR") || tokens[i+1].equals("or")) { //or relationship

					int originalPoisition = i;
					int arraySize = 1;
					while ((i+1 != tokens.length) &&
						   (tokens[i+1].equals("OR") || tokens[i+1].equals("or"))) {
						i = i+2;
						arraySize++;
						//System.out.println(i+" ");
						//System.out.println(arraySize);
					}
					String[] orOption = new String[arraySize];
					for (int j = originalPoisition, currentPos = 0; j <= i; j = j+2, currentPos++) {
						//System.out.println("j = "+j+", currentPos = "+currentPos);
						orOption[currentPos] = tokens[j];
						//System.out.println("orOption["+currentPos+"] = tokens["+j+"]");
					}

					/*
					for (int k = 0; k < orOption.length; k++) {
						System.out.print(orOption[k]+" ");
					}
					System.out.println(" ");
					*/

					boolean orFlag = false;
					for(String s: orOption)
					{
						if (n instanceof ImageNote) {	//n is ImageNote
							if (n.getTitle().toLowerCase().contains(s.toLowerCase())) {
								orFlag = true;	//if title doesn't contains token
							}
						}
						else {							//n is TextNote
							if (n.getTitle().toLowerCase().contains(s.toLowerCase()) ||
								((TextNote)n).content.toLowerCase().contains(s.toLowerCase())) {
								orFlag = true;	//if title and content doesn't contains token
							}
						}
					}
					if (orFlag == false) {
						satisfied = false;
					}

					/*
					System.out.print("finish checking ");
					for (int k = 0; k < orOption.length; k++) {
						System.out.print(orOption[k]+" ");
					}
					System.out.println();
					System.out.println("orFlag = " + orFlag);
					*/

				}
				else {	//and relationship
					if (n instanceof ImageNote) {	//n is ImageNote
						if (!n.getTitle().toLowerCase().contains(tokens[i].toLowerCase())) {
							satisfied = false;	//if title doesn't contains token
						}
					}
					else {							//n is TextNote
						if (!n.getTitle().toLowerCase().contains(tokens[i].toLowerCase()) &&
							!((TextNote)n).content.toLowerCase().contains(tokens[i].toLowerCase())) {
							satisfied = false;	//if title and content doesn't contains token
						}
					}
				}
			}

			if (satisfied == true) {	//if pass all the tokens check
				toBeReturn.add(n);	//add to return list
			}
		}
		return toBeReturn;
	}

}
