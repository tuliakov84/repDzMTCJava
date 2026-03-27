package com.mipt.sem2.hw1.todolist.aspect;


import com.mipt.sem2.hw1.todolist.repository.TaskRepository;
import com.mipt.sem2.hw1.todolist.service.TaskService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TaskLifecycleProcessor implements BeanPostProcessor {

  private static final Logger log = LoggerFactory.getLogger(TaskLifecycleProcessor.class);

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof TaskService || bean instanceof TaskRepository) {
      log.info("До инициализации бина: {} (класс: {})",
          beanName, bean.getClass().getSimpleName());
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof TaskService || bean instanceof TaskRepository) {
      log.info("После инициализации бина: {} (класс: {})",
          beanName, bean.getClass().getSimpleName());
    }
    return bean;
  }
}
