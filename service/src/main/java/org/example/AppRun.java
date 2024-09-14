package org.example;

import org.example.service.UserService;

public class AppRun {
    public static void main(String[] args) {
        var userService = new UserService();
        System.out.println(userService.getUser(1L));
    }
}
