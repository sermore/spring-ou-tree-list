package net.reliqs.outreelist.data.ou;

import java.util.Objects;
import java.util.UUID;

public class OrgUnitDTO {
    private UUID id;
    private UUID parentId;
    private Integer level;
    private String name;
    private Boolean active;

    public OrgUnitDTO() {
    }

    public OrgUnitDTO(UUID id, UUID parentId, Integer level, String name, Boolean active) {
        this.id = id;
        this.parentId = parentId;
        this.level = level;
        this.name = name;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrgUnitDTO that = (OrgUnitDTO) o;
        return id.equals(that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(level, that.level) && Objects.equals(name, that.name) && Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, level, name, active);
    }

    @Override
    public String toString() {
        return "OrgUnitDTO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", level=" + level +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
