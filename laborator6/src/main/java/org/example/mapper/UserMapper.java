package org.example.mapper;

import org.example.dto.UserPayload;
import org.example.dto.UserResponse;
import org.example.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserPayload p) {
        User u = new User();
        u.setFirstName(p.getFirstName());
        u.setLastName(p.getLastName());
        u.setEmail(p.getEmail());
        u.setBirthDate(p.getBirthDate());
        return u;
    }

    public User toEntity(User u, UserPayload p) {
        u.setFirstName(p.getFirstName());
        u.setLastName(p.getLastName());
        u.setEmail(p.getEmail());
        u.setBirthDate(p.getBirthDate());
        return u;
    }

    public UserResponse toResponse(User u) {
        UserResponse r = new UserResponse();
        r.setFirstName(u.getFirstName());
        r.setLastName(u.getLastName());
        r.setEmail(u.getEmail());
        r.setBirthDate(u.getBirthDate());
        return r;
    }
}
