SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------------------------
-- Table structure for OKR management system
-- ----------------------------------------------

-- ------------------------------------
-- Table structure for okr sector
-- ------------------------------------
DROP TABLE IF EXISTS `t_okr_sector`;
CREATE TABLE `t_okr_sector`
(
  `id`                 CHAR(32)            NOT NULL,
  `sector_name`        VARCHAR(32)         NOT NULL,
  `sector_year`        INT(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '归属年份',
  `start_date`         DATE                NOT NULL,
  `finish_date`        DATE                NOT NULL,
  `sector_status`      INT(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '0:未开始 1:生效 2:完成 3.删除',
  `sector_type`        INT(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '0:年度 1:时间段',
  `is_deleted`         TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `version`            INT(10) UNSIGNED    NOT NULL DEFAULT 0,
  `created_by`         CHAR(32)            NULL     DEFAULT NULL,
  `created_date`       DATETIME            NULL     DEFAULT NULL,
  `last_modified_by`   CHAR(32)            NULL     DEFAULT NULL,
  `last_modified_date` DATETIME            NULL     DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_t_okr_sector_sector_name_sector_year` (`sector_name`, `sector_year`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

-- ------------------------------------
-- Table structure for okr list
-- ------------------------------------
DROP TABLE IF EXISTS `t_okr_okr`;
CREATE TABLE `t_okr_okr`
(
  `id`                 CHAR(32)            NOT NULL,
  `sector_id`          CHAR(32)            NOT NULL,
  `obj_id`             CHAR(32)            NULL,
  `okr_type`           INT(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '0:个人 1:部门 3: 公司',
  `status`             INT(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '0:待生效 1:生效 2:废弃 3:临时回顾',
  `okr_version`        INT(10) UNSIGNED    NOT NULL DEFAULT 0,
  `effective`          TINYINT(1) UNSIGNED NOT NULL DEFAULT 1,
  `is_deleted`         TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `version`            INT(10) UNSIGNED    NOT NULL DEFAULT 0,
  `created_by`         CHAR(32)            NULL     DEFAULT NULL,
  `created_date`       DATETIME            NULL     DEFAULT NULL,
  `last_modified_by`   CHAR(32)            NULL     DEFAULT NULL,
  `last_modified_date` DATETIME            NULL     DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_t_okr_okr_sector_id` FOREIGN KEY (`sector_id`) REFERENCES `t_okr_sector` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

-- ------------------------------------
-- Table structure for okr obj type
-- ------------------------------------
DROP TABLE IF EXISTS `t_okr_obj_type`;
CREATE TABLE `t_okr_obj_type`
(
  `id`                 CHAR(32)            NOT NULL,
  `code`               VARCHAR(32)         NOT NULL,
  `name`               VARCHAR(32)         NOT NULL,
  `sequence`           INT(10) UNSIGNED    NOT NULL DEFAULT 0,
  `score_min`          DECIMAL(16, 2)      NOT NULL DEFAULT '0.00',
  `score_max`          DECIMAL(16, 2)      NOT NULL DEFAULT '100.00',
  `score_step`         DECIMAL(16, 2)      NOT NULL DEFAULT '1.00',
  `score_seca_max`     DECIMAL(16, 2)      NULL     DEFAULT NULL,
  `score_secb_max`     DECIMAL(16, 2)      NULL     DEFAULT NULL,
  `score_secc_max`     DECIMAL(16, 2)      NULL     DEFAULT NULL,
  `score_secd_max`     DECIMAL(16, 2)      NULL     DEFAULT NULL,
  `score_seca_color`   INT(10) UNSIGNED    NULL     DEFAULT NULL,
  `score_secb_color`   INT(10) UNSIGNED    NULL     DEFAULT NULL,
  `score_secc_color`   INT(10) UNSIGNED    NULL     DEFAULT NULL,
  `score_secd_color`   INT(10) UNSIGNED    NULL     DEFAULT NULL,
  `effective`          TINYINT(1) UNSIGNED NOT NULL DEFAULT 1,
  `is_deleted`         TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `version`            INT(10) UNSIGNED    NOT NULL DEFAULT 0,
  `created_by`         CHAR(32)            NULL     DEFAULT NULL,
  `created_date`       DATETIME            NULL     DEFAULT NULL,
  `last_modified_by`   CHAR(32)            NULL     DEFAULT NULL,
  `last_modified_date` DATETIME            NULL     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

-- ------------------------------------
-- Table structure for okr node
-- ------------------------------------
DROP TABLE IF EXISTS `t_okr_node`;
CREATE TABLE `t_okr_node`
(
  `id`                 CHAR(32)            NOT NULL,
  `okr_id`             CHAR(32)            NOT NULL,
  `sequence`           INT(10) UNSIGNED    NOT NULL DEFAULT 0,
  `node_type`          INT(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '0:type 1:obj 2:kr',
  `obj_type_id`        CHAR(32)            NULL,
  `parent_node_id`     CHAR(32)            NULL,
  `description`        VARCHAR(256)        NULL,
  `self_score`         DECIMAL(16, 2)      NULL     DEFAULT NULL,
  `self_desc`          VARCHAR(256)        NULL     DEFAULT NULL,
  `dept_score`         DECIMAL(16, 2)      NULL     DEFAULT NULL,
  `dept_desc`          VARCHAR(256)        NULL     DEFAULT NULL,
  `conf_index`         DECIMAL(16, 2)      NULL     DEFAULT NULL,
  `is_deleted`         TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `version`            INT(10) UNSIGNED    NOT NULL DEFAULT 0,
  `created_by`         CHAR(32)            NULL     DEFAULT NULL,
  `created_date`       DATETIME            NULL     DEFAULT NULL,
  `last_modified_by`   CHAR(32)            NULL     DEFAULT NULL,
  `last_modified_date` DATETIME            NULL     DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_t_okr_node_okr_id` FOREIGN KEY (`okr_id`) REFERENCES `t_okr_okr` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- ------------------------------------
-- Table structure for okr review
-- ------------------------------------
DROP TABLE IF EXISTS `t_okr_review`;
CREATE TABLE `t_okr_review`
(
  `id`                 CHAR(32)            NOT NULL,
  `user_id`            CHAR(32)            NOT NULL,
  `sequence`           INT(10) UNSIGNED    NULL     DEFAULT 0,
  `type`               INT(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '0:this_week 1:future 2:status',
  `status`             INT(10) UNSIGNED    NULL,
  `priority`           VARCHAR(32)         NULL,
  `description`        VARCHAR(256)        NULL,
  `is_deleted`         TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  `version`            INT(10) UNSIGNED    NOT NULL DEFAULT 0,
  `created_by`         CHAR(32)            NULL     DEFAULT NULL,
  `created_date`       DATETIME            NULL     DEFAULT NULL,
  `last_modified_by`   CHAR(32)            NULL     DEFAULT NULL,
  `last_modified_date` DATETIME            NULL     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

-- ---------------------------------
-- lov
-- ---------------------------------
DROP TABLE IF EXISTS `t_okr_lov`;
CREATE TABLE `t_okr_lov`
(
  `id`                 CHAR(32)                        NOT NULL,
  `type`               VARCHAR(32)                     NOT NULL,
  `parent_code`        VARCHAR(32)                     NOT NULL,
  `code`               VARCHAR(32)                     NOT NULL,
  `name`               VARCHAR(128)                    NOT NULL,
  `sequence`           INT UNSIGNED        DEFAULT '0' NOT NULL,
  `is_deleted`         TINYINT(1) UNSIGNED DEFAULT '0' NOT NULL,
  `version`            INT UNSIGNED        DEFAULT '0' NOT NULL,
  `created_by`         CHAR(32)                        NULL,
  `created_date`       DATETIME                        NULL,
  `last_modified_by`   CHAR(32)                        NULL,
  `last_modified_date` DATETIME                        NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_t_okr_lov_type` (`type`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

SET FOREIGN_KEY_CHECKS = 1;