package com.pragma.practica.aws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    private String id;
    private String firstName;
    private String lastName;
    private String identificationType;
    private String identificationNumber;
    private int age;
    private String birthCity;

    public static class Attributes {
        public static final String ID = "id";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String IDENTIFICATION_TYPE = "identificationType";
        public static final String IDENTIFICATION_NUMBER = "identificationNumber";
        public static final String AGE = "age";
        public static final String BIRTH_CITY = "birthCity";

        Attributes() {
            throw new IllegalStateException("Utility class");
        }
    }
}
