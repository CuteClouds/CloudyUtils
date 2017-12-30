package xyz.cuteclouds.utils.args.tuples;

public class Text implements Arg {
    private final String value;

    public Text(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String value() {
        return value;
    }
}
