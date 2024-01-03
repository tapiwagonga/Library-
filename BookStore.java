import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookStore {
    private final Map<String, User> users;
    private List<Book> books;
    private User currentUser;

    public BookStore () {
        this.users = new HashMap<>();
        this.books = new ArrayList<>();
    }

    public Book getBookDetails(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    public void registerUser(String username, String password) {
        users.put(username, new User(username, password));
        System.out.println("User registered. ");
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Logged In. ");
            currentUser = user; // Set the currentUser
            return user;
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public boolean addBook(String title, String author, String content) {
        boolean added = books.add(new Book(title, author, content ));
        if (added) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Failed to add the book.");
        }
        return added;
    }

    public boolean deleteBook(String title) {
        Book bookToDelete = null;
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                bookToDelete = book;
                break;
            }
        }

        if (bookToDelete != null) {
            books.remove(bookToDelete);
            System.out.println("Book deleted.");
            return true;
        } else {
            System.out.println("Book not found.");
            return false;
        }
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public User getCurrentUser() {
        return currentUser;
    }


}
