/*
学生管理系统数据库脚本
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- 班级管理系统相关表结构
-- ----------------------------
-- ----------------------------
-- Table structure for `class_info`
-- ----------------------------
DROP TABLE IF EXISTS `class_info`;
CREATE TABLE `class_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `grade` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `student_count` int(11) DEFAULT 0,
  `head_teacher` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `classroom` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `enrollment_year` int(4) DEFAULT NULL,
  `remark` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for `student`
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `student_no` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `sex` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `birthday` date DEFAULT NULL,
  `class_id` int(11) NOT NULL,
  `phone` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `entry_date` date DEFAULT NULL,
  `status` varchar(20) COLLATE utf8_unicode_ci DEFAULT '在读',
  PRIMARY KEY (`id`),
  KEY `class_id` (`class_id`),
  CONSTRAINT `class_id_fk` FOREIGN KEY (`class_id`) REFERENCES `class_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for `course`
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `teacher_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `credit` int(11) DEFAULT NULL,
  `total_hours` int(11) DEFAULT NULL,
  `course_type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `suitable_grade` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for `score`
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `score` decimal(5,1) DEFAULT NULL,
  `semester` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `exam_date` date DEFAULT NULL,
  `exam_type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `remark` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `student_id_fk` (`student_id`),
  KEY `course_id_fk` (`course_id`),
  CONSTRAINT `student_id_fk` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `course_id_fk` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for `ausertable`
-- ----------------------------
DROP TABLE IF EXISTS `ausertable`;
CREATE TABLE `ausertable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aname` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `apwd` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- 初始化学生管理系统数据
-- ----------------------------
INSERT INTO `class_info` VALUES (1, '一年级一班', '一年级', 45, '张老师', '1号楼101', 2023, '优秀班级');
INSERT INTO `class_info` VALUES (2, '一年级二班', '一年级', 42, '李老师', '1号楼102', 2023, '良好班级');
INSERT INTO `class_info` VALUES (3, '二年级一班', '二年级', 40, '王老师', '2号楼201', 2022, '重点班级');

INSERT INTO `course` VALUES (1, '语文', '张老师', 3, 48, '必修课', '一年级', '基础语文课程');
INSERT INTO `course` VALUES (2, '数学', '李老师', 3, 48, '必修课', '一年级', '基础数学课程');
INSERT INTO `course` VALUES (3, '英语', '王老师', 3, 48, '必修课', '一年级', '基础英语课程');
INSERT INTO `course` VALUES (4, '科学', '赵老师', 2, 32, '必修课', '一年级', '基础科学课程');

-- 管理员账号
INSERT INTO `ausertable` VALUES ('1', 'admin', 'admin');