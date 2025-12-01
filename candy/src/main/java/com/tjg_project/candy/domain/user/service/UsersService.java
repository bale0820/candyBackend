package com.tjg_project.candy.domain.user.service;

import com.tjg_project.candy.domain.user.entity.Users;

public interface UsersService {
    boolean signup(Users users);
    Users login(String id, String password);
    String findUserId(String query);
    String findPassword(String id, String query);
    boolean idcheck(Users users);
}
