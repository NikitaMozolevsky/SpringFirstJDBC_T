package com.nikita.lessons.dao.impl;

import com.nikita.lessons.dao.PersonDao;
import com.nikita.lessons.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDaoImpl implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static final String SELECT_PEOPLE_SQL = "SELECT * FROM Person";
    public static final String INSERT_INTO_PERSON_SQL = "INSERT INTO Person VALUES (?, ?, ?, ?)";
    public static final String SELECT_PERSON_BY_ID_SQL = "SELECT * FROM Person WHERE Person.id = ?";
    public static final String UPDATE_PERSON_SQL = "UPDATE Person SET name = ?, age = ?, email = ? WHERE id = ?";
    public static final String DELETE_PERSON_SQL = "DELETE FROM Person WHERE id = ?";

    public List<Person> getPeople() {
        return jdbcTemplate.query(SELECT_PEOPLE_SQL, new BeanPropertyRowMapper<>(Person.class));
    }

    public Person getPerson(int id) {
        return jdbcTemplate.query(SELECT_PERSON_BY_ID_SQL, new BeanPropertyRowMapper<>(Person.class), id)
                .stream().findAny().orElse(null);
    }

    public void savePerson(Person person) {
        jdbcTemplate.update(INSERT_INTO_PERSON_SQL, person.getId(), person.getName(), person.getAge(), person.getEmail());
    }

    public void changePerson(int id, Person updatedPerson) {
        jdbcTemplate.update(UPDATE_PERSON_SQL,
                updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void deletePerson(int id) {
        jdbcTemplate.update(DELETE_PERSON_SQL, id);
    }
}
