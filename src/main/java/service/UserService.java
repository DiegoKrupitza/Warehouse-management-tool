package service;

import domain.LogInStatus;
import domain.User;
import persistence.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService extends BaseService {


    public boolean loginUser(String username) throws Exception {

        Optional<User> optionalUser = userRepository.findByUsername(getConnection(), username);

        User u = null;
        if (optionalUser.isPresent()) u = userRepository.findByUsername(getConnection(), username).get();

        if (u == null) return false;

        u.setLogInStatus(LogInStatus.LOGGED_IN);
        u = userRepository.updateUser(getConnection(), u);

        return true;
    }

    public List<User> getAllUser() throws Exception {
        return userRepository.findAll(getConnection());
    }

    public boolean deleteUser(long id) throws Exception {
        return (userRepository.delete(getConnection(), id) == 1) ? true : false;
    }

    public User getUser(long id) throws Exception {
        Optional<User> user = userRepository.findById(getConnection(), id);
        if (user.isPresent()) return user.get();

        return null;
    }

    public User updateUser(User user) throws Exception {
        return userRepository.updateUser(getConnection(), user);
    }

    public User insertNewUser(User u) throws Exception {
        Optional<User> user = userRepository.registerUser(getConnection(), u);
        if (user.isPresent()) return user.get();
        return null;
    }
}