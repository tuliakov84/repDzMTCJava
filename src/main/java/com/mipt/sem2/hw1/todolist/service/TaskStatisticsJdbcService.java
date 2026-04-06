package com.mipt.sem2.hw1.todolist.service;

import com.mipt.sem2.hw1.todolist.enums.Priority;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskStatisticsJdbcService {

  private final JdbcTemplate jdbcTemplate;

  public TaskStatisticsJdbcService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Map<Priority, Long> getTasksCountByPriority() {
    String sql = "SELECT priority, COUNT(*) FROM tasks GROUP BY priority";
    RowMapper<Map.Entry<Priority, Long>> mapper = (rs, rowNum) ->
        Map.entry(Priority.valueOf(rs.getString("priority")), rs.getLong("count"));
    return jdbcTemplate.query(sql, mapper)
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
