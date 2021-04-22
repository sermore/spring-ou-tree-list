package net.reliqs.outreelist.data;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class TreeNode implements Persistable<UUID>, Serializable {

    @Id
    private UUID id;
    private UUID parentId;
    @Transient
    private boolean persisted;

    protected TreeNode() {
    }

    protected TreeNode(UUID id, UUID parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    protected TreeNode(UUID id, UUID parentId, boolean persisted) {
        this(id, parentId);
        this.persisted = persisted;
    }

    public UUID getId() {
        return id;
    }

    public UUID getParentId() {
        return parentId;
    }

    @PostPersist
    @PostLoad
    private void setPersisted() {
        persisted = true;
    }

    @Override
    public boolean isNew() {
        return !persisted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode node = (TreeNode) o;
        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "id=" + id +
                ", parentId=" + parentId +
                '}';
    }
}
