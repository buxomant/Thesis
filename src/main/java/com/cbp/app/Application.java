package com.cbp.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
