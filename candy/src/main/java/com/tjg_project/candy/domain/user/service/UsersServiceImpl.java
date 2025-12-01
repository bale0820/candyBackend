package com.tjg_project.candy.domain.user.service;

import com.tjg_project.candy.domain.user.entity.Users;
import com.tjg_project.candy.domain.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl (UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean signup (Users users) {
        String encodePwd = passwordEncoder.encode(users.getPassword());   //UUID 타입으로 생성됨
        users.setPassword(encodePwd);
        return usersRepository.signup(users);
    }

    @Override
    public boolean idcheck (Users users) {
        String param = users.getUserId();

        if(users.getRecommendation() != null) {
            param = users.getRecommendation();
        }

        return usersRepository.idcheck(param);
    }

    @Override
    public Users login(String id, String password) {
        Users users = usersRepository.findById(id);
        if (users != null && passwordEncoder.matches(password, users.getPassword())) {
            return users;
        }
        return null;
    }

    @Override
    public String findUserId(String query) {
        Users member = usersRepository.findByEmailOrPhone(query);
        if (member != null) {
            return member.getUserId();
        }
        return null;
    }

    @Override
    public String findPassword(String id, String query) {
        Users member = usersRepository.findByIdAndEmailOrPhone(id, query);
        if (member != null) {
            return member.getPassword();
        }
        return null;
    }

}
