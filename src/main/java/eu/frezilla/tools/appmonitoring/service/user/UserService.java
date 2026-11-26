package eu.frezilla.tools.appmonitoring.service.user;

import eu.frezilla.tools.appmonitoring.domain.User;
import eu.frezilla.tools.appmonitoring.repository.UserRepository;
import eu.frezilla.tools.appmonitoring.service.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class UserService extends Service {
    
    private final UserRepository userRepository = new UserRepository();

    public AccountStatusReport generateAccountStatusReport() {
        LocalDate inactiveDate = LocalDate.now().minus(2, ChronoUnit.MONTHS);
        
        List<User> users = userRepository.findAll();
        
        long totalUsers = users.size();
        long nbDisabled = users.stream().filter(u -> !u.isValid()).count();
        long nbActive = users.stream().filter(u -> u.isValid() && u.getLastLoginDate().isAfter(inactiveDate)).count();
        long nbInactive = totalUsers - nbDisabled - nbActive;
        
        return new AccountStatusReport(nbActive, nbInactive, nbDisabled);
    }
    
    
}
