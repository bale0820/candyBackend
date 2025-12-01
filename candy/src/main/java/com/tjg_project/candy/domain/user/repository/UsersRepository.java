package com.tjg_project.candy.domain.user.repository;

import com.tjg_project.candy.domain.user.entity.Users;

public interface UsersRepository {
    boolean signup(Users users);
    boolean idcheck(String id);
    Users findById(String id);
    Users findByEmailOrPhone(String query); // 아이디 찾기용
    Users findByIdAndEmailOrPhone(String id, String query); // 비밀번호 찾기용
    Users findByName(String name);
}
