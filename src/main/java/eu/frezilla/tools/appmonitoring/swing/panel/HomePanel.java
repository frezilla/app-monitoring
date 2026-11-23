package eu.frezilla.tools.appmonitoring.swing.panel;

import eu.frezilla.tools.appmonitoring.service.UserService;
import javax.swing.JPanel;

public class HomePanel extends JPanel {
    
    private final UserService userService = new UserService();

    public HomePanel() {
        userService.run();
    }
    
}
