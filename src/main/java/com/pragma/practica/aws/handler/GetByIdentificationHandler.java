package com.pragma.practica.aws.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pragma.practica.aws.Person;
import com.pragma.practica.aws.client.DynamoDbClient;

public class GetByIdentificationHandler implements RequestHandler<Person, Person> {

    public Person handleRequest(Person person, Context context) {
        DynamoDbClient client = new DynamoDbClient();
        return client.getByIdentification(person.getIdentificationType(), person.getIdentificationNumber());
    }
}