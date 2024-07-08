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
