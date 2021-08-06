package com.pragma.practica.aws.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pragma.practica.aws.Person;
import com.pragma.practica.aws.client.DynamoDbClient;

import java.util.List;

public class GetAllPersonHandler implements RequestHandler<String, List<Person>> {

    public List<Person> handleRequest(String str, Context context) {
        DynamoDbClient client = new DynamoDbClient();
        return client.getAll();
    }
}