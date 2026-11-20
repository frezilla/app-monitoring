package eu.fezilla.tools.appmonitoring.swing;

import eu.frezilla.tools.appmonitoring.core.PoolVariables;
import eu.frezilla.tools.appmonitoring.core.ConnectionParameters;
import eu.frezilla.tools.appmonitoring.core.DatabaseManager;
import java.awt.Dimension;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;

/**
 * Fenêtre de lancement de l'application.
 * <p>Elle permet à l'utilisateur de se connecter au serveur de base de données.
 */
public class LoginFrame extends JFrame {
    
    private final JTextField serverField = new JTextField("localhost", 20);
    private final JSpinner portField = new JSpinner(new SpinnerNumberModel(3306, 1, 65535, 1));
    private final JTextField databaseField = new JTextField("alize", 20);
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton connectButton = new JButton("Connexion");
    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel statusLabel = new JLabel(" ");

    private final DatabaseManager databaseManager;

    public LoginFrame() {
        databaseManager = new DatabaseManager();
        PoolVariables.getDefault().putObjects("databaseManager", databaseManager);

        initializeFrame();
        createUI();
        initializeEvents();
    }
    
    private void connect() {
        if (!validateForm()) {
            return;
        }

        ConnectionParameters parameters = new ConnectionParameters(
                serverField.getText().trim(),
                (Integer) portField.getValue(),
                databaseField.getText().trim(),
                usernameField.getText().trim(),
                new String(passwordField.getPassword())
        );
        setConnecting(true);

        
        // La connexion JDBC ne doit pas être effectuée depuis l'Event Dispatch Thread Swing.
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                databaseManager.connect(parameters);
                return null;
            }

            @Override
            protected void done() {
                setConnecting(false);

                try {
                    get();
                    connectionSuccessful();
                } catch (InterruptedException | ExecutionException e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    connectionFailed(cause);
                }
            }
        };

        worker.execute();
    }
    
    private void connectionFailed(Throwable exception) {
        statusLabel.setText("Echec de la connexion.");
        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Erreur de connexion",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void connectionSuccessful() {
        statusLabel.setText("Connexion établie.");

        JOptionPane.showMessageDialog(
                this,
                "Connexion à la base réussie.",
                "Connexion",
                JOptionPane.INFORMATION_MESSAGE
        );

        new DashBoardFrame(databaseManager).setVisible(true);
         
         dispose();
    }    
    
    private void createUI() {
        JPanel panel
                = new JPanel(
                        new MigLayout(
                                "insets 30, fillx, wrap 2",
                                "[][grow,fill]",
                                "[]15[]10[]10[]10[]10[]20[]10[]"
                        )
                );

        // Titre
        JLabel title = new JLabel("Connexion au serveur");
        title.putClientProperty("FlatLaf.styleClass", "h1");
        panel.add(title, "span 2, center, gapbottom 20");

        // Serveur
        panel.add(new JLabel("Serveur :"));
        panel.add(serverField);

        // Port
        panel.add(new JLabel("Port :"));
        panel.add(portField);

        // Base 
        panel.add(new JLabel("Base de données :"));
        panel.add(databaseField);

        // Utilisateur
        panel.add(new JLabel("Utilisateur :"));
        panel.add(usernameField);

        // Mot de passe
        panel.add(new JLabel("Mot de passe :"));
        panel.add(passwordField);

        // Bouton
        connectButton.putClientProperty("JButton.buttonType", "default");
        panel.add(connectButton, "span 2, growx, height 40!, gaptop 15");

        // Barre de progression
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        panel.add(progressBar, "span 2, growx");

        // Statut
        panel.add(statusLabel, "span 2, center");
        setContentPane(panel);
    }    

    private void initializeEvents() {
        // Bouton
        connectButton.addActionListener(e -> connect());

        // Touche Entrée depuis le mot de passe
        passwordField.addActionListener(e -> connect());
    }
    
    private void initializeFrame() {
        setTitle("Connexion à la base de données");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(380, 420));
        setResizable(false);
    }
    
    private void setConnecting(boolean connecting) {
        serverField.setEnabled(!connecting);
        portField.setEnabled(!connecting);
        databaseField.setEnabled(!connecting);
        usernameField.setEnabled(!connecting);
        passwordField.setEnabled(!connecting);

        connectButton.setEnabled(!connecting);

        progressBar.setVisible(connecting);

        if (connecting) {
            statusLabel.setText("Connexion en cours...");
        } else {
            statusLabel.setText(" ");
        }
    }    

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Paramètre incorrect",
                JOptionPane.WARNING_MESSAGE
        );
    }
    
    private boolean validateForm() {
        if (serverField.getText().isBlank()) {
            showValidationError("Le serveur doit être renseigné.");
            serverField.requestFocus();
            return false;
        }

        if (databaseField.getText().isBlank()) {
            showValidationError("La base de données doit être renseignée.");
            databaseField.requestFocus();
            return false;
        }

        if (usernameField.getText().isBlank()) {
            showValidationError("L'utilisateur doit être renseigné.");
            usernameField.requestFocus();
            return false;
        }

        return true;
    }

    

}
