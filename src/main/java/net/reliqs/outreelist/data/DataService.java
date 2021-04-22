package net.reliqs.outreelist.data;

import net.reliqs.outreelist.data.ou.OrgUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataService {
    private static Logger log = LoggerFactory.getLogger(DataService.class);

    private final OrgUnitRepository repo;

    public DataService(OrgUnitRepository repo) {
        this.repo = repo;
    }

    public void generate(int len) {
        repo.generate(len);
    }
}
