package ixcode.platform.text.format;

import java.util.UUID;

public class UuidFormat implements Format {
    @Override public UUID parseString(String source) {
        return null;
    }

    @Override public String format(Object source) {
        UUID uuid = (UUID)source;

        return String.format("urn:uuid:%s", uuid.toString());
    }
}