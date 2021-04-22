package net.reliqs.outreelist.data.ou;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Transactional controller: all transactional methods need to be public!
 */
@RestController
@RequestMapping("/api/ou/{version}")
@Transactional
public class OrgUnitController {
    private static final Logger log = LoggerFactory.getLogger(OrgUnitController.class);

    private final Long version;
    private final OrgUnitRepository repo;

    public OrgUnitController(OrgUnitRepository repo) {
        log.debug("init org-unit controller");
        this.version = 1L;
        this.repo = repo;
    }

    @GetMapping("{id}")
    public ResponseEntity<OrgUnitDTO> get(@PathVariable(name = "version") Long version, @PathVariable(name = "id") UUID id) {
        Assert.isTrue(version.equals(this.version), "version mismatch");
        Optional<OrgUnitDTO> res = repo.findById(id)
                .map(ou -> ou.toDto(0));
        return ResponseEntity.of(res);
    }

    @GetMapping(value = {"descendants", "descendants/{parentId}"})
    public List<OrgUnitDTO> getDescendants(@PathVariable(name = "version") Long version, @PathVariable(name= "parentId", required = false) UUID parentId) {
        log.debug("version={}, parentId={}", version, parentId);
        Assert.isTrue(version.equals(this.version), "version mismatch");
        List<OrgUnitDTO> res = repo.descendants(parentId);
        log.debug("result {}", res);
        return res;
    }

    @PostMapping()
    public ResponseEntity<OrgUnitDTO> add(@PathVariable(name = "version") Long version, @RequestBody OrgUnitDTO dto) {
        log.info("add version={}, dto={}", version, dto);
        Assert.isTrue(version.equals(this.version), "version mismatch");
        dto.setId(UUID.randomUUID());
        OrgUnit orgUnit = OrgUnit.fromDto(dto, true);
        orgUnit = repo.save(orgUnit);
        // set level from received dto, as its value is not filled in the created entity
        OrgUnitDTO res = orgUnit.toDto(dto.getLevel());
        return ResponseEntity.created(URI.create("/api/ou/" + version + "/" + orgUnit.getId())).body(res);
    }

    @PutMapping("{id}")
    public ResponseEntity<OrgUnitDTO> update(@PathVariable(name = "version") Long version, @PathVariable(name= "id") UUID id, @RequestBody OrgUnitDTO dto) {
        log.info("update version={}, id={}, dto={}", version, id, dto);
        Assert.isTrue(id.equals(dto.getId()) && version.equals(this.version), "version mismatch");
        dto.setId(id);
        OrgUnit orgUnit = OrgUnit.fromDto(dto, true);
        assert orgUnit.getId() != null;
        if (repo.findById(orgUnit.getId()).isPresent()) {
            orgUnit = repo.save(orgUnit);
            // set level from received dto, as its value is not filled in the updated entity
            OrgUnitDTO res = orgUnit.toDto(dto.getLevel());
            log.info("update result {}", res);
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTree(@PathVariable(name = "version") Long version, @PathVariable(name= "id") UUID id) {
        Assert.isTrue(version.equals(this.version), "version mismatch");
        Optional<OrgUnit> orgUnit = repo.findById(id);
        if (orgUnit.isPresent()) {
            repo.deleteTree(orgUnit.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("generate")
    public void generate(@PathVariable(name = "version") Long version, Integer len) {
        Assert.isTrue(version.equals(this.version), "version mismatch");
        repo.generate(len);
    }

}
