package eu.frezilla.tools.appmonitoring.swing.panel;

import eu.frezilla.tools.appmonitoring.service.user.AccountStatusReport;
import eu.frezilla.tools.appmonitoring.service.user.UserService;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class UserPanel extends JPanel {
    
    private final UserService userService = new UserService();
    
    public UserPanel() {
        createUI();
    }
    
    private JPanel createAccountStatusPanel() {
        AccountStatusReport report = userService.generateAccountStatusReport();
        
        DefaultPieDataset dataSet = new DefaultPieDataset();
        dataSet.setValue("Comptes actifs", report.getActiveRecord().count());
        dataSet.setValue("Comptes inactifs", report.getInactiveRecord().count());
        dataSet.setValue("Comptes désactivés", report.getDisabledRecord().count());
        
        JFreeChart chart = ChartFactory.createPieChart(
                "Statut des comptes utilisateurs",
                dataSet,
                true,
                true,
                Locale.FRANCE
        );
        
        return new ChartPanel(chart);
    }
    
    private void createUI() {
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 1, 1);
        
        add(createAccountStatusPanel(), gbc);
    }
    
}
