package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.connection.Connector;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Task;
import ru.otus.spring.domain.Testing;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {

    private final Testing testing;
    private final Connector connector;
    private final double passCoefficient;

    public TestingServiceImpl(Testing testing,
                              Connector connector,
                              @Value("${pass-coefficient}") double passCoefficient) {
        this.testing = testing;
        this.connector = connector;
        this.passCoefficient = passCoefficient;
    }

    @Override
    public void printQuestions() {
        for (Question question : testing.getAllQuestions()) {
            connector.sendLine("- " + question.getText());
        }
    }

    @Override
    public void printQuestionsWithAnswers() {
        for (Task task : testing.getAllTasks()) {
            connector.sendLine("- " + task.getQuestion().getText());
            for (Answer answer : task.getAnswers()) {
                connector.sendLine("|- (" + (answer.isValidity() ? "+" : "-") + ") " + answer.getText());
            }
        }
    }

    @Override
    public void beginTesting() {

        List<Task> tasks = testing.getAllTasks();
        if (tasks.size() == 0) {
            connector.sendLine("Test can not be loaded");
            connector.sendLine("Exiting...");
            return;
        }

        connector.sendText("Input your name: ");
        String name = connector.getLine();

        connector.sendLine("\nTesting is starting...");

        List<Integer> answerNumbers = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            connector.sendLine("\nQuestion " + (i + 1) + " of " + tasks.size() + ":");
            connector.sendLine("- " + tasks.get(i).getQuestion().getText());

            List<Answer> answers = tasks.get(i).getAnswers();
            for (int j = 0; j < answers.size(); j++) {
                connector.sendLine((j + 1) + ") " + answers.get(j).getText());
            }

            while (true) {
                connector.sendText("\nType your answer [1-" + answers.size() + "]: ");
                String typedString = connector.getLine();

                if (typedString.matches("[1-" + answers.size() + "]")) {
                    answerNumbers.add(Integer.parseInt(typedString));
                    break;
                } else {
                    connector.sendLine("Answer \"" + typedString + "\" is not valid");
                }
            }
        }

        int result = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getAnswers().get(answerNumbers.get(i) - 1).isValidity()) {
                result++;
            }
        }

        connector.sendLine("\n" + name + ", your have " + result + " points of " + tasks.size());
        connector.sendLine("Testing is " + (result >= tasks.size() * passCoefficient ? "passed" : "failed"));

        connector.sendLine("\nType \"show\" if your want to see valid answers");
        if (connector.getLine().equals("show")) {
            printQuestionsWithAnswers();
        }
    }
}
