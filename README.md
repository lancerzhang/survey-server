# Survey Server

Survey Server is a RESTful API built with Spring Boot for creating and managing surveys. This project supports creating users, surveys, and questions, and allows users to respond to surveys with their answers.

## Features

- Create, read, and update users
- Create, read, and update surveys
- Create, read, and update questions and options
- Respond to surveys with answers
- Query surveys by user with pagination

## Prerequisites

- JDK 1.8 or later
- Maven 3.2 or later
- An IDE like IntelliJ or Eclipse

## Installation

1. Clone the repository:
```shell
git clone https://github.com/your-username/survey-server.git
```
2. Import the project into your favorite IDE.

3. Set up the database connection in the `application.properties` file.

## Running the Application

To run the application, execute the following command in the terminal:
```shell
mvn spring-boot:run
```

The application will start running at `http://localhost:8080`.

## API Endpoints

### Users
- **Create User:** `POST /api/users`
- **Get User:** `GET /api/users/{id}`
- **Update User:** `PUT /api/users/{id}`

### Surveys
- **Create Survey:** `POST /api/surveys`
- **Get Survey:** `GET /api/surveys/{id}`
- **Update Survey:** `PUT /api/surveys/{id}`
- **Get Surveys By User:** `GET /api/surveys/user/{userId}`

### Questions
- **Create Question:** `POST /api/questions`
- **Get Question:** `GET /api/questions/{id}`
- **Update Question:** `PUT /api/questions/{id}`

### Options
- **Create Option:** `POST /api/options`
- **Get Option:** `GET /api/options/{id}`
- **Update Option:** `PUT /api/options/{id}`

## Testing

You can use Postman to test the API endpoints. Import the Postman collections provided in the previous sections to get started quickly.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT](https://choosealicense.com/licenses/mit/)


# Reference
https://start.spring.io/
https://plantuml.com/
