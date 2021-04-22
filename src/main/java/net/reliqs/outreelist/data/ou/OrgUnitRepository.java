package net.reliqs.outreelist.data.ou;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface OrgUnitRepository extends PagingAndSortingRepository<OrgUnit, UUID>, OrgUnitRepositoryCustom {
    Stream<OrgUnitDTO> ancestorsStream(UUID id);
    Stream<OrgUnitDTO> descendantsStream(UUID parentId);
    List<OrgUnitDTO> ancestors(UUID id);
    List<OrgUnitDTO> descendants(UUID parentId);
}
