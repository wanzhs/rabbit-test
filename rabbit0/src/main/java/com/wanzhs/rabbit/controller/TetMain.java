package com.wanzhs.rabbit.controller;

import com.wanzhs.rabbit.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TetMain {
    public static void main(String[] args) {
        User user = new User();
        user.setUsername("wanzhs").setPassword("123456").setPhone("13596543685");
        log.info("name:{},password:{},phone:{}", user.getUsername(), user.getPassword(), user.getPhone());
    }
}
