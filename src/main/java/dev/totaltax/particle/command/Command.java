package dev.totaltax.particle.command;

import java.util.ArrayList;

public class Command {
    private String name, description, syntax;
    private ArrayList<String> aliases;

    public Command() {}

    public Command(String name, String description, String syntax) {
        this.name = name;
        this.description = description;
        this.syntax = syntax;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSyntax() {
        return syntax;
    }

    public ArrayList<String> getAliases() {
        return aliases;
    }

    public void setAliases(ArrayList<String> aliases) {
        this.aliases = aliases;
    }

    public void appendAliases(String s) {
        this.aliases.add(s);
    }
}
