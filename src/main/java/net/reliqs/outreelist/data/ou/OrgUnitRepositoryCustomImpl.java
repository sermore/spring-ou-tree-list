package net.reliqs.outreelist.data.ou;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
class OrgUnitRepositoryCustomImpl implements OrgUnitRepositoryCustom {
    private static final Logger log = LoggerFactory.getLogger(OrgUnitRepositoryCustomImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Stream<OrgUnitDTO> ancestorsStream(UUID id) {
        TypedQuery<OrgUnitDTO> q = em.createNamedQuery("ancestors", OrgUnitDTO.class);
        q.setParameter("id", id);
        return q.getResultStream();
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<OrgUnitDTO> descendantsStream(UUID parentId) {
        TypedQuery<OrgUnitDTO> q;
        if (parentId == null) {
            q = em.createNamedQuery("root_descendants", OrgUnitDTO.class);
        } else {
            q = em.createNamedQuery("descendants", OrgUnitDTO.class);
            q.setParameter("parentId", parentId);
        }
        return q.getResultStream();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrgUnitDTO> ancestors(UUID id) {
        return ancestorsStream(id)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrgUnitDTO> descendants(UUID parentId) {
        return descendantsStream(parentId)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void deleteTree(OrgUnit orgUnit) {
        log.info("deleteTree orgUnit={}", orgUnit);
        for (OrgUnitDTO descendant : descendants(orgUnit.getId())) {
            log.debug("delete node {}", descendant);
            OrgUnit ou = em.find(OrgUnit.class, OrgUnit.fromDto(descendant, true).getId());
            em.remove(ou);
        }
        log.debug("delete node {}", orgUnit);
        em.remove(orgUnit);
    }

    @Override
    public List<OrgUnit> generate(int len) {
        log.info("generate len={}", len);
        Faker faker = new Faker();
        Random random = new Random();
        List<OrgUnit> list = new ArrayList<>();
        Queue<OrgUnit> parentStack = new ArrayDeque<>();
        for (int i = 0; i < len; i++) {
            while(!parentStack.isEmpty() && random.nextBoolean()) {
                parentStack.poll();
            }
            OrgUnit ou;
            if (parentStack.isEmpty()) {
                ou = new OrgUnit(null, String.format("%04d - %s", i, faker.company().profession()), true);
            } else {
                OrgUnit parent = parentStack.peek();
//                ou = new OrgUnit(parent, String.format("%d - %s / %s", i, parent.getName().split("-")[1].trim(), faker.company().profession()), true);
                ou = new OrgUnit(parent, String.format("%s/%04d - %s", parent.getName().split("-")[0].trim(), i, faker.company().profession()), true);
            }
            em.persist(ou);
            list.add(ou);
            parentStack.offer(ou);
        }
        log.info("generation complete");
        return list;
    }

}
