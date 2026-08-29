package com.iprt.orm.demo;

import com.iprt.orm.core.ORM;
import org.junit.jupiter.api.*;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrmIntegrationTest {

    static ORM orm;
    static Long savedId;

    @BeforeAll
    static void setup() throws Exception {
        Properties props = new Properties();
        InputStream input = OrmIntegrationTest.class
            .getClassLoader()
            .getResourceAsStream("test.properties");
        props.load(input);

        orm = new ORM.Builder()
            .host(props.getProperty("db.host"))
            .port(props.getProperty("db.port"))
            .database(props.getProperty("db.database"))
            .username(props.getProperty("db.username"))
            .password(props.getProperty("db.password"))
            .poolSize(3)
            .build();

        orm.register(User.class);
        orm.migrate();
    }

    @Test
    @Order(1)
    void testSave() {
        User user = new User("Amina Kimaro", "0712345678");
        orm.save(user);
        assertNotNull(user.getId());
        savedId = user.getId();
        System.out.println("Saved: " + user);
    }

    @Test
    @Order(2)
    void testFindById() {
        Optional<User> found = orm.findById(User.class, savedId);
        assertTrue(found.isPresent());
        assertEquals("Amina Kimaro", found.get().getDisplayName());
        System.out.println("Found: " + found.get());
    }

    @Test
    @Order(3)
    void testUpdate() {
        Optional<User> found = orm.findById(User.class, savedId);
        assertTrue(found.isPresent());
        User user = found.get();
        user.setDisplayName("Amina Hassan");
        orm.update(user);
        Optional<User> updated = orm.findById(User.class, savedId);
        assertEquals("Amina Hassan", updated.get().getDisplayName());
        System.out.println("Updated: " + updated.get());
    }

    @Test
    @Order(4)
    void testFindAll() {
        List<User> all = orm.findAll(User.class);
        assertFalse(all.isEmpty());
        System.out.println("All users: " + all);
    }

    @Test
    @Order(5)
    void testDelete() {
        Optional<User> found = orm.findById(User.class, savedId);
        assertTrue(found.isPresent());
        orm.delete(found.get());
        Optional<User> deleted = orm.findById(User.class, savedId);
        assertFalse(deleted.isPresent());
        System.out.println("Deleted successfully");
    }

    @AfterAll
    static void teardown() {
        if (orm != null) orm.close();
    }
}
