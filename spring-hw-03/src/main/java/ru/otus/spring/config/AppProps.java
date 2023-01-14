package ru.otus.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Locale;
import java.util.Map;

@ConstructorBinding
@ConfigurationProperties(prefix = "application")
public class AppProps implements DaoCsvProps, ResultServiceProps, MessageSourceProps {

    private final Map<Locale, String> csvFileName;
    private final double passCoefficient;
    private final Locale locale;

    public AppProps(Map<Locale, String> csvFileName,
                    double passCoefficient,
                    Locale locale) {
        this.csvFileName = csvFileName;
        this.passCoefficient = passCoefficient;
        this.locale = locale;
    }

    @Override
    public String getCsvFileName() {
        return csvFileName.get(locale);
    }

    @Override
    public double getPassCoefficient() {
        return passCoefficient;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }
}
