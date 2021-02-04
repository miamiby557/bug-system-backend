package com.szcinda.express.controller;

import com.szcinda.express.UserService;
import com.szcinda.express.configuration.UserLoginToken;
import com.szcinda.express.controller.dto.Result;
import com.szcinda.express.params.PageResult;
import com.szcinda.express.params.QueryUserParams;
import com.szcinda.express.persistence.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @UserLoginToken
    @PostMapping("/query")
    public PageResult<User> query(@RequestBody QueryUserParams params) {
        return userService.query(params);
    }

    @UserLoginToken
    @GetMapping("/{userId}")
    public Result<User> getUser(@PathVariable String userId) {
        User user = userService.findUserById(userId);
        return Result.success(user);
    }

    @GetMapping("all")
    public List<User> all() {
        return userService.findAll();
    }


    @UserLoginToken
    @GetMapping("/delete/{userId}")
    public Result<String> delete(@PathVariable String userId) {
        userService.delete(userId);
        return Result.success();
    }
}
