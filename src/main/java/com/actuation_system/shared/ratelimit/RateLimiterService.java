package com.actuation_system.shared.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean tentarConsumir(String chave) {
        Bucket bucket = buckets.computeIfAbsent(chave, k -> criarNovoBucket());
        return bucket.tryConsume(1);
    }

    private Bucket criarNovoBucket() {
        Bandwidth limite = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limite).build();
    }
}
