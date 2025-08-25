package com.counseling.studentapp.Security;
import java.util.Collections;
import com.counseling.studentapp.model.User;
import com.counseling.studentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

   @Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepo.findByEmail(email);
    if (user == null) {
        throw new UsernameNotFoundException("User not found");
    }

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        Collections.singleton(new SimpleGrantedAuthority(user.getRole()))  // "ROLE_ADMIN" or "ROLE_STUDENT"
    );
}
}
