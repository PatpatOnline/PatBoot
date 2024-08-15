# PatBoot

> Main application for Patpat Online

---

## Before you start

To run the project locally, you need to set up the following environment variables in your IDEA configuration.

- MySQL
  - `MYSQL_HOST`: The host of the MySQL server
  - `MYSQL_PORT`: The port of the MySQL server, default is `3306`
  - `MYSQL_DATABASE`: The database name, default is `patpat-*` (refer to `application-*.yml`)
  - `MYSQL_USERNAME`: The username of the MySQL server
  - `MYSQL_PASSWORD`: The password of the MySQL server
- RabbitMQ
  - `RABBITMQ_HOST`: The host of the RabbitMQ server
  - `RABBITMQ_PORT`: The port of the RabbitMQ server, default is `5672`
  - `RABBITMQ_USERNAME`: The username of the RabbitMQ server
  - `RABBITMQ_PASSWORD`: The password of the RabbitMQ server
- JWT
  - `JWT_SECRET`: The secret key for JWT
- Base URL
  - `HTTP_URL`: The base URL of the application, default is `http://localhost:8080`
  - `WS_URL`: The base URL of the WebSocket server, default is `ws://localhost:8080`

---

## Volume Structure

- `/log/`: The log directory
- `/bucket/`: The private bucket for storing files
  - `${buaa-id}/`: Private files uploaded by the user
  - `problem/${problem-id}/`: The problem directory
  - `course/${course-id}/`: Course material directory
  - `lab/${lab-id}/${buaa-id}-${name}`: Lab report directory
  - `iter/${iter-id}/${buaa-id}-${name}`: Iteration submission directory
  - `submission/${submission-id}/${buaa-id}`: Problem submission directory
  - `temp/`: Temporary files
- `/judge/`: The judge sandbox directory
  - `${random}/`: A copy of a submission for judging
- `/wwwroot`: The public bucket for storing files
  - `${buaa-id}/`: Public files uploaded by the user, e.g. avatar
  - `${files}`: Global public files
