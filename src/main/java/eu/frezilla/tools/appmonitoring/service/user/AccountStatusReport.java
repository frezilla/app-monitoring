package eu.frezilla.tools.appmonitoring.service.user;

public final class AccountStatusReport {
    
    private final Record activeRecord;
    private final Record disabledRecord;
    private final Record inactiveRecord;
    
    AccountStatusReport(long nbActives, long nbInactive, long nbDisabled) {
        activeRecord = new Record(AccountStatus.ACTIVE, nbActives);
        disabledRecord = new Record(AccountStatus.DISABLED, nbDisabled);
        inactiveRecord = new Record(AccountStatus.INACTIVE, nbInactive);
    }

    public Record getActiveRecord() {
        return activeRecord;
    }

    public Record getDisabledRecord() {
        return disabledRecord;
    }

    public Record getInactiveRecord() {
        return inactiveRecord;
    }
    
    public static enum AccountStatus {
        ACTIVE,
        INACTIVE,
        DISABLED
    }
    
    public static record Record(AccountStatus status, long count) {
        
        public Record {
            if (status == null) {
                throw new IllegalArgumentException("Le statut est obligatoire");
            }
            if (count < 0) {
                throw new IllegalArgumentException("Le nombre ne peut pas être inférieur à zéro");
            }
        }
    }
}
