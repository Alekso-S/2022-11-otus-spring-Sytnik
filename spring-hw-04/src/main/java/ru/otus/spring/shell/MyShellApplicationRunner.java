package ru.otus.spring.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.shell.DefaultShellApplicationRunner;
import org.springframework.shell.ShellApplicationRunner;
import org.springframework.shell.ShellRunner;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.TestingService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class MyShellApplicationRunner implements ShellApplicationRunner {

    private final static Logger log = LoggerFactory.getLogger(DefaultShellApplicationRunner.class);
    private final List<ShellRunner> shellRunners;
    private final TestingService testingService;

    public MyShellApplicationRunner(List<ShellRunner> shellRunners,
                                    TestingService testingService) {
        this.testingService = testingService;
        this.shellRunners = shellRunners;
        Collections.sort(shellRunners, new AnnotationAwareOrderComparator());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        testingService.showInviteMessage();

        log.debug("Checking shell runners {}", shellRunners);
        Optional<ShellRunner> optional = shellRunners.stream()
                .filter(sh -> sh.canRun(args))
                .findFirst();
        ShellRunner shellRunner = optional.orElse(null);
        log.debug("Using shell runner {}", shellRunner);
        if (shellRunner != null) {
            shellRunner.run(args);
        }
    }
}
