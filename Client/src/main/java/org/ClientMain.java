package org;

import org.inner.ConsoleIO;
import org.inner.commands.ClientCommandManager;

public class  ClientMain {


    public static void main(String[] args) {
        Client client = new Client(new ClientCommandManager());

        client.connect(new ConsoleIO());


    }
}
