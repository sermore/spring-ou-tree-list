# spring-ou-tree-list

A spring-boot simple application with a REST endpoint to manage hierarchical data.
This application is the REST data backend for the flutter project ou_tree_list.

Features:
- query ancestors and descendants with SQL CTE, with breadth first ordering
- use JPA @SqlResultSetMapping and @ConstructorResult to map native queries to DTO
- the default datasource points to a local postgresql db, which can be started with docker with command `docker run -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres`
- Produce random data adding the number of items to create as command line parameter
    - running with java `java -jar target/spring-ou-tree-list-0.0.1-SNAPSHOT.jar 100`
    - running with maven `mvn spring-boot:run -Dspring-boot.run.arguments=1000`


## Reference Documentation

- https://www.citusdata.com/blog/2018/05/15/fun-with-sql-recursive-ctes/
- https://haughtcodeworks.com/blog/software-development/recursive-sql-queries-using-ctes/
- https://www.depesz.com/2021/02/04/waiting-for-postgresql-14-search-and-cycle-clauses/
- https://www.baeldung.com/jpa-sql-resultset-mapping