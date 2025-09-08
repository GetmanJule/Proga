package org;

import org.inner.ConsoleIO;

public class Main {


    public static void main(String[] args) {
        Client client = new Client();
        client.connect(new ConsoleIO());
    }
}
