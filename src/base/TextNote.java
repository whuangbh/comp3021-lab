package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class TextNote extends Note {
	String content;
	private static final long serialVersionUID = 1L;

	public TextNote(String title) {
		super(title);
	}

	public TextNote(String title, String content) {
		super(title);
		this.content = content;
	}

	public TextNote(File f) {
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}

	public String getTextFromFile(String absolutePath) {
		String result = "";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader buf = null;
		try{
			fis = new FileInputStream(absolutePath);
			isr = new InputStreamReader(fis);
			buf = new BufferedReader(isr);
			String currentLine = null;
			while ((currentLine = buf.readLine()) != null) {
				result = result + currentLine + "\n";
			}
			buf.close();
			isr.close();
			fis.close();
		} catch (FileNotFoundException ffe) {
			System.out.println("File not found! ");
		} catch (IOException ioe) {
			System.out.println("IO Exception! ");
		}
		return result;
	}

	public void exportTextToFile(String pathFolder) {
		//FileWriter fw = null;
		//BufferedWriter bw = null;

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		if (pathFolder == "")
			pathFolder = ".";

		/*
		File file = new File(System.getProperty("user.dir") +
				 pathFolder +
				 File.separator +
				 this.getTitle().replaceAll(" ", "_") +
				 ".txt");
		*/
		File file = new File( pathFolder + File.separator + this.getTitle().replaceAll(" ", "_") + ".txt") ;

		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos);
			osw.write(this.content);
			osw.flush();
			osw.close();
			fos.close();

			/*
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(this.content);
			bw.flush();
			bw.close();
			fw.close();
			*/
		}catch (Exception e) {
			System.out.println("Error !");
			e.printStackTrace();
		}
	}
}
