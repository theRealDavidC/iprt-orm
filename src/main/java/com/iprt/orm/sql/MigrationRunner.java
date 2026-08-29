package com.iprt.orm.sql;

import com.iprt.orm.connection.ConnectionPool;
import com.iprt.orm.core.EntityMetadata;
import com.iprt.orm.core.EntityRegistry;
import java.sql.*;

public class MigrationRunner {
    private EntityRegistry registry;
    private ConnectionPool pool;

    public MigrationRunner(EntityRegistry registry, ConnectionPool pool) {
        this.registry = registry;
        this.pool = pool;
    }

    public void migrate() {
        Connection conn = null;
        try {
            conn = pool.borrow();
            for (EntityMetadata meta : registry.getAll()) {
                String sql = SqlGenerator.generateCreateTable(meta);
                conn.createStatement().executeUpdate(sql);
                System.out.println("Table created: " + meta.getTableName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to migrate", e);
        } finally {
            if (conn != null) pool.release(conn);
        }
    }
}
