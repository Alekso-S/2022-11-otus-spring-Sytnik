package ru.otus.spring.converter;

import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.List;

public class QuestionConverter {
    public static String toText(Question question) {
        StringBuilder result = new StringBuilder("- " + question.getText());

        List<Answer> answers = question.getAnswers();
        for (int j = 0; j < answers.size(); j++) {
            result.append("\n").append(j + 1).append(") ").append(answers.get(j).getText());
        }

        return result.toString();
    }
}
