package org.inner.commands;


import org.data.inner.Movie;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientCommandManager {
    public Map<String, Command> commands;

    {
        commands = new HashMap<>();
        commands.put("add", new AddElement());
        commands.put("update", new UpdateCommand());
    }

    public Movie execute(String stringCommand) {
        try {
            return commands.get(stringCommand).doo();
        } catch (Exception e) {
            return null;
        }
    }

}
