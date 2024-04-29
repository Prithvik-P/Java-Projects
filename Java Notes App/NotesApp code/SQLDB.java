import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Class to handle database operations
public class SQLDB
 {
    // Database connection and statement objects
    private Connection connection;
    private Statement statement;

    // Method to initialize the database
    public void initDatabase() 
    {
        try 
        {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Establishing connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ke007", "root", "pr123");

            // Creating a statement object
            Statement statement = connection.createStatement();

            // Creating the 'notes' table if it doesn't exist
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY AUTO_INCREMENT, subject VARCHAR(255), content TEXT)");
        } 
        
        catch (Exception e)
         {
            System.out.println("1 " + e);
        }
    }

    // Method to connect to the database
    public void connect() 
    {
        try 
        {
            // Check if connection is closed or not initialized, then establish a new connection
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ke007", "root", "pr123");
                statement = connection.createStatement();
            }
        } 
        catch (Exception e) 
        {
            System.out.println("2 " + e);
        }
    }

    // Method to save a note with a subject to the database
    public void saveNoteWithSubject(String content, String subject) 
    {
        try 
        {
            // Prepared statement to insert a new note into the 'notes' table
            String query = "INSERT INTO notes (subject, content) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, subject); // Setting the subject parameter
            preparedStatement.setString(2, content); // Setting the content parameter
            preparedStatement.executeUpdate(); // Executing the insert query
        } 
        catch (Exception e) {
            System.out.println("3 " + e);
        }
    }

    // Method to retrieve all notes from the database
    public List<String> getAllNotes() 
    {
        List<String> notes = new ArrayList<>();
        try 
        {
            // Executing a select query to retrieve all notes
            ResultSet resultSet = statement.executeQuery("SELECT * FROM notes");
            while (resultSet.next()) 
            {
                notes.add(resultSet.getString("subject") + " :\n" + resultSet.getString("content")); // Adding subject of each note to the list
            }
        } 
        
        catch (Exception e) 
        {
            System.out.println("4" + e);
        }
        return notes; // Returning the list of notes
    }

    // Method to delete a note from the database
    public void deleteNote(String content) 
    {
        try 
        {
            // Prepared statement to delete a note based on its content
            String query = "DELETE FROM notes WHERE content = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, content); // Setting the content parameter
            preparedStatement.executeUpdate(); // Executing the delete query
        } 
        catch (Exception e) 
        {
            System.out.println("5" + e);
        }
    }

    // Method to close the database connection
    public void close() 
    {
        try 
        {
            if (connection != null && !connection.isClosed()) {
                connection.close(); // Closing the connection
            }
        } 
        catch (Exception e) 
        {
            System.out.println("6" + e);
        }
    }
}
