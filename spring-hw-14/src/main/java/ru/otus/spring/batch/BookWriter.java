package ru.otus.spring.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import ru.otus.spring.dto.BookDto;

import java.util.List;

@RequiredArgsConstructor
public class BookWriter implements ItemWriter<BookDto> {

    private ExecutionContext executionContext;

    @Override
    public void write(List<? extends BookDto> booksDtos) throws JsonProcessingException {
        executionContext.put("bookDtos", new ObjectMapper().writeValueAsString(booksDtos));
    }

    @BeforeStep
    public void getJobExecutionContext(StepExecution stepExecution) {
        this.executionContext = stepExecution.getJobExecution().getExecutionContext();
    }
}
