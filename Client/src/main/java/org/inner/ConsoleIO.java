package org.inner;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleIO {
    private BufferedReader reader;

    public ConsoleIO() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String write() throws IOException {
        System.out.print(">>> ");
        return reader.readLine();
    }
}
