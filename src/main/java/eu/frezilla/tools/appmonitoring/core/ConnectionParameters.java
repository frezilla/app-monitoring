package eu.frezilla.tools.appmonitoring.core;

public record ConnectionParameters(
        String server,
        int port,
        String database,
        String username,
        String password
        ) {

    public String getJdbcUrl() {
        return "jdbc:mysql://"
                + server
                + ":"
                + port
                + "/"
                + database
                + "?serverTimezone=Europe/Paris";
    }

}
