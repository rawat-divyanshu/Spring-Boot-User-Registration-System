package com.rawatdivyanshu.userregistrationsystem.Service;

import com.rawatdivyanshu.userregistrationsystem.Exceptions.UserAuthException;
import com.rawatdivyanshu.userregistrationsystem.Model.User;

public interface UserService {

    User validateUser(String email, String password) throws UserAuthException;

    User registerUser(User user) throws UserAuthException;

    User getUserById(Long id) throws UserAuthException;
}
