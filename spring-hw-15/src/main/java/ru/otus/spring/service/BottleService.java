package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Bottle;
import ru.otus.spring.domain.Plastic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class BottleService {

    private final QualityService qualityService;

    private final AtomicInteger bottleCounter = new AtomicInteger();

    public List<Bottle> produce(Plastic plastic) {
        List<Bottle> bottles = new ArrayList<>();
        while (true){
            Bottle bottle = new Bottle();
            plastic.setVolume(plastic.getVolume() - bottle.getWeight());
            if (plastic.getVolume() < 0) {
                break;
            }
            bottles.add(bottle);
        }
        return bottles;
    }

    public boolean check(Bottle bottle) {
        boolean res = qualityService.check(bottle);
        if (res) {
            bottleCounter.incrementAndGet();
        }
        return res;
    }

    public int getCnt() {
        return bottleCounter.get();
    }
}
