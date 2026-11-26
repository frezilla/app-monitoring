package eu.frezilla.tools.appmonitoring.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.util.Objects;
import org.apache.commons.validator.routines.EmailValidator;

@Entity
@Access(AccessType.PROPERTY)
public class User {
    
    private final static LocalDate FINAL_DATE = LocalDate.of(3000, 12, 31);
    private final static LocalDate INIT_DATE = LocalDate.of(1900, 1, 1);


    private Integer id;
    private LocalDate lastLoginDate = INIT_DATE;
    private String login;
    private String email;
    private LocalDate validFromDate = INIT_DATE;
    private LocalDate validToDate = FINAL_DATE;
    
    protected User() {
        
    }

    @Id
    @Column(name = "loginid", nullable = false)
    public Integer getId() {
        return id;
    }

    @Column(nullable = false, length = 100)
    public String getEmail() {
        return email;
    }
    
    @Column(name = "lastlogindate")
    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }
    
    @Column(nullable = false, length = 100)
    public String getLogin() {
        return login;
    }    

    @Transient
    public DateRange getValidDateRange() {
        return new DateRange(validFromDate, validToDate);
    }
    
    @Column(name = "validfromdate")
    public LocalDate getValidFromDate() {
        return validFromDate;
    }

    @Column(name = "validtodate")
    public LocalDate getValidToDate() {
        return validToDate;
    }
    
    @Transient
    public boolean isValid() {
        return isValid(LocalDate.now());
    }
    
    @Transient
    public boolean isValid(LocalDate date) {
        return getValidDateRange().contains(date);
    }

    public void setEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) throw new IllegalArgumentException("L'email <" + email + "> est invalide");
        this.email = email;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }

    public void setValidFromDate(LocalDate validFromDate) {
        this.validFromDate = Objects.requireNonNullElse(validFromDate, INIT_DATE);
        getValidDateRange();
    }

    public void setValidToDate(LocalDate validToDate) {
        this.validToDate = Objects.requireNonNullElse(validToDate, FINAL_DATE);
        getValidDateRange();
    }

}
