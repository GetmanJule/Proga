package org;

import org.inner.ConsoleIO;

public class ClientMain {


    public static void main(String[] args) {
        Client client = new Client();
        client.connect(new ConsoleIO());
    }
}
