package com.iprt.orm.core;

import com.iprt.orm.connection.ConnectionPool;
import com.iprt.orm.operation.*;
import com.iprt.orm.sql.MigrationRunner;
import com.iprt.orm.sql.QueryBuilder;
import java.sql.Connection;
import java.util.*;

public class ORM {
    private EntityRegistry registry;
    private ConnectionPool pool;
    private MigrationRunner migrationRunner;

    public static class Builder {
        private String host;
        private String port;
        private String database;
        private String username;
        private String password;
        private int poolSize = 10;

        public Builder host(String host) { this.host = host; return this; }
        public Builder port(String port) { this.port = port; return this; }
        public Builder database(String database) { this.database = database; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder poolSize(int poolSize) { this.poolSize = poolSize; return this; }

        public ORM build() {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            return new ORM(url, username, password, poolSize);
        }
    }

    private ORM(String url, String username, String password, int poolSize) {
        this.registry = new EntityRegistry();
        this.pool = new ConnectionPool(url, username, password, poolSize);
        this.migrationRunner = new MigrationRunner(registry, pool);
    }

    public void register(Class<?> clazz) { registry.register(clazz); }
    public void migrate() { migrationRunner.migrate(); }

    public void save(Object entity) {
        EntityMetadata meta = registry.getMetadata(entity.getClass());
        Connection conn = pool.borrow();
        try {
            new SaveOperation().save(entity, meta, conn);
        } finally {
            pool.release(conn);
        }
    }

    public <T> Optional<T> findById(Class<T> clazz, Object id) {
        EntityMetadata meta = registry.getMetadata(clazz);
        Connection conn = pool.borrow();
        try {
            return new FindOperation().findById(clazz, id, meta, conn);
        } finally {
            pool.release(conn);
        }
    }

    public <T> List<T> findAll(Class<T> clazz) {
        EntityMetadata meta = registry.getMetadata(clazz);
        Connection conn = pool.borrow();
        try {
            return new FindOperation().findAll(clazz, meta, conn);
        } finally {
            pool.release(conn);
        }
    }

    public QueryBuilder find(Class<?> clazz) {
        EntityMetadata meta = registry.getMetadata(clazz);
        Connection conn = pool.borrow();
        return new QueryBuilder(clazz, meta, conn);
    }

    public void update(Object entity) {
        EntityMetadata meta = registry.getMetadata(entity.getClass());
        Connection conn = pool.borrow();
        try {
            new UpdateOperation().update(entity, meta, conn);
        } finally {
            pool.release(conn);
        }
    }

    public void delete(Object entity) {
        EntityMetadata meta = registry.getMetadata(entity.getClass());
        Connection conn = pool.borrow();
        try {
            new DeleteOperation().delete(entity, meta, conn);
        } finally {
            pool.release(conn);
        }
    }

    public void close() { pool.close(); }
}
