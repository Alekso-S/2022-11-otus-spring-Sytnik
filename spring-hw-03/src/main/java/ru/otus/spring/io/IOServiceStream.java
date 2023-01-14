package ru.otus.spring.io;

import ru.otus.spring.annotation.QuestionBenchmark;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class IOServiceStream implements IOService {

    private final Scanner scanner;
    private final PrintStream printStream;

    public IOServiceStream(InputStream inputStream, PrintStream printStream) {
        this.scanner = new Scanner(inputStream);
        this.printStream = new PrintStream(printStream);
    }

    @Override
    public void writeString(String s) {
        printStream.print(s);
    }

    @Override
    public void writeLine(String s) {
        printStream.println(s);
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @QuestionBenchmark
    @Override
    public String readLineWithRequest(String s) {
        writeString(s);
        return readLine();
    }
}
