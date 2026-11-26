package eu.frezilla.tools.appmonitoring.domain;

import java.time.LocalDate;

public record DateRange(LocalDate from, LocalDate to) {

    public DateRange {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Les dates sont obligatoires");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début");
        }
    }

    public boolean contains(LocalDate date) {
        if (date == null)  {
            return false;
        }
        return !date.isBefore(from) && !date.isAfter(to);
    }

}
