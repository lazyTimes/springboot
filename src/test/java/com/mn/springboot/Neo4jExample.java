package com.mn.springboot;

import org.neo4j.driver.v1.*;

/**
 * @program: springboot
 * @description: neo4j hello world
 * @author: zhaoxudong
 * @create: 2019-11-17 14:17
 **/
public class Neo4jExample implements AutoCloseable {

    private Driver driver;

    public Neo4jExample() {

    }

    public Neo4jExample(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void addPerson(String name) {

        Session session = driver.session();
        try {
            session.run("CREATE (a:Person {name: $name})", Values.parameters("name", name));
        } catch (Exception e) {

        }
    }

    // Create a company node
    private StatementResult addCompany(final Transaction tx, final String name) {
        return tx.run("CREATE (:Company {name: $name})", Values.parameters("name", name));
    }

    // Create a person node
    private StatementResult addPerson(final Transaction tx, final String name) {
        return tx.run("CREATE (:Person {name: $name})", Values.parameters("name", name));
    }

    // Create an employment relationship to a pre-existing company node.
    // This relies on the person first having been created.
    private StatementResult employ(final Transaction tx, final String person, final String company
    ) {
        return tx.run("MATCH (person:Person {name: $person_name}) " +
                        "MATCH (company:Company {name: $company_name}) " +
                        "CREATE (person)-[:WORKS_FOR]->(company)",
                Values.parameters("person_name", person, "company_name", company));
    }

    // Create a friendship between two people.
    private StatementResult makeFriends(final Transaction tx, final String person1, final String
            person2) {
        return tx.run("MATCH (a:Person {name: $person_1}) " +
                        "MATCH (b:Person {name: $person_2}) " +
                        "MERGE (a)-[:KNOWS]->(b)",
                Values.parameters("person_1", person1, "person_2", person2));
    }

    public static void main(String[] args) throws Exception {
        Neo4jExample neo4j = new Neo4jExample("bolt://127.0.0.1:7687", "neo4j", "123");
        neo4j.addPerson("user");
        neo4j.close();
    }
}
