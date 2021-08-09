package com.pragma.practica.aws.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pragma.practica.aws.Person;
import com.pragma.practica.aws.client.DynamoDbClient;

import java.util.List;

public class GetByAge implements RequestHandler<Integer, List<Person>> {

    public List<Person> handleRequest(Integer age, Context context) {
        DynamoDbClient client = new DynamoDbClient();
        return client.getByAge(age);
    }
}