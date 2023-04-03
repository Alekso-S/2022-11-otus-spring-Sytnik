package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Cap;
import ru.otus.spring.domain.Plastic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class CapService {

    private final QualityService qualityService;

    private final AtomicInteger capCounter = new AtomicInteger();

    public List<Cap> produce(Plastic plastic) {
        List<Cap> caps = new ArrayList<>();
        while (true){
            Cap cap = new Cap();
            plastic.setVolume(plastic.getVolume() - cap.getWeight());
            if (plastic.getVolume() < 0) {
                break;
            }
            caps.add(cap);
        }
        return caps;
    }

    public boolean check(Cap cap) {
        boolean res = qualityService.check(cap);
        if (res) {
            capCounter.incrementAndGet();
        }
        return res;
    }

    public int getCnt() {
        return capCounter.get();
    }
}
