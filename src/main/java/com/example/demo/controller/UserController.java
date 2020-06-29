package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public List<UserEntity> getUserInfo(HttpServletRequest request, HttpServletResponse response){
        return service.getFindAll();
    }

    @RequestMapping(value = "/user/{userName}",method = RequestMethod.GET)
    public UserEntity getUserInfoByUserName(HttpServletRequest request, HttpServletResponse response,
                                            @PathVariable(name = "userName") String userName){
        return service.getFindByUserName(userName);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Long save(HttpServletRequest request, HttpServletResponse response,
                     @RequestBody UserEntity user){
        return service.save(user);
    }

}
