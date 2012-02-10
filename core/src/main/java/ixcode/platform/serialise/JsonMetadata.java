package ixcode.platform.serialise;

public class JsonMetadata {
    public String[] tagsFor(Class itemType) {
        return new String[] {itemType.getSimpleName().toLowerCase()};
    }
}