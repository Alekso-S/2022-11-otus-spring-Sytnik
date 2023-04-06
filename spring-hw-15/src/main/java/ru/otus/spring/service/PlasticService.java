package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Bottle;
import ru.otus.spring.domain.Cap;
import ru.otus.spring.domain.Plastic;
import ru.otus.spring.enums.Assignment;

@Service
public class PlasticService {

    private final static float BOTTLE_CAP_PROPORTION = Bottle.STANDARD_WEIGHT / Cap.STANDARD_WEIGHT;

    public Plastic getBottleVolume(Plastic plastic) {
        return new Plastic(Assignment.BOTTLE, plastic.getVolume() / (BOTTLE_CAP_PROPORTION + 1)
                * BOTTLE_CAP_PROPORTION);
    }

    public Plastic getCapVolume(Plastic plastic) {
        return new Plastic(Assignment.CAP, plastic.getVolume() / (BOTTLE_CAP_PROPORTION + 1));
    }
}
