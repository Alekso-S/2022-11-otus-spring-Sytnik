package ru.otus.spring.batch;

import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.lang.Nullable;

public class RepeatableMongoItemReader<T> extends MongoItemReader<T> {

    @Nullable
    @Override
    protected T doRead() throws Exception {
        T res = super.doRead();
        if (res == null) {
            page = 0;
        }
        return res;
    }
}
