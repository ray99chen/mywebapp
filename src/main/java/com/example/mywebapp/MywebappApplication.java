package com.example.mywebapp;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisStringReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableSwagger2WebFlux
public class MywebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(MywebappApplication.class, args);
    }

    @Bean
    public Docket petApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }

    @Bean(name = "master")
    public RedisStringReactiveCommands<String, String> writeCommands() {
        System.out.println("redis master: "+System.getenv("redis-master"));
        //RedisClient redisClient = RedisClient.create("redis://192.168.99.100:32423/0");
        RedisClient redisClient = RedisClient.create("redis://"+System.getenv("redis-master")+":6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisStringReactiveCommands<String, String> reactiveCommands = connection.reactive();
        return reactiveCommands;
    }

    @Bean(name = "slave")
    public RedisStringReactiveCommands<String, String> readCommands() {
        System.out.println("redis slave: "+System.getenv("redis-slave"));
        RedisClient redisClient = RedisClient.create("redis://"+System.getenv("redis-slave")+":6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisStringReactiveCommands<String, String> reactiveCommands = connection.reactive();
        return reactiveCommands;
    }
}

