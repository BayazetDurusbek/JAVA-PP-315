package ru.restTemplate.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        Communication communication = context.getBean("communication", Communication.class);

        communication.getAllUsers();
        communication.saveUser();
        communication.updateUser();
        System.out.println(communication.deleteUser(3L));
        System.out.println(communication.finalApp());

    }


}
