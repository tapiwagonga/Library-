import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.*;

public class Library extends JFrame {
    private JTextArea message;
    private final BookStore bookStore;
    private User currentUser;

    public Library(BookStore bookStore, User currentUser) {
        this.bookStore = bookStore;
        this.currentUser = currentUser;

        setTitle("Online Library");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        messagePanel();

        setVisible(true);
    }

    private void messagePanel() {
        message = new JTextArea();
        message.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(message);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton viewBooksButton = new JButton("View Books");
        viewBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBooks();
            }
        });

        JButton deleteBookButton = new JButton("Delete Book (Admin)");
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteBook();
            }
        });
        buttonPanel.add(deleteBookButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logout();
            }
        });

        JButton readBookButton = new JButton("Read Book");
        readBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReadBook();
            }
        });

        buttonPanel.add(viewBooksButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(readBookButton);


        if (currentUser != null && currentUser.getUsername().equals("admin")) {
            JButton addBookButton = new JButton("Add Book (Admin)");
            addBookButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddBook();
                }
            });

            deleteBookButton = new JButton("Delete Book (Admin)");
            deleteBookButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DeleteBook();
                }
            });

            buttonPanel.add(addBookButton);
        }

        JButton submitReviewButton = new JButton("Submit a book review...");
        submitReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitReview();
            }
        });
        buttonPanel.add(submitReviewButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void displayBooks() {
        List<Book> books = bookStore.getBooks();

        if (!books.isEmpty()) {
            StringBuilder bookList = new StringBuilder("List of Books:\n");

            for (Book book : books) {
                bookList.append("Title: ").append(book.getTitle()).append(" Written by  ").append(book.getAuthor()).append("\n");

                // Display reviews if available
                List<Review> reviews = book.getReviews();
                if (!reviews.isEmpty()) {
                    bookList.append("Reviews:\n");
                    for (Review review : reviews) {
                        bookList.append(" ").append(review.getUsername()).append(" rated it : ").append(review.getRating())
                                .append(" stars  ").append(review.getComment()).append("\n\n");
                    }
                } else {
                    bookList.append("No reviews available at the moment.\n");
                }

                bookList.append("\n");
            }

            displayText(bookList.toString());
        } else {
            displayText("No books available.");
        }
    }


    private void AddBook() {
        String title = JOptionPane.showInputDialog("Enter book title :");
        String author = JOptionPane.showInputDialog("Enter author :");
        String filePath = JOptionPane.showInputDialog("Enter file path of the book : ");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder BookContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                BookContent.append(line).append("\n");
            }

            if (bookStore.addBook(title, author, BookContent.toString())) {
                displayBooks();
            } else {
                displayText("Failed to add the book.");
            }
        } catch (IOException e) {
            displayText("Error reading file path " + e.getMessage());
        }
    }

    private void DeleteBook() {
        String title = JOptionPane.showInputDialog("Enter the title of the book to delete:");

        if (bookStore.deleteBook(title)) {
            displayBooks();
        } else {
            displayText("Failed to delete the book. Try again: ");
        }
    }

    private void Logout() {
        new Login(bookStore);
        dispose();
    }

    private void ReadBook() {
        List<Book> books = bookStore.getBooks();

        if (!books.isEmpty()) {
            String[] Titles = new String[books.size()];
            for (int i = 0; i < books.size(); i++) {
                Titles[i] = books.get(i).getTitle();
            }

            String selectedBookTitle = (String) JOptionPane.showInputDialog(
                    this,
                    "Select the book you want to read :",
                    "Read Book",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    Titles,
                    Titles[0]
            );

            Book selectedBook = null;
            for (Book book : books) {
                if (book.getTitle().equals(selectedBookTitle)) {
                    selectedBook = book;
                    break;
                }
            }

            if (selectedBook != null) {
                bookDetails(selectedBook);
            } else {
                displayText("Book not available.");
            }
        } else {
            displayText("No books available right now. Check Later ");
        }
    }

    private void bookDetails(Book book) {
        StringBuilder bookInfo = new StringBuilder("Book Details: \n");
        bookInfo.append("Title : ").append(book.getTitle()).append("\n");
        bookInfo.append("Author : ").append(book.getAuthor()).append("\n\n");

        bookInfo.append("BookContent:\n").append(book.getBookContent()).append("\n");

        displayText(bookInfo.toString());
    }

    private void displayText(String textmessage) {
        message.setText(textmessage);
    }

    private void submitReview() {
        List<Book> books = bookStore.getBooks();

        if (books.isEmpty()) {
            displayText("No books available right now. Check Later.");
            return;
        }

        String[] bookTitles = new String[books.size()];
        for (int i = 0; i < books.size(); i++) {
            bookTitles[i] = books.get(i).getTitle();
        }

        String selectedBookTitle = (String) JOptionPane.showInputDialog(
                this,
                "Select the book to review:",
                "Submit Review",
                JOptionPane.QUESTION_MESSAGE,
                null,
                bookTitles,
                bookTitles[0]
        );

        Book selectedBook = null;
        for (Book book : books) {
            if (book.getTitle().equals(selectedBookTitle)) {
                selectedBook = book;
                break;
            }
        }

        if (selectedBook != null) {
            int rating = Integer.parseInt(JOptionPane.showInputDialog("Rate the book between 1-5:"));
            String comment = JOptionPane.showInputDialog("Enter any comments:");

            selectedBook.addReview(new Review(currentUser.getUsername(), rating, comment));
            displayText("Review submitted.");
        } else {
            displayText("Book not available. ");
        }

    }}

