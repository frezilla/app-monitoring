package eu.frezilla.tools.appmonitoring.swing.panel;

import eu.frezilla.tools.appmonitoring.core.DatabaseManager;
import java.util.Objects;
import javax.swing.JPanel;

public class WeekEndPanel extends JPanel {
    
    private final DatabaseManager databaseManager;

    public WeekEndPanel(DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
    }
    
}
