package app.security;

import app.model.Person;
import app.repository.PersonRepository;
import app.security.jwt.JwtPersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtPersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public JwtPersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByEmail(email);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("Person with email: " + email + " not found");
        }
        return new JwtPersonDetails(person.get());
    }

}
