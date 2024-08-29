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
    `gender`     int         NOT NULL DEFAULT 0,
    `school`     varchar(63) NOT NULL,
    `teacher`    bool        NOT NULL DEFAULT false,
    `ta`         bool        NOT NULL DEFAULT false,
    `avatar`     varchar(63) NOT NULL,
    `created_at` timestamp   NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `account_buaa_id_index` ON `account` (`buaa_id`);
CREATE INDEX `account_name_index` ON `account` (`name`);

# Insert root user.
INSERT INTO `account` (`buaa_id`, `name`, `password`, `gender`, `school`, `teacher`, `ta`, `avatar`, `created_at`)
VALUES ('java', 'Java', 'password', 0, 'Oracle', true, true, 'default.svg', NOW());


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
VALUES ('Root', 'root', '1952', FALSE, NOW(), NOW());


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
    `repeat`     bool        NOT NULL DEFAULT FALSE,
    `created_at` timestamp   NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `student_account_id_course_id_index` ON `student` (`account_id`, `course_id`);
CREATE INDEX `student_course_id_index` ON `student` (`course_id`);
CREATE INDEX `student_teacher_id_index` ON `student` (`teacher_id`);


############################################################
#                       Announcement                       #
############################################################
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`
(
    `id`         int          NOT NULL AUTO_INCREMENT,
    `course_id`  int          NOT NULL,
    `account_id` int          NOT NULL,
    `title`      varchar(255) NOT NULL,
    `content`    text         NOT NULL,
    `topped`     bool         NOT NULL DEFAULT FALSE,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `announcement_course_id_index` ON `announcement` (`course_id`);


############################################################
#                          Task                            #
############################################################
DROP TABLE IF EXISTS task;
CREATE TABLE task
(
    `id`            int          NOT NULL AUTO_INCREMENT,
    `course_id`     int          NOT NULL,
    `type`          smallint     NOT NULL,
    `title`         varchar(255) NOT NULL,
    `content`       text         NOT NULL,
    `visible`       bool         NOT NULL DEFAULT FALSE,
    `start_time`    datetime     NOT NULL,
    `deadline_time` datetime     NOT NULL,
    `end_time`      datetime     NOT NULL,
    `created_at`    datetime     NOT NULL,
    `updated_at`    datetime     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `task_course_id_index` ON `task` (`course_id`);


############################################################
#                      Course Material                     #
############################################################
DROP TABLE IF EXISTS `course_material`;
CREATE TABLE `course_material`
(
    `id`         int          NOT NULL AUTO_INCREMENT,
    `course_id`  int          NOT NULL,
    `filename`   varchar(255) NOT NULL,
    `comment`    varchar(255) NULL,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    PRIMARY KEY (`id`)
);


############################################################
#                      Course Tutorial                     #
############################################################
DROP TABLE IF EXISTS `course_tutorial`;
CREATE TABLE `course_tutorial`
(
    `course_id`  int           NOT NULL,
    `url`        varchar(1023) NOT NULL,
    `created_at` datetime      NOT NULL,
    `updated_at` datetime      NOT NULL,
    PRIMARY KEY (`course_id`)
);


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
#                    Task Problem                          #
############################################################
DROP TABLE IF EXISTS `task_problem`;
CREATE TABLE `task_problem`
(
    `task_id`    int NOT NULL,
    `problem_id` int NOT NULL,
    `order`      int NOT NULL,
    PRIMARY KEY (`task_id`, `problem_id`)
);


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

CREATE INDEX `submission_problem_id_account_id_index` ON `submission` (`problem_id`, `account_id`);
CREATE INDEX `submission_course_id_index` ON `submission` (`course_id`);
CREATE INDEX `submission_account_id_index` ON `submission` (`account_id`);


############################################################
#                    Problem Score                         #
############################################################
DROP TABLE IF EXISTS problem_score;
CREATE TABLE problem_score
(
    `problem_id` int      NOT NULL,
    `account_id` int      NOT NULL,
    `score`      int      NOT NULL,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`problem_id`, `account_id`)
);


############################################################
#                       Discussion                         #
############################################################
DROP TABLE IF EXISTS `discussion`;
CREATE TABLE `discussion`
(
    `id`         int          NOT NULL AUTO_INCREMENT,
    `type`       int          NOT NULL,
    `course_id`  int          NOT NULL,
    `author_id`  int          NOT NULL,
    `title`      varchar(255) NOT NULL,
    `content`    text         NOT NULL,
    `topped`     bool         NOT NULL DEFAULT FALSE,
    `starred`    bool         NOT NULL DEFAULT FALSE,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `discussion_course_id_topped_index` ON `discussion` (`course_id`, `topped`);

DROP TABLE IF EXISTS `like_discussion`;
CREATE TABLE `like_discussion`
(
    `account_id`    int NOT NULL,
    `discussion_id` int NOT NULL,
    PRIMARY KEY (`discussion_id`, `account_id`)
);

############################################################
#                        Reply                             #
############################################################
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`
(
    `id`            int      NOT NULL AUTO_INCREMENT,
    `discussion_id` int      NOT NULL,
    `parent_id`     int      NOT NULL,
    `to_id`         int      NOT NULL,
    `author_id`     int      NOT NULL,
    `content`       text     NOT NULL,
    `verified`      bool     NOT NULL DEFAULT FALSE,
    `created_at`    datetime NOT NULL,
    `updated_at`    datetime NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `reply_discussion_id_index` ON `reply` (`discussion_id`);
CREATE INDEX `reply_parent_id_index` ON `reply` (`parent_id`);

DROP TABLE IF EXISTS `like_reply`;
CREATE TABLE `like_reply`
(
    `account_id` int NOT NULL,
    `reply_id`   int NOT NULL,
    PRIMARY KEY (`reply_id`, `account_id`)
);


############################################################
#                      Subscription                        #
############################################################
DROP TABLE IF EXISTS `subscription`;
CREATE TABLE `subscription`
(
    `account_id`    int         NOT NULL,
    `discussion_id` int         NOT NULL,
    `buaa_id`       varchar(10) NOT NULL,
    PRIMARY KEY (`account_id`, `discussion_id`)
);


############################################################
#                     Task Score                           #
############################################################
# Actually a (`task_id`, `student_id`) can be the primary key.
# But that case it won't be able to record T.A.'s score.
# If `student_id` is 0, it means the score is for T.A.
DROP TABLE IF EXISTS `task_score`;
CREATE TABLE `task_score`
(
    `task_id`    int          NOT NULL,
    `course_id`  int          NOT NULL,
    `account_id` int          NOT NULL,
    `student_id` int          NOT NULL,
    `score`      int          NOT NULL,
    `late`       bool         NOT NULL,
    `record`     varchar(255) NULL,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,

    PRIMARY KEY (`task_id`, `course_id`, `account_id`)
);

CREATE INDEX `task_score_task_id_student_id_index` ON `task_score` (`task_id`, `student_id`);


############################################################
#                    Group Config                          #
############################################################
DROP TABLE IF EXISTS `group_config`;
CREATE TABLE `group_config`
(
    `course_id`  int  NOT NULL,
    `max_size`   int  NOT NULL,
    `min_weight` int  NOT NULL,
    `max_weight` int  NOT NULL,
    `enabled`    bool NOT NULL,
    PRIMARY KEY (`course_id`)
);


############################################################
#                        Group                             #
############################################################
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group`
(
    `id`          int          NOT NULL AUTO_INCREMENT,
    `course_id`   int          NOT NULL,
    `name`        varchar(63)  NOT NULL,
    `description` varchar(255) NOT NULL,
    `locked`      bool         NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `group_course_id_index` ON `group` (`course_id`);


############################################################
#                    Group Member                          #
############################################################
DROP TABLE IF EXISTS `group_member`;
CREATE TABLE `group_member`
(
    `course_id`  int  NOT NULL,
    `account_id` int  NOT NULL,
    `group_id`   int  NOT NULL,
    `owner`      bool NOT NULL,
    `weight`     int  NOT NULL DEFAULT 100,
    PRIMARY KEY (`course_id`, `account_id`)
);

CREATE INDEX `group_member_group_id_index` ON `group_member` (`group_id`);


############################################################\
#                    Group Assignment                      #
############################################################
DROP TABLE IF EXISTS `group_assignment`;
CREATE TABLE `group_assignment`
(
    `course_id`  int          NOT NULL,
    `comment`    varchar(255) NOT NULL,
    `visible`    bool         NOT NULL,
    `start_time` datetime     NOT NULL,
    `end_time`   datetime     NOT NULL,
    PRIMARY KEY (`course_id`)
);


############################################################
#                    Group Score                           #
############################################################
DROP TABLE IF EXISTS `group_score`;
CREATE TABLE `group_score`
(
    `group_id`   int          NOT NULL,
    `course_id`  int          NOT NULL,
    `score`      int          NOT NULL,
    `record`     varchar(255) NULL,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    PRIMARY KEY (`group_id`)
);


############################################################
#                        Message                           #
############################################################
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`
(
    `id`         int      NOT NULL AUTO_INCREMENT,
    `type`       int      NOT NULL,
    `course_id`  int      NOT NULL,
    `account_id` int      NOT NULL,
    `content`    json     NOT NULL,
    `argument`   json     NULL     DEFAULT NULL,
    `read`       bool     NOT NULL DEFAULT FALSE,
    `created_at` datetime NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `message_course_id_account_id_index` ON `message` (`course_id`, `account_id`);


############################################################
#                       Score Config                       #
############################################################
DROP TABLE IF EXISTS `score_config`;
CREATE TABLE `score_config`
(
    `course_id`  int NOT NULL,
    `lab_score`  int NOT NULL,
    `iter_score` int NOT NULL,
    `proj_score` int NOT NULL,
    PRIMARY KEY (`course_id`)
);