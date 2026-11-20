package eu.fezilla.tools.appmonitoring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    
    @Id
    @Column(name = "loginid")
    private Integer id;
    
    private String login;
    
    private String email;
    
}
