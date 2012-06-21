package ixcode.platform.serialise;

import ixcode.platform.repository.Resource;

import java.lang.annotation.Annotation;

public class JsonMetadata {
    public String[] tagsFor(Class itemType) {
        return new String[] {itemType.getSimpleName().toLowerCase()};
    }

    public boolean previewList(Class<?> itemType) {
        return itemType.getAnnotation(Resource.class).previewList();
    }

    public boolean isQueryable(Class<?> itemType) {
        return itemType.getAnnotation(Resource.class).queryable();
    }
}