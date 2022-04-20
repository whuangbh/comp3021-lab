package base;

import java.io.File; //Lab 8
import java.util.ArrayList;
import javafx.stage.FileChooser; //Lab 8
import java.util.List;
import java.util.Optional;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * NoteBook GUI with JAVAFX
 *
 * COMP 3021
 *
 *
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 *
	 * Combobox for selecting the folder
	 *
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";

	/**
	 * current note selected by the user
	 */
	String currentNote = "";

	/**
	 *
	 * the stage of this display
	 */
	Stage stage; //Lab 8

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		this.stage = stage;
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	private void refreshDisplay() {
		textAreaNote.clear();	//clear all the old list/data/text....
		titleslistView.getItems().clear();
		foldersComboBox.getItems().clear();
		currentFolder = "";
		currentSearch = "";

		BorderPane border = new BorderPane();	//create new pane
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	private boolean folderNameCollision(String name) {
		for (Folder f: noteBook.getFolders()) {
			if (f.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This create the top section
	 *
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load from File");
		buttonLoad.setPrefSize(100, 20);
		//buttonLoad.setDisable(true);
		Button buttonSave = new Button("Save to File");
		buttonSave.setPrefSize(100, 20);
		//buttonSave.setDisable(true);

		//Event handler for Load from File/ Save to File (Lab 8)
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object!");

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showOpenDialog(stage);

				if (file != null) {
					loadNoteBook(file);
					refreshDisplay();
				}
			}
		});

		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File To Save The NoteBook!");

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showSaveDialog(stage);

				if (file != null) {
					noteBook.save(file.getPath());

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Successfully saved");
					alert.setContentText("Your file has been saved to file " + file.getName());
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}
			}
		});


		//To-do (Lab 7)
		Label label = new Label("Search : ");
		TextField textSearch = new TextField();
		Button buttonSearch = new Button("Search");
		buttonSearch.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				currentSearch = textSearch.getText();	//get the search keyword from the text field
				textAreaNote.setText("");

				ArrayList<String> list = new ArrayList<String>();
				for (Folder f: noteBook.getFolders()) { //loop through all the folders
					if (currentFolder.equals(f.getName())) {	//find the target folder
						List<Note> searchResult = f.searchNotes(currentSearch);	//perform the searching
						for (Note n: searchResult) {
							list.add(n.getTitle());
						}
						break;
					}
				}
				ObservableList<String> combox2 = FXCollections.observableArrayList(list);
				titleslistView.setItems(combox2);
			}
		});

		Button buttonRemove = new Button("Clear Search");
		buttonRemove.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				currentSearch = "";
				textSearch.setText("");
				textAreaNote.setText("");
				updateListView();
			}
		});

		hbox.getChildren().addAll(buttonLoad, buttonSave, label, textSearch, buttonSearch, buttonRemove);

		return hbox;
	}

	/**
	 * this create the section on the left
	 *
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		// TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
		//foldersComboBox.getItems().addAll("FOLDER NAME 1", "FOLDER NAME 2", "FOLDER NAME 3");
		for (Folder f: this.noteBook.getFolders()) {
			foldersComboBox.getItems().add(f.getName());
		}

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 != null) {
					currentFolder = t1.toString();
					currentNote = "";
				}
				// this contains the name of the folder selected
				// TODO update listview
				updateListView();
			}
		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				currentNote = title;
				for (Folder f: noteBook.getFolders()) {	//loop through all the folders
					if (f.getName().equals(currentFolder)) {	//find the current folder
						for (Note n : f.getNotes()) {	//loop through all the notes
							if (n.getTitle().equals(title)) {	//find the current note
								if (n instanceof TextNote) {
									String content = ((TextNote)n).content;	//load the content
									textAreaNote.setText(content);
								}
								else {
									textAreaNote.setText("This is a ImageNote, text not available");
								}
								break;
							}
						}
						break;
					}
				}
			}
		});
		vbox.getChildren().add(new Label("Choose folder: "));

		//new HBox for ComboBox and Add file button
		HBox hbox2 = new HBox();
		hbox2.setPadding(new Insets(0));
		hbox2.setSpacing(10); // Gap between nodes
		Button addFolder = new Button("Add a Folder");
		addFolder.setPrefSize(100, 20);
		addFolder.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				TextInputDialog dialog = new TextInputDialog("Add a Folder");
				dialog.setTitle("Input");
				dialog.setHeaderText("Add a new folder for your notebook:");
				dialog.setContentText("Please enter the name you want to create:");

				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					if (result.get().equals("")) {	//empty string
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Warning");
						alert.setContentText("Please input a valid folder name");
						alert.showAndWait().ifPresent(rs -> {
							if (rs == ButtonType.OK) {
								System.out.println("Pressed OK.");
							}
						});
					}
					else if (folderNameCollision(result.get())) {	//folder name collision
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Warning");
						alert.setContentText("You already have a folder named with " + result.get());
						alert.showAndWait().ifPresent(rs -> {
							if (rs == ButtonType.OK) {
								System.out.println("Pressed OK.");
							}
						});
					}
					else {	//success
						noteBook.addFolder(result.get());
						refreshDisplay();
						currentFolder = result.get();
						foldersComboBox.getSelectionModel().select(foldersComboBox.getItems().size()-1);
					}
				}
			}
		});
		hbox2.getChildren().addAll(foldersComboBox, addFolder);

		vbox.getChildren().add(hbox2);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);

		//new Add note button
		Button addNote = new Button("Add a Note");
		addNote.setPrefSize(100, 20);
		addNote.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (currentFolder.equals("") || currentFolder.equals("-----")) {			//folder not selected
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Please choose a folder first!");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}
				else {											//success
					TextInputDialog dialog = new TextInputDialog("Add a Note");
					dialog.setTitle("Input");
					dialog.setHeaderText("Add a new note to current folder");
					dialog.setContentText("Please enter the name of your note:");

					Optional<String> result = dialog.showAndWait();
					if (result.isPresent()) {
						noteBook.createTextNote(currentFolder, result.get());
						updateListView();
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Successful!");
						alert.setContentText("Insert note " + result.get() + " to folder " + currentFolder + " successfully!");
						alert.showAndWait().ifPresent(rs -> {
							if (rs == ButtonType.OK) {
								System.out.println("Pressed OK.");
							}
						});
					}
				}
			}

		});

		vbox.getChildren().add(addNote);
		return vbox;
	}

	private void updateListView() {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		for (Folder f: this.noteBook.getFolders()) {	//loop through all the folders
			if (f.getName().equals(currentFolder)) { //find the current folder
				for (Note n: f.getNotes()) {
					list.add(n.getTitle());	//add all the name of the notes
				}
				break;
			}
		}

		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);

		//Lab 8
		HBox hbox3 = new HBox();
		hbox3.setPadding(new Insets(0));
		hbox3.setSpacing(10); // Gap between nodes

		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);
		hbox3.getChildren().add(saveView);

		Button saveNote = new Button("Save Note");
		saveNote.setPrefSize(100, 20);
		hbox3.getChildren().add(saveNote);
		saveNote.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println("currentFolder = " + currentFolder);
				//System.out.println("currentNote = " + currentNote);

				if (currentFolder.equals("") || currentFolder.equals("-----") || currentNote.equals("")) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note!");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}
				else {
					//System.out.println("haha");
					for (Folder f: noteBook.getFolders()) {
						if (f.getName().equals(currentFolder)) {
							//System.out.println("hehe");
							for (Note n: f.getNotes()) {
								if (n.getTitle().equals(currentNote)) {
									//System.out.println("huhu");
									((TextNote)n).content = textAreaNote.getText();
									//System.out.println("textAreaNote = " + textAreaNote.getText());
									break;
								}
							}
							break;
						}
					}
				}
			}
		});

		ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
		deleteView.setFitHeight(18);
		deleteView.setFitWidth(18);
		deleteView.setPreserveRatio(true);
		hbox3.getChildren().add(deleteView);

		Button deleteNote = new Button("Delete Note");
		deleteNote.setPrefSize(100, 20);
		hbox3.getChildren().add(deleteNote);
		deleteNote.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (currentFolder.equals("") || currentFolder.equals("-----") || currentNote.equals("")) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note!");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}
				else {
					for (Folder f: noteBook.getFolders()) {
						if (f.getName().equals(currentFolder)) {
							f.removeNote(currentNote);
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Succeed!");
							alert.setContentText("Your note havs been successfully removed");
							alert.showAndWait().ifPresent(rs -> {
								if (rs == ButtonType.OK) {
									System.out.println("Pressed OK.");
								}
							});
							updateListView();
							currentNote = "";
							break;
						}
					}
				}
			}

		});

		// 0 0 is the position in the grid
		grid.add(hbox3, 0, 0);
		grid.add(textAreaNote, 0, 1);

		return grid;
	}

	private void loadNoteBook(File file) { //Lab 8
		//System.out.println(file.getPath());
		noteBook = new NoteBook(file.getPath());
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();

		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called ¡§the most shocking play in NFL history¡¨ and the Washington Redskins dubbed the ¡§Throwback Special¡¨: the November 1985 play in which the Redskins¡¦ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award¡Vwinning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything¡Xuntil it wasn¡¦t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant¡Xa part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether¡¦s Daddy Was a Number Runner and Dorothy Allison¡¦s Bastard Out of Carolina, Jacqueline Woodson¡¦s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood¡Xthe promise and peril of growing up¡Xand exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");


		noteBook = nb;

	}

}
