/*
Navicat MySQL Data Transfer

Source Server         : MyCon
Source Server Version : 50519
Source Host           : localhost:3306
Source Database       : personmis

Target Server Type    : MYSQL
Target Server Version : 50519
File Encoding         : 65001

Date: 2021-01-28 06:00:36
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
-- 初始化班级管理系统数据
-- ----------------------------
INSERT INTO `class_info` VALUES (1, '一年级一班', '一年级', 45, '张老师', '1号楼101', 2023, '优秀班级');
INSERT INTO `class_info` VALUES (2, '一年级二班', '一年级', 42, '李老师', '1号楼102', 2023, '良好班级');
INSERT INTO `class_info` VALUES (3, '二年级一班', '二年级', 40, '王老师', '2号楼201', 2022, '重点班级');

INSERT INTO `course` VALUES (1, '语文', '张老师', 3, 48, '必修课', '一年级', '基础语文课程');
INSERT INTO `course` VALUES (2, '数学', '李老师', 3, 48, '必修课', '一年级', '基础数学课程');
INSERT INTO `course` VALUES (3, '英语', '王老师', 3, 48, '必修课', '一年级', '基础英语课程');
INSERT INTO `course` VALUES (4, '科学', '赵老师', 2, 32, '必修课', '一年级', '基础科学课程');

-- 人事管理系统原有表结构
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
-- Records of ausertable
-- ----------------------------
-- 保持原有的管理员账号
INSERT INTO `ausertable` VALUES ('1', 'admin', 'admin');

-- ----------------------------
-- Table structure for `department`
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dtype` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dtel` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dfax` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `supdepartment` int(11) DEFAULT NULL,
  `establishmentdate` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of department
-- ----------------------------

-- ----------------------------
-- Table structure for `post`
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pname` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `ptype` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `organization` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of post
-- ----------------------------

-- ----------------------------
-- Table structure for `quit`
-- ----------------------------
DROP TABLE IF EXISTS `quit`;
CREATE TABLE `quit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `staff_id` int(11) DEFAULT NULL,
  `sname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `qtype` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `qdate` date DEFAULT NULL,
  `opdate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `qsid` (`staff_id`),
  CONSTRAINT `qsid` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of quit
-- ----------------------------

-- ----------------------------
-- Table structure for `staff`
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sname` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `sex` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `birthday` date NOT NULL,
  `sid` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `depart_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `entrydate` date NOT NULL,
  `joinworkdate` date NOT NULL,
  `workform` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `staffsource` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `politicalstatus` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nation` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nativeplace` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `stel` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `semail` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sheight` decimal(12,2) DEFAULT NULL,
  `bloodtype` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `maritalstatus` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `registeredresidence` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `education` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `degree` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `university` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `major` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `graduationdate` date DEFAULT NULL,
  `startdate` date DEFAULT NULL,
  `enddate` date DEFAULT NULL,
  `status` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `peroidopdate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `did` (`depart_id`),
  KEY `pid` (`post_id`),
  CONSTRAINT `did` FOREIGN KEY (`depart_id`) REFERENCES `department` (`id`),
  CONSTRAINT `pid` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of staff
-- ----------------------------

-- ----------------------------
-- Table structure for `transfer`
-- ----------------------------
DROP TABLE IF EXISTS `transfer`;
CREATE TABLE `transfer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `staff_id` int(11) DEFAULT NULL,
  `sname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `beforepost_id` int(11) DEFAULT NULL,
  `afterpost_id` int(11) DEFAULT NULL,
  `ttype` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tdate` date DEFAULT NULL,
  `opdate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ssid` (`staff_id`),
  KEY `ppid` (`afterpost_id`),
  KEY `bpid` (`beforepost_id`),
  CONSTRAINT `bpid` FOREIGN KEY (`beforepost_id`) REFERENCES `post` (`id`),
  CONSTRAINT `ppid` FOREIGN KEY (`afterpost_id`) REFERENCES `post` (`id`),
  CONSTRAINT `ssid` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of transfer
-- ----------------------------
