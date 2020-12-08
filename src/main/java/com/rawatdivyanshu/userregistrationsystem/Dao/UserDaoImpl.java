package com.rawatdivyanshu.userregistrationsystem.Dao;

import com.rawatdivyanshu.userregistrationsystem.Exceptions.UserAuthException;
import com.rawatdivyanshu.userregistrationsystem.Model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_CREATE = "INSERT INTO user_registration_system.users(first_name, last_name, email, password) " +
                                                "VALUES(?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM user_registration_system.users AS u WHERE u.email = ?";
    private static final String SQL_FIND_BY_ID = "SELECT u.user_id, u.first_name, u.last_name, u.email, u.password " +
                                                    "from user_registration_system.users AS u WHERE u.user_id = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT u.user_id, u.first_name, u.last_name, u.email, u.password " +
                                                        "from user_registration_system.users AS u WHERE u.email = ?";

    @Override
    public void create(User user) throws UserAuthException {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        try{
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE);
                ps.setString(1, user.getFirst_name());
                ps.setString(2, user.getLast_name());
                ps.setString(3, user.getEmail());
                ps.setString(4, hashedPassword);
                return ps;
            });
        } catch (Exception e) {
            throw new UserAuthException("Invalid Details. Failed to register the user.");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws UserAuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, new Object[]{email});
            if(!BCrypt.checkpw(password, user.getPassword()))
                throw new UserAuthException("Invalid Email/Password");
            return user;
        } catch(Exception e) {
            throw new UserAuthException("Invalid Email/Password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) throws UserAuthException {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, new Object[]{email});
    }

    @Override
    public User findById(Long id) throws UserAuthException {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, new Object[]{id});
    }

    @Override
    public User findByEmail(String email) throws UserAuthException {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, new Object[]{email});
    }

    private final RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(rs.getLong("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password"));
    });
}
