# PatBoot

> Main application for Patpat Online, made with 💖.

---

## 部署 & 运行

在生产环境部署项目，参见 [PatpatDeploy](https://github.com/JavaEE-PatPatOnline/PatpatDeploy)。

在本地运行项目，首先需要克隆并构建项目。

```bash
# 克隆仓库
git clone https://github.com/JavaEE-PatPatOnline/PatBoot.git
cd PatBoot

# 使用 Maven 构建项目
mvn clean install
```

构建完成后，需要在服务器或本地配置好 MySQL 和 RabbitMQ，在 IntelliJ IDEA 中创建运行配置，添加命令行参数  `--spring.profiles.active=dev`，并添加如下环境变量。

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

本地运行时，还需要额外创建目录。在项目同级创建如下目录。

```
.
|-- PatBoot
|-- PatJudge
\-- volume
    |-- bucket
    |   |-- problem
    |   \-- submission
    \-- wwwroot
```

---

## 贡献代码

如果你想让 PatBoot 继续运行下去，欢迎加入我们维护该项目，具体参考 [CONTRIBUTING.md](CONTRIBUTING.md)。