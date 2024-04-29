import javax.swing.*;
import java.awt.*;
import java.util.List;

// The main class for the Notes App GUI
public class NotesApp extends JFrame 
{
    // GUI components
    private JTextArea noteTextArea; // Text area for displaying/editing notes
    private JTextField subjectField; // Text field for entering subject of notes
    private JList<String> noteList; // List to display saved notes

    // Database instance to interact with
    private SQLDB db;

    // Constructor for the NotesApp class
    public NotesApp() 
    {
        super("Notes App"); // Setting the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exiting the application on window close

        setSize(600, 400); // Setting initial size of the window

        // Creating the main panel to hold all components
        JPanel panel = new JPanel(new BorderLayout());

        // Creating text area for note editing
        noteTextArea = new JTextArea();
        noteTextArea.setPreferredSize(new Dimension(200, 400)); // Setting preferred size

        // Initializing the subject field for note subjects
        subjectField = new JTextField();

        // Creating buttons for saving, deleting, and showing notes
        JButton saveButton = new JButton("Save");
        JButton deleteButton = new JButton("Delete");
        JButton showButton = new JButton("Show");

        // Creating a list model for the note list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        noteList = new JList<>(listModel); // Creating the JList with the list model
        noteList.setPreferredSize(new Dimension(200, 400)); // Setting preferred size

        // Adding scroll functionality to the note list
        JScrollPane scrollPane = new JScrollPane(noteList);

        // Creating a panel for the left side of the window to display the note list
        JPanel leftPanel = new JPanel(new BorderLayout());

        // Adding label for note list
        leftPanel.add(new JLabel("Saved Notes"), BorderLayout.NORTH);

        // Adding the scrollable note list to the left panel
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Adding the left panel and note text area to the main panel
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(noteTextArea, BorderLayout.CENTER);

        // Creating a panel for the subject field and adding it to the UI
        JPanel subjectPanel = new JPanel(new BorderLayout());
        subjectPanel.add(new JLabel("Subject: "), BorderLayout.WEST);
        subjectPanel.add(subjectField, BorderLayout.CENTER);
        panel.add(subjectPanel, BorderLayout.NORTH); // Adding the subject panel to the UI

        // Creating a panel for buttons and adding it to the UI
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Adding the main panel to the JFrame
        add(panel);

        // Initializing the database and connecting to it
        db = new SQLDB();
        db.initDatabase();
        db.connect();

        // Fetching all saved notes from the database and displaying them in the note list
        List<String> notes = db.getAllNotes();
        
        for (String note : notes) 
        {
            listModel.addElement(note);
        }

        // Action listener for the save button
        saveButton.addActionListener(e -> {
            String noteText = noteTextArea.getText(); // Getting text from note text area
            String subject = subjectField.getText(); // Getting subject from the subject field
            db.saveNoteWithSubject(noteText, subject); // Saving note with subject
            listModel.clear(); // Clearing the list model
            List<String> updatedNotes = db.getAllNotes(); // Fetching updated notes from database
            
            // Adding updated notes to the list model
            for (String note : updatedNotes) 
            {
                listModel.addElement(note);
            }
        });

        // Action listener for the delete button
        deleteButton.addActionListener(e -> {
            String selectedNote = noteList.getSelectedValue(); // Getting selected note
            // Checking if a note is selected
            if (selectedNote != null) 
            {
                db.deleteNote(selectedNote); // Deleting the selected note from database
                listModel.removeElement(selectedNote); // Removing the note from the list model
            } 
            
            else 
            {
                JOptionPane.showMessageDialog(this, "Please select a note to delete!"); // Error message if no note is selected
            }
        });

        // Action listener for the show button
        showButton.addActionListener(e -> {
            String selectedNote = noteList.getSelectedValue(); // Getting selected note
            // Checking if a note is selected
            if (selectedNote != null)
             {
                noteTextArea.setText(selectedNote); // Displaying the selected note in the text area
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Please select a note to show!"); // Error message if no note is selected
            }
        });

        setVisible(true); // Making the window visible
    }

    // Main method to start the application
    public static void main(String[] args) 
    {
        new NotesApp(); // Creating an instance of the NotesApp class
    }
}
