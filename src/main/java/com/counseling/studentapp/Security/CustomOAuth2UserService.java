package com.counseling.studentapp.Security;

import com.counseling.studentapp.model.User;
import com.counseling.studentapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // ✅ Extract user details from Google
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // ✅ Save or update user in DB
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword("GOOGLE_USER"); // dummy password
            user.setRole("ROLE_STUDENT");
        } else {
            // Update name if changed on Google account
            user.setName(name);
        }
        userRepository.save(user);

        // ✅ Return OAuth2User with ROLE_STUDENT
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT")),
                attributes,
                "email"
        );
    }
}
