package base;

import java.util.Date;

public class Note implements Comparable<Note>, java.io.Serializable {

	private Date date;
	private String title;
	private static final long serialVersionUID = 1L;

	public Note(String title) {
		this.title = title;
		date = new Date(System.currentTimeMillis());
	}

	public String getTitle() {return this.title;}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		if (!(obj instanceof Note)) {
			return false;
		}
		Note other = (Note) obj;
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Note o) {
		if (this.date.compareTo(o.date) == 0) {	//same date
			return 0;
		}
		else if (this.date.compareTo(o.date) > 0) { //this is more recent than argument
			return -1;	//this is smaller than argument
		}
		else {
			return 1; //this is larger than argument
		}
	}

	public String toString() {
		return date.toString() + "\t" + title;
	}

}
