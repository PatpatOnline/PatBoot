############################################################
#                         Account                          #
############################################################
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`
(
    `id`       int         NOT NULL AUTO_INCREMENT,
    `buaa_id`  varchar(10) NOT NULL UNIQUE,
    `name`     varchar(31) NOT NULL,
    `password` varchar(31) NOT NULL,
    `gender`   int         NOT NULL default 0,
    `school`   varchar(63) NOT NULL,
    `teacher`  bool        NOT NULL default false,
    `ta`       bool        NOT NULL default false,
    `avatar`   varchar(63) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `account_buaa_id_index` ON `account` (`buaa_id`);
CREATE INDEX `account_name_index` ON `account` (`name`);

# Insert super user.
INSERT INTO `account` (`buaa_id`, `name`, `password`, `gender`, `school`, `teacher`, `ta`, `avatar`)
VALUES ('java', 'Java', 'password', 0, 'Oracle', true, true, 'default.svg');


############################################################
#                          Course                          #
############################################################
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`
(
    `id`         int          NOT NULL AUTO_INCREMENT,
    `name`       varchar(255) NOT NULL,
    `code`       varchar(15)  NOT NULL,
    `semester`   varchar(15)  NOT NULL,
    `active`     bool         NOT NULL DEFAULT true,
    `created_at` timestamp    NOT NULL,
    `updated_at` timestamp    NOT NULL,
    PRIMARY KEY (`id`)
);

# Insert root course to ensure at least one course exists.
INSERT INTO `course` (`name`, `code`, `semester`, `active`, `created_at`, `updated_at`)
VALUES ('Object-oriented Programming (Java)', 'root', '2023 Autumn', true, NOW(), NOW());
