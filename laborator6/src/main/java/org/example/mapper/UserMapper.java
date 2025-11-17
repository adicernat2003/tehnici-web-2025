package org.example.mapper;

import org.example.model.User;
import org.example.dto.UserPayload;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserPayload p) {
        User u = new User();
        u.setFirstName(p.getFirstName());
        u.setLastName(p.getLastName());
        u.setEmail(p.getEmail());
        u.setBirthDate(p.getBirthDate());
        if (p.getId() != null) {
            u.setId(p.getId());
        }
        return u;
    }

    public User toEntity(User u, UserPayload p) {
        u.setFirstName(p.getFirstName());
        u.setLastName(p.getLastName());
        u.setEmail(p.getEmail());
        u.setBirthDate(p.getBirthDate());
        return u;
    }
}
