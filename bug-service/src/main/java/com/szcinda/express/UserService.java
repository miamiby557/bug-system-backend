package com.szcinda.express;

import com.szcinda.express.dto.UserIdentity;
import com.szcinda.express.params.PageResult;
import com.szcinda.express.params.QueryUserParams;
import com.szcinda.express.persistence.User;

import java.util.List;

public interface UserService {
    PageResult<User> query(QueryUserParams params);

    UserIdentity getUserIdentity(String account, String password);

    void delete(String userId);

    String getToken(String userId, String password);

    User findUserById(String userId);

    List<User> findAll();

    void init();
}
