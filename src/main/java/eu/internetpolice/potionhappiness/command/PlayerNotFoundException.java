package eu.internetpolice.potionhappiness.command;

public class PlayerNotFoundException extends NoSuchFieldException {
    public PlayerNotFoundException() {
        super("The requested player cannot be found.");
    }
}
