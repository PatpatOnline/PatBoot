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


############################################################
#                         Student                          #
############################################################
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`
(
    `id`         int         NOT NULL AUTO_INCREMENT,
    `account_id` int         NOT NULL,
    `course_id`  int         NOT NULL,
    `teacher_id` int         NOT NULL,
    `major`      varchar(63) NOT NULL,
    `class_name` varchar(15) NOT NULL,
    `repeat`     bool        NOT NULL DEFAULT false,
    `created_at` timestamp   NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `student_account_id_index` ON `student` (`account_id`);
CREATE INDEX `student_course_id_index` ON `student` (`course_id`);


############################################################
#                        Problem                           #
############################################################
DROP TABLE IF EXISTS `problem`;
CREATE TABLE `problem`
(
    `id`          int          NOT NULL AUTO_INCREMENT,
    `title`       varchar(255) NOT NULL,
    `description` text         NOT NULL,
    `hidden`      bool         NOT NULL,
    `data`        json         NULL DEFAULT NULL,
    `created_at`  datetime     NOT NULL,
    `updated_at`  datetime     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `problem_title_index` ON `problem` (`title`);


############################################################
#                        Submission                        #
############################################################
DROP TABLE IF EXISTS `submission`;
CREATE TABLE `submission`
(
    `id`          int         NOT NULL AUTO_INCREMENT,
    `account_id`  int         NOT NULL,
    `buaa_id`     varchar(10) NOT NULL,
    `course_id`   int         NOT NULL,
    `problem_id`  int         NOT NULL,
    `language`    varchar(7)  NOT NULL,

    `submit_time` datetime    NOT NULL,
    `start_time`  datetime    NULL     DEFAULT NULL,
    `end_time`    datetime    NULL     DEFAULT NULL,

    `score`       int         NOT NULL DEFAULT 0,
    `data`        json        NULL     DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `submission_account_id_index` ON `submission` (`account_id`);
CREATE INDEX `submission_buaa_id_index` ON `submission` (`buaa_id`);
CREATE INDEX `submission_course_id_index` ON `submission` (`course_id`);
CREATE INDEX `submission_problem_id_index` ON `submission` (`problem_id`);


############################################################
#                         Score                            #
############################################################
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score`
(
    `problem_id` int      NOT NULL,
    `account_id` int      NOT NULL,
    `score`      int      NOT NULL,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`problem_id`, `account_id`)
);

