package com.example.mywebapp;

import io.lettuce.core.api.reactive.RedisStringReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "Echo", description = "An echo service", tags = ("Echo"))
public class Controller {
    Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    @Qualifier("master")
    private RedisStringReactiveCommands<String, String> writeCommands;

    @Autowired
    @Qualifier("slave")
    private RedisStringReactiveCommands<String, String> readCommands;


    @GetMapping("/visitor")
    @ApiOperation(value = "Get visitor", notes = "Reads the current value of the visitor counter")
    public Mono<String> getVisitor() {
        return readCommands.get("visitor").map(v -> {
            logger.info("visitor count: " + v);
            return v;
        });
    }

    @PostMapping("/visitor")
    @ApiOperation(value = "Post visitor", notes = "Increase the visitor counter", nickname = "Add visitor")
    public void addVisitor() {
        logger.info("Visitor number: " + writeCommands.incr("visitor"));
    }

    @GetMapping("/fact/{key}")
    @ApiOperation(value = "Facts", notes = "Return some facts", nickname = "getFactsByKey")
    public Mono<Map<String, String>> fact(@PathVariable String key) {
        return writeCommands.incr("visitor").flatMap(v -> {
            logger.info("Visitor number: "+v);

            return readCommands.get(key).map(r -> {
                Map<String, String> map = new HashMap<>();
                map.put(key, r);
                return map;
            });
        });

    }

    @PostMapping("/fact/{key}/{value}")
    @ApiOperation(value = "Facts", notes = "Return some facts", nickname = "setFacts")
    public Mono<String> fact(@PathVariable String key, @PathVariable String value) {
        logger.info("Visitor number: " + writeCommands.incr("visitor"));
        writeCommands.incr("visitor").map(v -> {
            logger.info("Visitor number: "+v);
            return v;
        });

        return writeCommands.set(key, value);
    }
}
