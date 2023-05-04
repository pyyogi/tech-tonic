
# TechTonic (Backend)
Backend часть магазина цифровой техники TechTonic разработана с использованием Spring Boot и Hibernate.
Актуальная версия frontend части находится по адресу: ... 
## Использованные технологии
+ Spring (Boot, Data, Security)
+ JPA / Hibernate
+ PostgreSQL
+ Maven
+ Lombok, slf4j
+ Postman
## Функционал
Пользователь без роли может:
+ смотреть главную страницу с товарами
+ зарегистрироваться (получить роль USER)
+ авторизоваться
+ смотреть страницу About (О авторе)

Пользователь с ролью USER может:
+ смотреть страницу About (О авторе)
+ выйти из аккаунта
+ смотреть главную страницу с товарами
+ искать товар по названию, типу, бренду, цене
+ добавить товар в корзину
+ смотреть список выбранных товаров в корзине
+ выбрать количество каждого товара в корзине
+ заказать выбранное количество товара в корзине
+ смотреть список своих заказов
+ смотреть детали каждого своего заказа
+ сортировать по возрастанию/убыванию таблицу своих заказов по ID (чем меньше ID, тем старее заказ) и суммарной цене заказа

Пользователь с ролью ADMIN может:
+ смотреть страницу About (О авторе)
+ выйти из аккаунта
+ смотреть главную страницу с товарами
+ искать товар по названию, типу, бренду, цене
+ удалить/обновить товар
+ смотреть таблицу, включающую в себя всех пользователей с ролью USER: ID пользователя, username пользователя, count orders (общее количество заказов) пользователя, user cost (общее количество потраченных денег) пользователя
+ сортировать по возрастанию/убыванию каждую колонку таблицы с пользователями с ролью USER
+ удалить любого пользователя с ролью USER
+ смотреть таблицу со всеми заказами пользователей с ролью USER
+ сортировать по возрастанию/убыванию  таблицу со всеми заказами пользователей с ролью USER
+ смотреть детали любого заказа из таблицы с заказами



[//]: # (![Solution Overview]&#40;https://github.com/IMS94/spring-boot-jwt-authorization/blob/master/authorization_process.png?raw=true "Solution Overview"&#41;)

## Диаграмма сущности базы данных

```mermaid
erDiagram
    User }o--o{ Device : owns
    User }o--|{ Role : has
    Order ||--|{ OrderItem : contains
    OrderItem }|--o| Device : has
    Order }|--o| User : placed
```



## Диаграмма работы controller-model 
### AuthController
```mermaid
flowchart LR;
    A[AuthController]-->B(UserService);
    A-->C(UserRepository);
    A-->G(JwtHelper);
    B-->C;
    B-->H(PasswordEncoder);
    C-->Y[(kursach3 database)];
```

### GuestController
```mermaid
flowchart LR;
  DS[DispatcherServlet]-->A;
  A[GuestController] -- uses --> B(DeviceService)
  B -- uses --> C(DeviceRepository)
  C -- uses --> D[(kursach3 database)]
```

### UserController
```mermaid
flowchart LR;
    DS[DispatcherServlet]-->A;
    A[UserController]-->B(UserService);
    A-->C(DeviceService);
    A-->D(OrderService);
    B-->E(UserRepository);
    B-->F(RoleRepository);
    B-->G(OrderRepository);
    B-->H(PasswordEncoder);
    C-->E;
    C-->D;
    D-->K(UserRepository);
    D-->G;
    
    E-->Y[(kursach3 database)];
    G-->Y;
    F-->Y;
    B-->Y;
    
```

### AdminController
```mermaid
flowchart LR;
    DS[DispatcherServlet]-->A[AdminController];
    A-->B(OrderService);
    A-->C(UserService);
    A-->D(DeviceService);
    
    B-->E(OrderRepository);
    B-->D;
    B-->X(OrderItemRepository);
    B --> F(UserRepository);
    
    C-->F;
    C-->G(RoleRepository);
    C-->E;
    
    D-->H(DeviceRepository);
    
    
    E-->Y[(kursach3 database)];
    F-->Y;
    G-->Y;
    H-->Y;
    X-->Y;

```


## Функционал

### Пример работы приложения
```mermaid
sequenceDiagram
    participant User
    participant Web App
    participant Authentication Service
    participant Order Service
    participant Device Service
    participant User Service
    User->>Web App: Посещение сайта
    Web App->>Authentication Service: Отправка JWT токена для аутентификации
    Authentication Service-->>Web App: Подтверждение аутентификации пользователя
    User->>Web App: Просмотр устройств
    Web App->>Device Service: Получение списка устройств
    Device Service-->>Web App: Возврат списка устройств
    User->>Web App: Добавление устройства в корзину
    Web App->>User Service: Добавление устройства в устройства пользователя
    User Service-->>Web App: Возврат списка устройств пользователя
    User->>Web App: Оформление заказа
    Web App->>Order Service: Обработка заказа
    Order Service->>User Service: Удаление устройств пользователя
    User Service->>Order Service: Подтверждение удаления устройства
    Order Service-->>Web App: Возврат информации о заказе
    User->>Web App: Просмотр заказов пользователя
    Web App->>Device Service: Получение списка заказов пользователя
    Device Service-->>Web App: Возврат списка списка заказов пользователя
    User->>Web App: Выход из системы
    Web App->>Authentication Service: Удаление JWT токена
    Authentication Service-->>Web App: Сеанс завершен
```
### Блок-схема возможностей пользователя
```mermaid
graph LR
A[Пользователь] --> B[Регистрация]
A --> C[Авторизация]
A --> D[Получение списка девайсов]
A --> E[Получение информации об авторе]
B --> C
C --> |Роль: USER| G[Получение списка девайсов в корзине]
G --> |Роль: USER| H[Удаление девайса из корзины]
C --> |Роль: USER| I[Получение списка заказов]
G --> |Роль: USER| J[Создание заказа]
I --> |Роль: USER| K[Получение деталей заказа]
C --> |Роль: Admin| L[Получение списка всех заказов]
L --> |Роль: Admin| M[Получение заказа по ID]
D --> |Роль: Admin| N[Получение информации о девайсе по ID]
D --> |Роль: Admin| O[Удаление девайса по ID]
D --> |Роль: Admin| P[Создание нового девайса]
N --> |Роль: Admin| Q[Изменение информации о девайсе]
C --> |Роль: Admin| R[Получение списка пользователей]
R --> |Роль: Admin| S[Удаление пользователя по ID]
C --> |Роль: Admin| D
C --> |Роль: USER| D
D --> |Роль: USER| X[Добавление девайса в корзину]

```


## Getting Started

- Используй команду `mvn clean install` в корневой директории проекта, чтобы собрать проект. 
- Запусти главный класс `com.kursach.TechTonicApplication`, чтобы запустить приложение.

## Endpoints

#### Общедоступные endpoints:
- `POST /signup` -> отправляет запрос на регистрацию нового пользователя
- `POST /login` -> отправляет запрос на подтверждение JWT токена для валидации пользователя (username/password)
- `GET /devices` -> возвращает список всех девайсов магазина
- `GET /about` -> возвращает информацию об авторе

#### Endpoints доступные пользователю с ролью USER:
- `GET /users/{username}/devices` -> возвращает список девайсов из корзины пользователя с ником `username`
- `POST /users/{username}/devices` -> отправляет запрос на добавление устройства в корзину пользователя с ником `username`
- `POST /users/{username}/devices/{deviceId}` -> отправляет запрос на удаление устройства с `deviceId` из корзины пользователя с ником `username`
- `GET /users/{username}/orders` -> возвращает список заказов пользователя с ником `username`
- `POST /users/{username}/orders` -> отправляет запрос на создание заказа пользователем с ником `username`
- `GET /users/{username}/orders/{orderId}` -> возваращает детали заказа с номером `orderId` пользователя с ником `username`

#### Endpoints доступные пользователю с ролью Admin:
- `GET /admins/orders` -> возвращает список заказов всех пользователей
- `GET /admins/orders/{orderId}` -> возвращает заказ под номером `orderId`
- `GET /admins/devices/{deviceId}` -> возвращает девай под номером `deviceId`
- `GET /admins/devices/delete/{id}` -> отправляет запрос на удаление девайса под номером `id`
- `POST /admins/devices` -> отправляет запрос на создание нового девайса
- `POST /admins/devices/update` -> отправляет запрос на изменение уже существующего девайса
- `GET /admins/users` -> возвращает список пользователей
- `POST /admins/users/{id}` -> отправляет запрос на удаление пользователя под номером `id`