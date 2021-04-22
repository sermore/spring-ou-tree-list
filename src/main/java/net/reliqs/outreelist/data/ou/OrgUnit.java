package net.reliqs.outreelist.data.ou;

import net.reliqs.outreelist.data.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "ancestors",
                query = "with recursive ancestors(parent_id, id, level, name, active, path) as (" +
                        "(select ou.parent_id, ou.id, 0 as level, cast(ou.name as varchar(500)), ou.active, cast (('/' || ou.name) as varchar(5000)) path from org_unit ou where ou.id = :id)" +
                        "union all" +
                        "(select this.parent_id, this.id, next.level + 1 as level, this.name, this.active, cast((next.path || '/' || this.name) as varchar(5000)) as path from ancestors next inner join org_unit this on (this.id = next.parent_id))" +
                        ")" +
                        "select a.parent_id, a.id, a.level, a.name, a.active, a.path from ancestors a order by a.path asc",
                resultSetMapping = "OrgUnitDTOMapping"
        ),
        @NamedNativeQuery(
                name = "descendants",
                query = "with recursive descendants(parent_id, id, level, name, active, path) as (" +
                        "(select ou.parent_id, ou.id, 0 as level, ou.name, ou.active, cast (('/' || ou.name) as varchar(5000)) path from org_unit ou where ou.parent_id = :parentId)" +
                        "union all" +
                        "(select this.parent_id, this.id, prior.level + 1 as level, this.name, this.active, cast((prior.path || '/' || this.name) as varchar(5000)) as path from descendants prior inner join org_unit this on (this.parent_id = prior.id))" +
                        ")" +
                        "select d.parent_id, d.id, d.level, d.name, d.active, d.path from descendants d order by d.path asc",
                resultSetMapping = "OrgUnitDTOMapping"
        ),
        @NamedNativeQuery(
                name = "root_descendants",
                query = "with recursive descendants(parent_id, id, level, name, active, path) as (" +
                        "(select ou.parent_id, ou.id, 0 as level, ou.name, ou.active, cast (('/' || ou.name) as varchar(5000)) path from org_unit ou where ou.parent_id is null)" +
                        "union all" +
                        "(select this.parent_id, this.id, prior.level + 1 as level, this.name, this.active, cast((prior.path || '/' || this.name) as varchar(5000)) as path from descendants prior inner join org_unit this on (this.parent_id = prior.id))" +
                        ")" +
                        "select d.parent_id, d.id, d.level, d.name, d.active, d.path from descendants d order by d.path asc",
                resultSetMapping = "OrgUnitDTOMapping"
        )
})
@SqlResultSetMapping(
        name = "OrgUnitDTOMapping",
        classes = @ConstructorResult(
                targetClass = OrgUnitDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = UUID.class),
                        @ColumnResult(name = "parent_id", type = UUID.class),
                        @ColumnResult(name = "level", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "active", type = Boolean.class),
                }))
public class OrgUnit extends TreeNode {

    @Column(length = 5000)
    private String name;
    private Boolean active;

    OrgUnit(String name, Boolean active) {
        super(UUID.randomUUID(), null);
        this.name = name;
        this.active = active;
    }

    OrgUnit(OrgUnit parent, String name, Boolean active) {
        super(UUID.randomUUID(), parent == null ? null : parent.getId());
        this.name = name;
        this.active = active;
    }

    private OrgUnit(UUID id, UUID parentId, String name, Boolean active, boolean persisted) {
        super(id == null ? UUID.randomUUID() : id, parentId, persisted);
        this.name = name;
        this.active = active;
    }

    protected OrgUnit(OrgUnit orgUnit) {
        super(orgUnit.getId(), orgUnit.getParentId());
        this.name = orgUnit.name;
        this.active = orgUnit.active;
    }

    OrgUnit() {
        super();
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

    OrgUnit copy() {
        return new OrgUnit(this);
    }

    static OrgUnit fromDto(OrgUnitDTO dto, boolean persisted) {
        return new OrgUnit(dto.getId(), dto.getParentId(), dto.getName(), dto.getActive(), persisted);
    }

    public OrgUnitDTO toDto(int level) {
        return new OrgUnitDTO(getId(), getParentId(), level, getName(), getActive());
    }

    @Override
    public String toString() {
        return "OrgUnit{" +
                "id=" + getId() +
                ", parentId=" + getParentId() +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
