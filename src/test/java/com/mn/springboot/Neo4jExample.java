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

    public Neo4jExample(){

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
        }catch (Exception e){

        }
    }

    public static void main(String[] args) throws Exception {
        Neo4jExample neo4j = new Neo4jExample("bolt://127.0.0.1:7687", "neo4j", "123");
        neo4j.addPerson("user");
        neo4j.close();
    }
}
