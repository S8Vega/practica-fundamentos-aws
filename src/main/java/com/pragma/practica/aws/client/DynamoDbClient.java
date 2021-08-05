package com.pragma.practica.aws.client;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.pragma.practica.aws.Person;

public class DynamoDbClient {

    private DynamoDB dynamoDb;
    private static final String DYNAMODB_TABLE_NAME = "Person";
    private static final Regions REGION = Regions.US_EAST_1;

    public DynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }

    public PutItemOutcome save(Person person) throws ConditionalCheckFailedException {
        return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
                .putItem(
                        new PutItemSpec().withItem(new Item()
                                .withString(Person.Atributos.ID, person.getId())
                                .withString(Person.Atributos.FIRST_NAME, person.getFirstName())
                                .withString(Person.Atributos.LAST_NAME, person.getLastName())
                                .withString(Person.Atributos.IDENTIFICATION_TYPE, person.getIdentificationType())
                                .withString(Person.Atributos.IDENTIFICATION_NUMBER, person.getIdentificationNumber())
                                .withInt(Person.Atributos.AGE, person.getAge())
                                .withString(Person.Atributos.BIRTH_CITY, person.getBirthCity())
                        )
                );
    }
}
