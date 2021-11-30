package web.dao;

import org.springframework.transaction.annotation.Transactional;
import web.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User getUserByName(String name);

    void addUser(User user);
    void updateUser(User user);
    void removeUserById(Long id);
    User getUser(Long id);

    //void addUserDB();
}
