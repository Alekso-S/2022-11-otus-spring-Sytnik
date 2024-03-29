package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.BaseProduct;

@Service
public class QualityService {

    private final static float ACCEPTANCE_COEF = .04f;

    public boolean check(BaseProduct baseProduct) {
        return Math.abs(baseProduct.getWeight() - baseProduct.getStandardWeight()) / baseProduct.getStandardWeight()
                <= ACCEPTANCE_COEF;
    }
}
