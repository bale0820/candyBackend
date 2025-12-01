package com.tjg_project.candy.domain.user.repository;

import com.tjg_project.candy.domain.user.entity.Users;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcTemplateUsersRepository implements UsersRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateUsersRepository (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean signup(Users users) {
        boolean result = false;
        String sql = "insert into users(birthday, email, gender, name, password, phone, provider, user_id, address, recommendation, zonecode, role) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'USER')";

        Object[] param = {
                users.getBirthday(), users.getEmail(), users.getGender(), users.getName(), users.getPassword(),
                users.getPhone(), users.getProvider(), users.getUserId(), users.getAddress(), users.getRecommendation(),
                users.getZonecode()
        };

        int rows = jdbcTemplate.update(sql, param);

        if(rows == 1) {
            result = true;
        }
        return result;
    }

    @Override
    public boolean idcheck(String id) {
        String sql = "select count(*) from users where user_id = ?";

        Long rows = jdbcTemplate.queryForObject(sql, long.class , id);

        return rows != null && rows != 0;
    }

    @Override
    public Users findById(String id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Users.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Users findByEmailOrPhone(String query) {
        String sql = "SELECT * FROM users WHERE email = ? OR phone = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Users.class), query, query);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Users findByIdAndEmailOrPhone(String id, String query) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND (email = ? OR phone = ?)";

        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Users.class), id, query, query);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Users findByName(String name) {
        String sql = "SELECT * FROM users WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Users.class), name);
        } catch (Exception e) {
            return null;
        }
    }
}
