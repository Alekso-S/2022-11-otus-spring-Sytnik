package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Bottle;
import ru.otus.spring.domain.Plastic;

import java.util.ArrayList;
import java.util.List;

@Service
public class BottleService {

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
}
