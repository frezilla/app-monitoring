package eu.frezilla.tools.appmonitoring;

public class Launcher {
    
    public static void main(String[] args) {
        try {
            SwingMonitoring.main(args);
            System.exit(0);
        } catch (Exception e) {
            System.exit(-1);
        }
    }
    
}
