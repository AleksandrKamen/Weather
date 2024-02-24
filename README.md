# Обзор проекта (Weather)

![image](https://github.com/AleksandrKamen/Weather/assets/144233016/a07c372d-bdee-47c3-b5f2-4450b6595d37)

**Техническое задание проекта** -  [https://zhukovsd.github.io/java-backend-learning-course/Projects/TennisScoreboard/](https://zhukovsd.github.io/java-backend-learning-course/Projects/WeatherViewer/)

**Суть проекта** - Веб-приложение для посмотра текущей погоды для заданной локации. Зарегистрированные пользователи могут добавить выбранные локации с текущей погодой на стартовую страницу,
а также удалить локацию из избранных.    

**Используемые технологии/инструменты:**

•	[Gradle](https://gradle.org/)                                       

•	[Hibernate](https://hibernate.org/)

•	[JUnit5](https://junit.org/junit5/)

•	[Java Servlets](https://en.wikipedia.org/wiki/Jakarta_Servlet)

•	[Apache Tomcat](https://tomcat.apache.org/)

•	[Docker](https://www.docker.com/)

•	[Thymeleaf](https://www.thymeleaf.org/)

•	[Bootstrap](https://getbootstrap.com/)

•	[PostgreSQL](https://www.postgresql.org/)

**Структура базы данных**

![drawSQL-weather-export-2024-02-24](https://github.com/AleksandrKamen/Weather/assets/144233016/6a4dceae-0035-4f15-83f1-e3b407b6babc)

**Использование OpenWeatherMap API**

Для поиска данных о погоде использован сервис: https://openweathermap.org/. 

Применены следующие API методы:

1. Поиск текущих данных о погоде - https://openweathermap.org/current#one

2. Поиск локации по названию -   https://openweathermap.org/current#geocoding

Документация - https://openweathermap.org/api.


**Интерфейс Приложения** - для пользователя доступны следующие страницы:

1. *Стартовая страница (для незарегистрированного пользователя)* - имеется возможность перейти на страницу регистрации или авторизации. 

![image](https://github.com/AleksandrKamen/Weather/assets/144233016/78fde0ff-489c-4203-a2b6-ff069052c584)

2. *Страницы - регистрации и авторизации (для незарегистрированного пользователя)* - при авторизации пользователя бэкенд приложение создаёт сессию с идентификатором, и устанавливает этот идентификатор в cookies HTTP ответа, которым приложение отвечает на POST запрос формы авторизации. К тому же, сессия содержит в себе ID авторизовавшегося юзера.
Далее, при каждом запросе к любой странице, бэкенд приложение анализирует cookies из запроса и определяет, существует ли сессия для ID из cookies. Если есть - страница рендерится для того пользователя, ID которого соответствует ID сессии из cookies.

![image](https://github.com/AleksandrKamen/Weather/assets/144233016/9a834c00-96da-4630-b4cb-b315942c20c5)

![image](https://github.com/AleksandrKamen/Weather/assets/144233016/ae4d3223-622a-4f6f-a0b5-1e01809c1982)

3. *Стартовая страница зарегистрированного пользователя (с добавленными локациями)* - содержит информацию о логине текущего пользователя,  кнопку logout, а также форму поиска локаций.

![image](https://github.com/AleksandrKamen/Weather/assets/144233016/b720ccf1-05f6-455f-a41f-8598cfd0c347)

4. *Страница найденных локаций по критериям поиска* - отображает до 5 найденных совпадений с критериям поиска.

![image](https://github.com/AleksandrKamen/Weather/assets/144233016/80ca1599-16d3-44d6-b48d-76275d99b2af)

5. *Страница полной информации по выбранной локаций* - содержит более подробную информацию о погоде для выбранной локации.

![image](https://github.com/AleksandrKamen/Weather/assets/144233016/ec0a8e9c-44a8-4c59-8cb3-21c6d7f784aa)



