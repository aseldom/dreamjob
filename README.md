# cinema

## О проекте

Данное web-приложение позволяет работать с вакансиями и резюме.
В системе присутствует две модели: вакансии и кандидаты. Кандидаты могут публиковать резюме. Кадровики могут публиковать вакансии о работе.
Кандидаты могут откликнуться на вакансию. Кадровик может пригласить на вакансию кандидата.

## Стек технологий
* Spring Boot
* Thymeleaf
* Bootstrap
* Liquibase
* Mockito
* Sql2o
* H2
* PostgreSQL

## Окружение
Java 17, Maven 3.8, PostgreSQL 14, H2 2.1.214, ;

## Запуск проекта
- Импортировать проект в IntelliJ IDEA
- В PostgreSQL создать БД dreamjob
- В Maven выполнить команду Plugins\liquibase\liquibase:update
- Выполнить метод main ru.job4j.dreamjob\Main.java
- Открыть веб-браузер по адресу: 127.0.0.1:8080

## Screenshots
- Главная страница
  ![](/img/screenshot_main_page.jpg)
- Перечень вакансий
  ![](/img/screenshot_vacancy_list.jpg)
- Список резюме
  ![](/img/screenshot_resume_list.jpg)
- Страница регистрации
  ![](/img/screenshot_register_page.jpg)
- Страница создания вакансии
  ![](/img/screenshot_create_vacancy.jpg)
- Страница создания резюме
  ![](/img/screenshot_create_resume.jpg)

---
#### Контакты для связи:
* email: a.seldom@gmail.com
* telegram: @aseldom