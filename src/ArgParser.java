public class ArgParser {
    private static ArgParser instance;

    private ArgParser() {
    }

    public static ArgParser getInstance() {
        if (instance == null) {
            instance = new ArgParser();
        }
        return instance;
    }

    public String getArgument(String[] arguments, String argName) {
        for (int i = 0; i < arguments.length - 1; i++) {
            if (arguments[i].equals(argName)) {
                return arguments[i + 1];
            }
        }
        return null;
    }
}
