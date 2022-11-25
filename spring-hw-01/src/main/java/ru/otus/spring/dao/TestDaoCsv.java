package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import ru.otus.spring.domain.Record;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class TestDaoCsv implements TestDao {

    private final InputStream resourceInputStream;

    public TestDaoCsv(String csvResourceName) {
        resourceInputStream = getClass().getResourceAsStream(csvResourceName);
    }

    @Override
    public List<Record> getAllRecords() {
        try {
            HeaderColumnNameMappingStrategy<Record> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
            mappingStrategy.setType(Record.class);

            InputStreamReader inputStreamReader = new InputStreamReader(resourceInputStream);

            CsvToBean<Record> csvToBean = new CsvToBeanBuilder<Record>(inputStreamReader)
                    .withMappingStrategy(mappingStrategy)
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    private void destroy() throws IOException {
        resourceInputStream.close();
    }
}

