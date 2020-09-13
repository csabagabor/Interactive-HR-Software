package com.gabor.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationApplication {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Waiting--notifications");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        //Thread.sleep(10000);
        System.out.println("Starting--notifications");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        SpringApplication.run(NotificationApplication.class, args);
    }

}
