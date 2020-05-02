package eu.internetpolice.potionhappiness.command;

public class PotionNotFoundException extends NoSuchFieldException {
    public PotionNotFoundException() {
        super("The requested PotionEffectType cannot be found.");
    }
}
