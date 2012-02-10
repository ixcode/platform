package ixcode.platform.repository;

public class StringRepositoryKey {


    public static StringRepositoryKeyBuilder keyFrom(Object... keys) {
        StringBuilder sb = new StringBuilder();
        for (Object object : keys) {
            sb.append("#");
            sb.append((object == null) ? "NULL" : object.toString());
        }
        return new StringRepositoryKeyBuilder(sb.toString());
    }

    private StringRepositoryKey() {
    }

    public static class StringRepositoryKeyBuilder {
        private final String key;

        public StringRepositoryKeyBuilder(String key) {
            this.key = key;
        }

        public RepositoryKey forRepository(Object forRepository) {
            return new RepositoryKey(forRepository.getClass().getName(), key);
        }
    }
}