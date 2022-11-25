package ru.otus.spring.communication;

public class ProducerImpl implements Producer {

    @Override
    public void printLine(String s) {
        System.out.println(s);
    }
}
