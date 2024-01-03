import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private BookStore BookStore ;

    public Login(BookStore  BookStore) {
        this.BookStore  = BookStore ;

        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginDetails();

        setVisible(true);
    }

    private void loginDetails() {
        JPanel loginDetails = new JPanel(new GridLayout(4, 2));

        JLabel usernameTag = new JLabel("Enter a username :");
        JLabel passwordLabel = new JLabel("Enter a Password :");

        usernameInput = new JTextField();
        passwordInput= new JPasswordField();

        JButton registerButton = new JButton("Register as a new user ");
        JButton loginButton = new JButton("Login");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserRegistration();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login();
            }
        });

        loginDetails.add(usernameTag);
        loginDetails.add(usernameInput);
        loginDetails.add(passwordLabel);
        loginDetails.add(passwordInput);
        loginDetails.add(new JPanel());
        loginDetails.add(registerButton);
        loginDetails.add(new JPanel());
        loginDetails.add(loginButton);

        add(loginDetails, BorderLayout.CENTER);
    }

    private void UserRegistration() {
        String username = usernameInput.getText();
        String password = new String(passwordInput.getPassword());

        if (BookStore .userExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists.",
                    "Registration Failed", JOptionPane.ERROR_MESSAGE);
        } else {
            BookStore .registerUser(username, password);
            JOptionPane.showMessageDialog(this, "User registered.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void Login() {
        String username = usernameInput.getText();
        String password = new String(passwordInput.getPassword());

        User currentUser = BookStore .login(username, password);

        if (currentUser != null) {
            new Library(BookStore , currentUser);
            dispose(); // Close the login frame after successful login
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
