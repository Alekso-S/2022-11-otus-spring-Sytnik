package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Record;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Component
public class TestingDaoCsv implements TestingDao {

    public final String csvResourceName;

    public TestingDaoCsv(@Value("${csv-file-name}") String csvResourceName) {
        this.csvResourceName = csvResourceName;
    }

    @Override
    public List<Record> getAllRecords() {
        try (InputStream resourceInputStream = getClass().getResourceAsStream("/"+csvResourceName)) {
            HeaderColumnNameMappingStrategy<Record> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
            mappingStrategy.setType(Record.class);

            InputStreamReader inputStreamReader = new InputStreamReader(resourceInputStream);

            CsvToBean<Record> csvToBean = new CsvToBeanBuilder<Record>(inputStreamReader)
                    .withMappingStrategy(mappingStrategy)
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            Logger logger = Logger.getLogger("application");
            logger.severe("Error during loading the source file: " + e);

            return Collections.emptyList();
        }
    }
}

