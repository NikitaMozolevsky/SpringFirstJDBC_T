package com.nikita.lessons.dao.impl;

import com.nikita.lessons.dao.PersonDao;
import com.nikita.lessons.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
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

        return jdbcTemplate.query(SELECT_PEOPLE_SQL, );
    }

    public Person getPerson(int id) {
        Person person = new Person();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PERSON_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public void savePerson(Person person) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_PERSON_SQL)) {
            preparedStatement.setInt(1, person.getId());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setInt(3, person.getAge());
            preparedStatement.setString(4, person.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void changePerson(int id, Person updatedPerson) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PERSON_SQL)) {

            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(int id) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PERSON_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
