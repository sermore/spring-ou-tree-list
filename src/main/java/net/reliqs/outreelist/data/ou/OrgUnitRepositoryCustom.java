package net.reliqs.outreelist.data.ou;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

interface OrgUnitRepositoryCustom {

    Stream<OrgUnitDTO> ancestorsStream(UUID id);
    Stream<OrgUnitDTO> descendantsStream(UUID id);
    List<OrgUnitDTO> ancestors(UUID id);
    List<OrgUnitDTO> descendants(UUID id);

    void deleteTree(OrgUnit orgUnit);
    List<OrgUnit> generate(int len);
}
