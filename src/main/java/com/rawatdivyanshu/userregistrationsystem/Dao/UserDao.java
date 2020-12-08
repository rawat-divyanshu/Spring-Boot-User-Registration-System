package com.rawatdivyanshu.userregistrationsystem.Dao;

import com.rawatdivyanshu.userregistrationsystem.Exceptions.UserAuthException;
import com.rawatdivyanshu.userregistrationsystem.Model.User;

public interface UserDao {

    void create(User user) throws UserAuthException;

    User findByEmailAndPassword(String email, String password) throws UserAuthException;

    Integer getCountByEmail(String email) throws UserAuthException;

    User findById(Long id) throws UserAuthException;

    User findByEmail(String email) throws UserAuthException;

}
