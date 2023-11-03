package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestRegistrationPageThenGetPage() {
        var actualPage = userController.getRegistrationPage();
        var expectedPage = "users/register";

        assertThat(actualPage).isEqualTo(expectedPage);
    }

    @Test
    public void whenRequestRegisterThenGetRegistrationPage() {
        var user = new User(1, "email@domen.org", "name", "password");
        var expectedPage = "redirect:/users/register";
        var model = new ConcurrentModel();
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var actualPage = userController.register(model, user);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void whenRequestRegisterWithSameEmailThenGetErrorPageAndErrorMessage() {
        var user = new User(1, "email@domen.org", "name", "password");
        var expectedPage = "errors/404";
        var expectedErrorMessage = "Пользователь с такой почтой уже существует";
        var model = new ConcurrentModel();
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.empty());

        var actualPage = userController.register(model, user);
        var actualErrorMessage = model.getAttribute("message");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
    }

    @Test
    public void whenRequestLoginPageThenGetPage() {
        var actualPage = userController.getLoginPage();
        var expectedPage = "users/login";

        assertThat(actualPage).isEqualTo(expectedPage);
    }

    @Test
    public void whenLoginUserThenGetVacanciesPage() {
        var user = new User(1, "email@domen.org", "name", "password");
        var expectedPage = "redirect:/vacancies";
        var model = new ConcurrentModel();
        when(userService.findByEmailAndPassword(anyString(), anyString())).thenReturn(Optional.of(user));

        var actualPage = userController.loginUser(user, model, new MockHttpServletRequest());

        assertThat(actualPage).isEqualTo(expectedPage);
    }

    @Test
    public void whenErrorLoginUserThenGetLoginPageAndErrorMessage() {
        var user = new User(1, "email@domen.org", "name", "password");
        var expectedPage = "users/login";
        var expectedMessage = "Почта или пароль введены неверно";
        var model = new ConcurrentModel();
        when(userService.findByEmailAndPassword(anyString(), anyString())).thenReturn(Optional.empty());

        var actualPage = userController.loginUser(user, model, new MockHttpServletRequest());
        var actualMessage = model.getAttribute("error");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenRequestLogoutThenGetLoginPage() {
        var actualPage = userController.logout(new MockHttpSession());
        var expectedPage = "redirect:/users/login";

        assertThat(actualPage).isEqualTo(expectedPage);
    }

}