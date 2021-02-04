package com.szcinda.express.controller;

import com.szcinda.express.HomeService;
import com.szcinda.express.UserService;
import com.szcinda.express.controller.dto.Result;
import com.szcinda.express.dto.MainDto;
import com.szcinda.express.dto.UserIdentity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final HomeService homeService;

    public HomeController(UserService userService, HomeService homeService) {
        this.userService = userService;
        this.homeService = homeService;
    }

    @GetMapping("/")
    public String hello() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @PostMapping("/authenticate")
    public Result<UserIdentity> login(@RequestParam("account") String account, @RequestParam("password") String password) {
        UserIdentity identity = userService.getUserIdentity(account, password);
        String token = userService.getToken(identity.getId(), identity.getPassword());
        identity.setToken(token);
        return Result.success(identity);
    }

    @GetMapping("init")
    public Result<String> init() {
        userService.init();
        return Result.success();
    }

    @PostMapping("postData")
    public Result<String> postData(HttpServletRequest request) {
        StringBuilder data = new StringBuilder();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
            System.out.println("接收到数据：" + data.toString());
        } catch (IOException e) {
            return Result.fail(e.getMessage());
        }
        return Result.success();
    }


    @GetMapping("/query/{userId}")
    public Result<MainDto> hello(@PathVariable String userId) {
        return Result.success(homeService.query(userId));
    }
}
