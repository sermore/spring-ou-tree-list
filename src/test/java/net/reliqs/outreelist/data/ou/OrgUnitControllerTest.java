package net.reliqs.outreelist.data.ou;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrgUnitControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private OrgUnitRepository repo;

    @Test
    public void jsonTransform() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        OrgUnitDTO o1 = new OrgUnitDTO(UUID.fromString("c1e9a306-2b80-451f-a93a-2d7427865668"), null, 0, "o1", true);
        String json = m.writeValueAsString(o1);
        assertEquals("{\"id\":\"c1e9a306-2b80-451f-a93a-2d7427865668\",\"parentId\":null,\"level\":0,\"name\":\"o1\",\"active\":true}", json);
        OrgUnitDTO o = m.readValue(json, OrgUnitDTO.class);
        System.out.println("ou=" + o);
        assertEquals(o, o1);
    }

    @Test
    public void testGet() {
        OrgUnit o1 = new OrgUnit("o1", true);
        repo.save(o1);

        ResponseEntity<OrgUnitDTO> response = this.restTemplate.getForEntity("/api/ou/1/" + o1.getId(), OrgUnitDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(o1.getId(), response.getBody().getId());
    }

    @Test
    public void descendants() {
        ResponseEntity<OrgUnitDTO[]> response = restTemplate.getForEntity("/api/ou/1/descendants", OrgUnitDTO[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void add() {
        OrgUnitDTO o1 = new OrgUnitDTO(UUID.fromString("c1e9a306-2b80-451f-a93a-2d7427865668"), null, 0, "o1", true);
        ResponseEntity<OrgUnitDTO> response = this.restTemplate.postForEntity("/api/ou/1", o1, OrgUnitDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertEquals(URI.create("/api/ou/1/" + response.getBody().getId()), response.getHeaders().getLocation());
        assertEquals(o1.getName(), response.getBody().getName());
        assertNotEquals(o1.getId(), response.getBody().getId());
    }

    @Test
    public void addWithoutId() {
        OrgUnitDTO o1 = new OrgUnitDTO(null, null, 0, "o1", true);
        ResponseEntity<OrgUnitDTO> response = this.restTemplate.postForEntity("/api/ou/1", o1, OrgUnitDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("/api/ou/1/" + response.getBody().getId()));
    }

}