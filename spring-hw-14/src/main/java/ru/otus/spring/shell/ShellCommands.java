package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.batch.JobConfig;

import java.time.LocalDateTime;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {

    private final JobOperator jobOperator;

    @ShellMethod(value = "startJob1", key = "sj1")
    public void startJob1() throws Exception {
        Long jobExecutionId = jobOperator.start(JobConfig.JOB_1_NAME, "time=" + LocalDateTime.now());
        System.out.println(jobOperator.getSummary(jobExecutionId));
    }

    @ShellMethod(value = "startJob2", key = "sj2")
    public void startJob2() throws Exception {
        Long jobExecutionId = jobOperator.start(JobConfig.JOB_2_NAME, "time=" + LocalDateTime.now());
        System.out.println(jobOperator.getSummary(jobExecutionId));
    }
}
