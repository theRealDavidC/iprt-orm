# iprt-orm

A lightweight ORM built from scratch in Java.
Maps Java classes to PostgreSQL tables using reflection and annotations.

## What it does
- Maps Java classes to database tables automatically
- Generates SQL from Java objects
- Supports CRUD: save, findById, findAll, update, delete
- Supports fluent queries: find().where().orderBy().limit().execute()
- Runs migrations automatically on startup
- Manages a connection pool internally

## Built by
iprt learning how ORMs work internally by building one.
