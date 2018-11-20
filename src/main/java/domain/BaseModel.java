package domain;

import java.time.LocalDateTime;

import static foundation.Ensurer.ensureNotNull;

public abstract class BaseModel<DOMAIN_TYPE extends BaseModel, PK_TYPE extends Number>
        implements Comparable<DOMAIN_TYPE> {
    private PK_TYPE id;
    private Integer version;
    private String tableName;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private LocalDateTime deletedAt;

    protected BaseModel() {
        this.version = 0;
    }

    protected BaseModel(final PK_TYPE id, final Integer version) {
        this.id = ensureNotNull(id, "id");
        this.version = ensureNotNull(version, "version");
    }

    public final void setId(PK_TYPE id) {
        this.id = ensureNotNull(id, "id");
    }

    public final PK_TYPE getId() {
        return id;
    }

    public final void setVersion(Integer version) {
        this.version = ensureNotNull(version, "version");
    }

    public final Integer getVersion() {
        return version;
    }

    public final Boolean isNew() {
        return id == null;
    }

    public final String toString() {
        return String.format("%s{id:'%d', version:'%d', isNew: '%b'}", getClass().getSimpleName(),
                id, version,isNew());
    }
}