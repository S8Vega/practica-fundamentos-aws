package com.pragma.practica.aws.client;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.pragma.practica.aws.Person;

import java.util.*;

public class DynamoDbClient {

    private final DynamoDB dynamoDb;
    private static final String DYNAMODB_TABLE_NAME = "Person";
    private static final Regions REGION = Regions.US_EAST_1;

    public DynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }

    public Table getTable() {
        return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME);
    }

    public void save(Person person) throws ConditionalCheckFailedException {
        getTable().putItem(
                new PutItemSpec().withItem(new Item()
                        .withString(Person.Attributes.ID, person.getId())
                        .withString(Person.Attributes.FIRST_NAME, person.getFirstName())
                        .withString(Person.Attributes.LAST_NAME, person.getLastName())
                        .withString(Person.Attributes.IDENTIFICATION_TYPE, person.getIdentificationType())
                        .withString(Person.Attributes.IDENTIFICATION_NUMBER, person.getIdentificationNumber())
                        .withInt(Person.Attributes.AGE, person.getAge())
                        .withString(Person.Attributes.BIRTH_CITY, person.getBirthCity())
                )
        );
    }

    public List<Person> getAll() {
        Table table = getTable();
        ItemCollection<ScanOutcome> items = table.scan(new ScanSpec());
        return getPersonList(items.iterator());
    }


    public void update(Person person) {
        ValueMap attributes = new ValueMap();
        attributes.put(":fn", person.getFirstName());
        attributes.put(":ln", person.getLastName());
        attributes.put(":it", person.getIdentificationType());
        attributes.put(":in", person.getIdentificationNumber());
        attributes.put(":a", person.getAge());
        attributes.put(":b", person.getBirthCity());
        String updateExpression = "set " +
                Person.Attributes.FIRST_NAME + " = :fn, " +
                Person.Attributes.LAST_NAME + " = :ln, " +
                Person.Attributes.IDENTIFICATION_TYPE + " = :it, " +
                Person.Attributes.IDENTIFICATION_NUMBER + " = :in, " +
                Person.Attributes.AGE + " = :a, " +
                Person.Attributes.BIRTH_CITY + " = :b";
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey(Person.Attributes.ID, person.getId())
                .withUpdateExpression(updateExpression)
                .withValueMap(attributes);
        getTable().updateItem(updateItemSpec);
    }

    public void delete(Person person) {
        getTable().deleteItem(Person.Attributes.ID, person.getId());
    }

    public Person getByIdentification(String type, String number) {

        String conditionExpression = "#it = :it and #in = :in";

        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("#it", Person.Attributes.IDENTIFICATION_TYPE);
        nameMap.put("#in", Person.Attributes.IDENTIFICATION_NUMBER);

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(":it", type);
        valueMap.put(":in", number);

        ScanSpec scanSpec = new ScanSpec()
                .withFilterExpression(conditionExpression)
                .withNameMap(nameMap)
                .withValueMap(valueMap);

        ItemCollection<ScanOutcome> items = getTable().scan(scanSpec);

        List<Person> persons = getPersonList(items.iterator());
        if (persons.isEmpty()) return Person.builder().build();
        return persons.get(0);
    }

    public List<Person> getByAge(int age) {

        String conditionExpression = "#a >= :a";

        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("#a", Person.Attributes.AGE);

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(":a", age);

        ScanSpec scanSpec = new ScanSpec()
                .withFilterExpression(conditionExpression)
                .withNameMap(nameMap)
                .withValueMap(valueMap);

        ItemCollection<ScanOutcome> items = getTable().scan(scanSpec);

        return getPersonList(items.iterator());
    }

    private List<Person> getPersonList(Iterator<Item> iter) {
        List<Person> persons = new ArrayList<>();
        while (iter.hasNext()) {
            Item item = iter.next();
            persons.add(
                    Person.builder()
                            .id(item.getString(Person.Attributes.ID))
                            .firstName(item.getString(Person.Attributes.FIRST_NAME))
                            .lastName(item.getString(Person.Attributes.LAST_NAME))
                            .identificationType(item.getString(Person.Attributes.IDENTIFICATION_TYPE))
                            .identificationNumber(item.getString(Person.Attributes.IDENTIFICATION_NUMBER))
                            .age(item.getInt(Person.Attributes.AGE))
                            .birthCity(item.getString(Person.Attributes.BIRTH_CITY))
                            .build()
            );
        }
        return persons;
    }

}
