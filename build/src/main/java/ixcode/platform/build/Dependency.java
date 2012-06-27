package ixcode.platform.build;

import static java.lang.String.format;

/**
 * This class could get more complex to allow fancy defaulting such as:
 * <p/>
 * 1) Only specify the group if group and artifact are the same 2) Don't bother with the version and have it default to
 * "latest"
 * <p/>
 * But for now, we must have them all
 */
public class Dependency {


    private final String group;
    private final String artifact;
    private final String version;

    public static Dependency parseFromString(String value) {
        String[] parts = value.split(":");

        String group = getIfPresent(parts, 0);
        String artifact = getIfPresent(parts, 1);
        String version = getIfPresent(parts, 2);

        return new Dependency(group, artifact, version);
    }

    private Dependency(String group, String artifact, String version) {
        this.group = group;
        this.artifact = artifact;
        this.version = version;
    }

    private static String getIfPresent(String[] parts, int index) {
        if (index > parts.length - 1) {
            return null;
        }
        return parts[index];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(group).append(":").append(artifact).append(":").append(version);

        return sb.toString();
    }

    public String toMavenRepositoryPath() {
        return format("%s/%s/%s/%s", group.replaceAll("\\.", "/"),
                      artifact,
                      version,
                      toJarFileName());
    }

    public String toJarFileName() {
        return format("%s-%s.jar", artifact, version);
    }
}