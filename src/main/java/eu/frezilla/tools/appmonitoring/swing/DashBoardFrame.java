package eu.frezilla.tools.appmonitoring.swing;

import eu.frezilla.tools.appmonitoring.swing.panel.HomePanel;
import eu.frezilla.tools.appmonitoring.swing.panel.SettingsPanel;
import eu.frezilla.tools.appmonitoring.swing.panel.WeekEndPanel;
import eu.frezilla.tools.appmonitoring.core.DatabaseManager;
import eu.frezilla.tools.appmonitoring.swing.panel.UserPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Objects;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

public class DashBoardFrame extends JFrame {

    private final DatabaseManager databaseManager;

    private final JTabbedPane tabbedPane;

    public DashBoardFrame(DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
        this.tabbedPane = new JTabbedPane();

        initializeFrame();
        createUI();
    }

    private void initializeFrame() {
        setTitle("Database Client");

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setSize(1200, 800);
        setLocationRelativeTo(null);

        /*
         * La croix Windows passe également
         * par notre méthode de fermeture.
         */
        addWindowListener(
                new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                exit();
            }
        }
        );
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        /*
         * Préférences
         */
        JButton preferencesButton
                = new JButton("Préférences");

        preferencesButton.setFocusable(false);

        preferencesButton.addActionListener(
                e -> showPreferences()
        );

        toolBar.add(
                preferencesButton
        );

        toolBar.addSeparator();

        /*
         * À propos
         */
        JButton aboutButton
                = new JButton("À propos de");

        aboutButton.setFocusable(false);

        aboutButton.addActionListener(
                e -> showAbout()
        );

        toolBar.add(
                aboutButton
        );

        /*
         * Espace extensible :
         * le bouton Quitter part complètement à droite.
         */
        toolBar.add(
                Box.createHorizontalGlue()
        );

        toolBar.addSeparator();

        /*
         * Quitter
         */
        JButton quitButton
                = new JButton("Quitter");

        quitButton.setFocusable(false);

        quitButton.addActionListener(
                e -> exit()
        );

        toolBar.add(
                quitButton
        );

        return toolBar;
    }
    
    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Barre supérieure
        JToolBar toolBar = createToolBar();
        mainPanel.add(toolBar, BorderLayout.NORTH);

        // Onglets
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.addTab("Accueil", new HomePanel());
        tabbedPane.addTab("Utilisateurs", new UserPanel());
        tabbedPane.addTab("Connexion le week-end", new WeekEndPanel(databaseManager));
        tabbedPane.addTab("Paramètres", new SettingsPanel());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    

    private void showPreferences() {

        /*
         * Pour l'instant on sélectionne
         * directement l'onglet Paramètres.
         */
        for (int i = 0;
                i < tabbedPane.getTabCount();
                i++) {

            if ("Paramètres".equals(
                    tabbedPane.getTitleAt(i))) {

                tabbedPane.setSelectedIndex(i);

                return;
            }
        }
    }

    private void showAbout() {

        JLabel application
                = new JLabel("Database Client");

        application.setFont(
                application
                        .getFont()
                        .deriveFont(
                                Font.BOLD,
                                20f
                        )
        );

        JLabel version
                = new JLabel("Version 1.0.0");

        JLabel runtime
                = new JLabel(
                        "Java "
                        + System.getProperty(
                                "java.version"
                        )
                );

        JLabel description
                = new JLabel(
                        "Client de gestion de base de données"
                );

        JPanel panel
                = new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        application.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        version.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        runtime.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        description.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(application);

        panel.add(
                Box.createVerticalStrut(10)
        );

        panel.add(description);

        panel.add(
                Box.createVerticalStrut(15)
        );

        panel.add(version);

        panel.add(runtime);

        JOptionPane.showMessageDialog(
                this,
                panel,
                "À propos de Database Client",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void exit() {

        int result
                = JOptionPane.showConfirmDialog(
                        this,
                        "Voulez-vous vraiment quitter l'application ?",
                        "Quitter",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        /*
         * Fermeture propre du pool HikariCP.
         */
        if (databaseManager != null) {
            databaseManager.close();
        }

        dispose();
    }

}
