package ru.gb.config;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import org.springframework.context.annotation.*;
import ru.gb.GameLogic;

@Configuration
public class AppConfig {
    @Bean
    public HeadlessApplication getApplication(GameLogic gameLogic) {
        return new HeadlessApplication(gameLogic);
    }
}
