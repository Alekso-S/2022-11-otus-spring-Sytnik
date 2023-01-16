package ru.otus.spring.validator;

import ru.otus.spring.dto.TaskRecord;
import ru.otus.spring.exception.DataValidityException;

import java.util.ArrayList;
import java.util.List;

public class TaskRecordValidator {
    public static void checkValidity(TaskRecord taskRecord) throws DataValidityException {
        List<String> nullFields = new ArrayList<>();

        if (taskRecord.getId() == null) {
            nullFields.add("id");
        }
        if (taskRecord.getQuestion() == null) {
            nullFields.add("question");
        }
        if (taskRecord.getAnswer() == null) {
            nullFields.add("answer");
        }
        if (taskRecord.isValid() == null) {
            nullFields.add("valid");
        }

        if (nullFields.size() > 0) {
            throw new DataValidityException("Empty field(s): " + nullFields);
        }
    }
}
