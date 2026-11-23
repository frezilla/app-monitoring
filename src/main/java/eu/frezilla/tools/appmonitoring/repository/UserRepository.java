package eu.frezilla.tools.appmonitoring.repository;

import eu.frezilla.tools.appmonitoring.domain.User;

public class UserRepository extends CRUDRepository<User, Integer> {
    
    public UserRepository() {
        super(User.class);
    }
    
}
