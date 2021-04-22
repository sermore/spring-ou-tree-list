package net.reliqs.outreelist.data.ou;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrgUnitRepositoryCustomImplTest {
    @Autowired
    OrgUnitRepository repo;

    @Test
    public void testDescendants() {
        assertThat(repo.descendants(null), hasSize(0));

        OrgUnit o1 = new OrgUnit("o1", true);
        repo.save(o1);
        OrgUnit o2 = new OrgUnit(o1, "o2", true);
        repo.save(o2);
        OrgUnit o3 = new OrgUnit(o2, "o3", true);
        repo.save(o3);
        OrgUnit o4 = new OrgUnit(o3, "o4", true);
        repo.save(o4);
        OrgUnit o5 = new OrgUnit(o2, "o5", true);
        repo.save(o5);
        OrgUnit o6 = new OrgUnit(o1, "o6", true);
        repo.save(o6);

        assertEquals(List.of(o1.toDto(0), o2.toDto(1), o3.toDto(2), o4.toDto(3), o5.toDto(2), o6.toDto(1)), repo.descendants(null));
        assertEquals(List.of(o3.toDto(0), o4.toDto(1), o5.toDto(0)), repo.descendants(o2.getId()));
        assertEquals(List.of(), repo.descendants(o4.getId()));

    }

    @Test
    public void testAncestors() {
        OrgUnit o1 = new OrgUnit("o1", true);
        repo.save(o1);
        OrgUnit o2 = new OrgUnit(o1, "o2", true);
        repo.save(o2);
        OrgUnit o3 = new OrgUnit(o2, "o3", true);
        repo.save(o3);
        OrgUnit o4 = new OrgUnit(o3, "o4", true);
        repo.save(o4);
        OrgUnit o5 = new OrgUnit(o2, "o5", true);
        repo.save(o5);
        OrgUnit o6 = new OrgUnit(o1, "o6", true);
        repo.save(o6);

        assertEquals(List.of(o4.toDto(0), o3.toDto(1), o2.toDto(2), o1.toDto(3)), repo.ancestors(o4.getId()));
        assertEquals(List.of(o3.toDto(0), o2.toDto(1), o1.toDto(2)), repo.ancestors(o3.getId()));
        assertEquals(List.of(o5.toDto(0), o2.toDto(1), o1.toDto(2)), repo.ancestors(o5.getId()));
        assertEquals(List.of(o6.toDto(0), o1.toDto(1)), repo.ancestors(o6.getId()));
        assertEquals(List.of(o1.toDto(0)), repo.ancestors(o1.getId()));

    }

    @Test
    public void testGenerate() {
        List<OrgUnit> l = repo.generate(10);
        assertEquals(10, l.size());
        System.out.println(l);
    }
}