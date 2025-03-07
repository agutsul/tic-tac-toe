package com.agutsul.tictactoe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.agutsul.tictactoe.service.PlayerService;

@Configuration
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

//    @Bean
//    CommandLineRunner initPlayers(PlayerService playerService) {
//        
//      return args -> {
//        log.info("Preloading " + playerService.create(PlayerService.BOT_NAME));
//      };
//    }
}