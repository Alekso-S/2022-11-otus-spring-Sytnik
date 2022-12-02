package ru.otus.spring.connection;

import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Component
public class ConnectorImpl implements Connector {

    private final Scanner scanner;
    private final PrintStream printStream;

    public ConnectorImpl(InputStream inputStream, PrintStream printStream) {
        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public void sendText(String s) {
        printStream.print(s);
    }

    @Override
    public void sendLine(String s) {
        printStream.println(s);
    }

    @Override
    public String getLine() {
        return scanner.nextLine();
    }
}
