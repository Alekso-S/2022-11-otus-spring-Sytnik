package ru.otus.spring.io;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class IOServiceConsole implements IOService {

    private final Scanner scanner;
    private final PrintStream printStream;

    public IOServiceConsole(InputStream inputStream, PrintStream printStream) {
        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
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

    @Override
    public String readLineWithRequest(String s) {
        writeString(s);
        return readLine();
    }
}
