package com.pragma.practica.aws.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pragma.practica.aws.Person;
import com.pragma.practica.aws.client.DynamoDbClient;

public class DeletePersonHandler implements RequestHandler<Person, String> {

    public String handleRequest(Person person, Context context) {
        DynamoDbClient client = new DynamoDbClient();
        client.delete(person);
        return "Deleted successfully!";
    }
}