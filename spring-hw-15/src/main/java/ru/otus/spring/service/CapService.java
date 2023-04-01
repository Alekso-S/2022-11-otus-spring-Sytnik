package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Cap;
import ru.otus.spring.domain.Plastic;

import java.util.ArrayList;
import java.util.List;

@Service
public class CapService {

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
}
