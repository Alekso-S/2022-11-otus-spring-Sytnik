package ru.otus.spring.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.model.jpa.JpaBook;
import ru.otus.spring.model.jpa.JpaComment;
import ru.otus.spring.model.mongo.MongoBook;
import ru.otus.spring.model.mongo.MongoComment;
import ru.otus.spring.repository.jpa.JpaAuthorRepository;
import ru.otus.spring.repository.jpa.JpaBookRepository;
import ru.otus.spring.repository.jpa.JpaCommentRepository;
import ru.otus.spring.repository.jpa.JpaGenreRepository;
import ru.otus.spring.service.JpaService;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final MongoTemplate mongoTemplate;
    private final EntityManagerFactory entityManagerFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JpaService jpaService;
    private final JpaAuthorRepository jpaAuthorRepository;
    private final JpaGenreRepository jpaGenreRepository;
    private final JpaBookRepository jpaBookRepository;
    private final JpaCommentRepository jpaCommentRepository;

    public static final String JOB_1_NAME = "migration-job-1";
    public static final String JOB_2_NAME = "migration-job-2";
    public static final String ERASE_STEP_NAME = "erase-step";
    public static final String JOB_1_BOOK_STEP_NAME = "book-step-1";
    public static final String JOB_2_BOOK_STEP_NAME = "book-step-2";
    private static final String JOB_1_COMMENT_STEP_NAME = "comment-step-1";
    private static final String JOB_2_COMMENT_STEP_NAME = "comment-step-2";
    private static final int ALL_RECORDS_CNT = 100;

    @Bean
    public Job job1(JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get(JOB_1_NAME)
                .start(eraseStep())
                .next(bookStep1())
                .next(commentStep1())
                .build();
    }

    @Bean
    public Job job2(JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get(JOB_2_NAME)
                .start(eraseStep())
                .next(bookStep2())
                .next(commentStep2())
                .build();
    }

    @Bean
    public Step eraseStep() {
        return stepBuilderFactory.get(ERASE_STEP_NAME)
                .tasklet(eraseTasklet())
                .build();
    }

    @Bean
    public Step bookStep1() {
        return stepBuilderFactory.get(JOB_1_BOOK_STEP_NAME)
                .<MongoBook, JpaBook>chunk(1)
                .reader(reader(MongoBook.class))
                .processor((ItemProcessor<MongoBook, JpaBook>) jpaService::prepareJpaBook)
                .writer(writer())
                .build();
    }

    @Bean
    public Step commentStep1() {
        return stepBuilderFactory.get(JOB_1_COMMENT_STEP_NAME)
                .<MongoComment, JpaComment>chunk(ALL_RECORDS_CNT)
                .reader(reader(MongoComment.class))
                .processor((ItemProcessor<MongoComment, JpaComment>) jpaService::prepareJpaComment)
                .writer(writer())
                .build();
    }

    @Bean
    public Step bookStep2() {
        return stepBuilderFactory.get(JOB_2_BOOK_STEP_NAME)
                .<MongoBook, BookDto>chunk(ALL_RECORDS_CNT)
                .reader(reader(MongoBook.class))
                .processor((ItemProcessor<MongoBook, BookDto>) BookDto::new)
                .writer(bookWriter())
                .build();
    }

    @Bean
    public Step commentStep2() {
        return stepBuilderFactory.get(JOB_2_COMMENT_STEP_NAME)
                .<MongoComment, CommentDto>chunk(ALL_RECORDS_CNT)
                .reader(reader(MongoComment.class))
                .processor((ItemProcessor<MongoComment, CommentDto>) CommentDto::new)
                .writer(commentWriter())
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter eraseTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(jpaService);
        adapter.setTargetMethod("erase");
        return adapter;
    }

    @Bean
    @Scope("prototype")
    public <T> MongoItemReader<T> reader(Class<T> tClass) {
        MongoItemReader<T> mongoItemReader = new RepeatableMongoItemReader<>();
        mongoItemReader.setTemplate(mongoTemplate);
        mongoItemReader.setTargetType(tClass);
        mongoItemReader.setQuery("{}");
        mongoItemReader.setSort(new HashMap<>());
        return mongoItemReader;
    }

    @Bean
    public JpaItemWriter<Object> writer() {
        return new JpaItemWriterBuilder<>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public ItemWriter<BookDto> bookWriter() {
        return new BookWriter();
    }

    @Bean
    public ItemWriter<CommentDto> commentWriter() {
        return new CommentWriter(jpaAuthorRepository, jpaGenreRepository, jpaBookRepository, jpaCommentRepository);
    }
}
