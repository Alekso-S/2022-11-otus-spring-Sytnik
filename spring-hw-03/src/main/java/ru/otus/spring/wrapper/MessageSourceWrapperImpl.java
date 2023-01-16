package ru.otus.spring.wrapper;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.spring.config.MessageSourceProps;

@Component
public class MessageSourceWrapperImpl implements MessageSourceWrapper {

    private final MessageSource messageSource;
    private final MessageSourceProps messageSourceProps;

    public MessageSourceWrapperImpl(MessageSource messageSource,
                                    MessageSourceProps messageSourceProps) {
        this.messageSource = messageSource;
        this.messageSourceProps = messageSourceProps;
    }

    @Override
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, messageSourceProps.getLocale());
    }

    @Override
    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, messageSourceProps.getLocale());
    }
}
