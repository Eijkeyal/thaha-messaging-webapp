package com.chat.app.config;
import io.github.bucket4j.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket createBucket(){

        Bandwidth limit =
                Bandwidth.builder()

                        // allow 100 requests
                        .capacity(100)

                        // refill 100 requests every minute
                        .refillGreedy(
                                100,
                                Duration.ofMinutes(1)
                        )

                        .build();
        return Bucket.builder()

                .addLimit(limit)

                .build();
    }


}