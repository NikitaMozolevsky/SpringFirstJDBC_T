package com.nikita.lessons.dao;

import com.nikita.lessons.models.Person;

import java.util.List;

public interface PersonDao {
    List<Person> getPeople();
    Person getPerson(int id);
    void savePerson(Person person);
    void changePerson(int id, Person updatedPerson);
    void deletePerson(int id);
}
