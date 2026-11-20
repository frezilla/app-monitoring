package eu.fezilla.tools.appmonitoring.repository;

import eu.fezilla.tools.appmonitoring.domain.User;

public class UserRepository extends CRUDRepository<User, Integer> {
    
    public UserRepository() {
        super(User.class);
    }
    
}
