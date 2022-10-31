package com.nikita.lessons.dao.impl;

import com.nikita.lessons.dao.PersonDao;
import com.nikita.lessons.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class PersonDaoImpl implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static final String SELECT_PEOPLE_SQL = "SELECT * FROM Person";
    public static final String INSERT_INTO_PERSON_SQL = "INSERT INTO Person (name, age, email) VALUES (?, ?, ?)";
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
        jdbcTemplate.update(INSERT_INTO_PERSON_SQL, person.getName(), person.getAge(), person.getEmail());
    }

    public void changePerson(int id, Person updatedPerson) {
        jdbcTemplate.update(UPDATE_PERSON_SQL,
                updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void deletePerson(int id) {
        jdbcTemplate.update(DELETE_PERSON_SQL, id);
    }

    /**Performance Test*/

    public void testMultipleUpdate() {
        List<Person> personList = create1000People();

        long before = System.currentTimeMillis();

        for (Person person : personList) {
            jdbcTemplate.update(INSERT_INTO_PERSON_SQL, person.getName(), person.getAge(), person.getEmail());
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }

    @Override
    public void testBatchUpdate() {
        List<Person> personList = create1000People();

        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate(INSERT_INTO_PERSON_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, personList.get(i).getName());
                preparedStatement.setInt(2, personList.get(i).getAge());
                preparedStatement.setString(3, personList.get(i).getEmail());
            }

            @Override
            public int getBatchSize() {
                return personList.size();
            }
        });

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }

    private List<Person> create1000People() {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            personList.add(new Person(i, "Name " + i, 33, "test" + i + "@mail.ru"));
        }
        return personList;
    }
}
