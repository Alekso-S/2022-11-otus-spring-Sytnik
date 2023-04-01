package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.spring.enums.Assignment;

@Data
@AllArgsConstructor
public class Plastic {
    Assignment assignment;
    float volume;

    public Plastic(float volume) {
        this.volume = volume;
    }
}
