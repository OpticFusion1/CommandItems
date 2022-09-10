package optic_fusion1.commanditems.util;

public final class StringUtils {

    private StringUtils() {
    }

    // TODO: Improve this if possible
    public static String join(String[] args, int endIndex) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < endIndex; i++) { // 1: So it skips the first arg "assign"/"unassign"
            builder.append(args[i]).append(" ");
        }
        return builder.toString().trim();
    }

}
