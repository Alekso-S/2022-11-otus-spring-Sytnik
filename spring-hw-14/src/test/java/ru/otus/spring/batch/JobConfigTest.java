package ru.otus.spring.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.repository.jpa.JpaBookRepository;
import ru.otus.spring.repository.jpa.JpaCommentRepository;
import ru.otus.spring.util.DataProducer;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Менеджер джобов должен ")
class JobConfigTest {

    @Autowired
    private JobOperator jobOperator;
    @Autowired
    private JpaBookRepository bookRepository;
    @Autowired
    private JpaCommentRepository commentRepository;

    @Test
    @Transactional
    @DisplayName("выполнять первую версию джоба")
    void shouldExecuteJob1() throws Exception {
        TestTransaction.end();

        jobOperator.start(JobConfig.JOB_1_NAME, "time=" + LocalDateTime.now());

        assertData();
    }

    @Test
    @Transactional
    @DisplayName("выполнять вторую версию джоба")
    void shouldExecuteJob2() throws Exception {
        TestTransaction.end();

        jobOperator.start(JobConfig.JOB_2_NAME, "time=" + LocalDateTime.now());

        assertData();
    }

    private void assertData() {
        TestTransaction.start();

        assertThat(bookRepository.findAll().stream()
                .map(BookDto::new)
        ).hasSameElementsAs(DataProducer.getAllBooks().stream()
                .map(BookDto::new)
                .collect(Collectors.toList())
        );

        assertThat(commentRepository.findAll().stream()
                .map(CommentDto::new)
        ).hasSameElementsAs(DataProducer.getAllComments().stream()
                .map(CommentDto::new)
                .collect(Collectors.toList())
        );

        TestTransaction.end();
    }
}