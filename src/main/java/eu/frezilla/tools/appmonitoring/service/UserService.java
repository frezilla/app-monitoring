package eu.frezilla.tools.appmonitoring.service;

import eu.frezilla.tools.appmonitoring.repository.UserRepository;

public class UserService extends Service {
    
    private final UserRepository userRepository = new UserRepository();

    public void run() {
        System.out.println(userRepository.count());
    }
}
