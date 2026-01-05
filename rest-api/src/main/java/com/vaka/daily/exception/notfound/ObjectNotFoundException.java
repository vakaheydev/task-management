package com.vaka.daily.exception.notfound;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Common exception if object not found by ID, unique name or both
 */
public abstract class ObjectNotFoundException extends RuntimeException {
    private final Map<String, Object> details;

    protected ObjectNotFoundException(String detailName, Object detailValue) {
        super("Object wasn't found. See details for more details.");
        this.details = new HashMap<>();
        putDetail(detailName, detailValue);
        putDetail("objectName", getObjectName());
    }

    protected abstract String getObjectName();

    protected void putDetail(String detailName, Object detailValue) {
        details.put(detailName, detailValue);
    }

    public Optional<Object> getDetail(String detailName) {
        return Optional.ofNullable(details.get(detailName));
    }

    public Set<Map.Entry<String, Object>> getDetails() {
        return Set.copyOf(details.entrySet());
    }
}
