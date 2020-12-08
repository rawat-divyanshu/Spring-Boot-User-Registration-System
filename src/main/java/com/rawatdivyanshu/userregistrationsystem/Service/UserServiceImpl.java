package com.rawatdivyanshu.userregistrationsystem.Service;

import com.rawatdivyanshu.userregistrationsystem.Dao.UserDao;
import com.rawatdivyanshu.userregistrationsystem.Exceptions.UserAuthException;
import com.rawatdivyanshu.userregistrationsystem.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User validateUser(String email, String password) throws UserAuthException {
        if(email != null) email = email.toLowerCase();
        return userDao.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(User user) throws UserAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");

        if(user.getEmail() != null) user.setEmail(user.getEmail().toLowerCase());

        if(!pattern.matcher(user.getEmail()).matches())
            throw new UserAuthException("Invalid Email Format");

        if(userDao.getCountByEmail(user.getEmail()) > 0)
            throw new UserAuthException(("Email Already in Use"));

        userDao.create(user);

        return userDao.findByEmail(user.getEmail());
    }

    @Override
    public User getUserById(Long id) throws UserAuthException {
        return userDao.findById(id);
    }
}
