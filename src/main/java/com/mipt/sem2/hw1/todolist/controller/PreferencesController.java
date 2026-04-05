package com.mipt.sem2.hw1.todolist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/preferences")
public class PreferencesController {

    private static final String VIEW_PREFERENCE_COOKIE_NAME = "viewPreference";
    private static final String DEFAULT_VIEW = "detailed";

    @GetMapping("/view")
    public ResponseEntity<String> getViewPreference(@CookieValue(name = VIEW_PREFERENCE_COOKIE_NAME, defaultValue = DEFAULT_VIEW) String viewPreference) {
        return ResponseEntity.ok(viewPreference);
    }

    @PostMapping("/view")
    public ResponseEntity<Void> setViewPreference(@RequestParam String mode, HttpServletResponse response) {
        if (!mode.equals("compact") && !mode.equals("detailed")) {
            return ResponseEntity.badRequest().build();
        }
        Cookie cookie = new Cookie(VIEW_PREFERENCE_COOKIE_NAME, mode);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
