package com.mipt.sem2.hw1.todolist.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PreferencesController.class)
class PreferencesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getViewPreference_withCookie_returnsValue() throws Exception {
    mockMvc.perform(get("/api/preferences/view")
            .cookie(new jakarta.servlet.http.Cookie("viewPreference", "compact")))
        .andExpect(status().isOk())
        .andExpect(content().string("compact"));
  }

  @Test
  void getViewPreference_noCookie_returnsDefault() throws Exception {
    mockMvc.perform(get("/api/preferences/view"))
        .andExpect(status().isOk())
        .andExpect(content().string("detailed"));
  }

  @Test
  void setViewPreference_validMode_returnsOkAndSetsCookie() throws Exception {
    mockMvc.perform(post("/api/preferences/view")
            .param("mode", "compact"))
        .andExpect(status().isOk())
        .andExpect(cookie().exists("viewPreference"))
        .andExpect(cookie().value("viewPreference", "compact"))
        .andExpect(cookie().path("viewPreference", "/"))
        .andExpect(cookie().maxAge("viewPreference", 60 * 60 * 24 * 365));
  }

  @Test
  void setViewPreference_invalidMode_returnsBadRequest() throws Exception {
    mockMvc.perform(post("/api/preferences/view")
            .param("mode", "invalid"))
        .andExpect(status().isBadRequest())
        .andExpect(cookie().doesNotExist("viewPreference"));
  }
}