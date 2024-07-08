############################################################
#                         Account                          #
############################################################
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`
(
    `id`         int         NOT NULL AUTO_INCREMENT,
    `buaa_id`    varchar(10) NOT NULL UNIQUE,
    `name`       varchar(31) NOT NULL,
    `password`   varchar(31) NOT NULL,
    `gender`     int         NOT NULL default 0,
    `school`     varchar(63) NOT NULL,
    `is_teacher` bool        NOT NULL default false,
    `is_ta`      bool        NOT NULL default false,
    `avatar`     varchar(63) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `account_buaa_id_index` ON `account` (`buaa_id`);
CREATE INDEX `account_name_index` ON `account` (`name`);

# Insert super user.
INSERT INTO `account` (`buaa_id`, `name`, `password`, `gender`, `school`, `is_teacher`, `is_ta`, `avatar`)
VALUES ('java', 'Java', 'password', 0, 'Oracle', true, true, 'default.svg');
