package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        sql2oUserRepository.clear();
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = sql2oUserRepository.save(new User(0, "ivan@mail.com", "Ivan", "pass1"));
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword()).get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user.get());
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var user1 = sql2oUserRepository.save(new User(0, "ivan1@mail.com", "Ivan1", "pass1"));
        var user2 = sql2oUserRepository.save(new User(0, "ivan2@mail.com", "Ivan2", "pass2"));
        var user3 = sql2oUserRepository.save(new User(0, "ivan3@mail.com", "Ivan3", "pass3"));
        List<Optional<User>> users = List.of(user1, user2, user3);
        for (var user : users) {
            var savedUser = sql2oUserRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword()).get();
            assertThat(savedUser).usingRecursiveComparison().isEqualTo(user.get());
        }
    }

    @Test
    public void whenSaveTwiceSameEmailThenGetEmpty() {
        var user1 = sql2oUserRepository.save(new User(0, "ivan@mail.com", "Ivan", "pass1"));
        var user2 = sql2oUserRepository.save(new User(0, "ivan@mail.com", "Ivan", "pass2"));
        assertThat(user2).isEmpty();
    }
}