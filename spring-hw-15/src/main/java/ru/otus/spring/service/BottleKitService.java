package ru.otus.spring.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BottleKitService {

    private final AtomicInteger bottleKitCounter = new AtomicInteger();

    public int incrementAndGet() {
        return bottleKitCounter.incrementAndGet();
    }

    public int get() {
        return bottleKitCounter.get();
    }
}
