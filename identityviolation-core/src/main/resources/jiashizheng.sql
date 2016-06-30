/*
Navicat MySQL Data Transfer

Source Server         : jiashizheng
Source Server Version : 50530
Source Host           : 192.168.0.176:3306
Source Database       : jiashizheng

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2016-01-04 16:18:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for city_list
-- ----------------------------
DROP TABLE IF EXISTS `city_list`;
CREATE TABLE `city_list` (
  `province_pinyin` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '省份拼音简称',
  `province` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '省份名称',
  `pro` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '省份名称简写',
  `city_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '城市编码',
  `city_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `city_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '城市开关',
  `is_yzm` int(3) DEFAULT '0' COMMENT '是否需要图片验证码',
  `update_time` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '城市列表更新时间',
  `is_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '是否需要驾驶人姓名',
  `is_license` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '是否需要驾驶证号',
  `is_licensedate` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '是否需要初次领证日期',
  `is_archive` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '是否需要驾驶证档案编号',
  `is_effective` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截至日期',
  PRIMARY KEY (`city_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of city_list
-- ----------------------------
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'qitaihe', '七台河', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'wanning', '万宁', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'sanya', '三亚', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'sanming', '三明', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'sanmenxia', '三门峡', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SH', '上海', '沪', 'shanghai', '上海', '1', '0', '2015-03-30 13:39:37', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'shangyu', '上虞', '0', '0', '2015-07-22 16:58:51', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'dongfang', '东方', '0', '0', '2015-08-04 16:48:53', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'dongguan', '东莞', '1', '0', '2015-07-22 12:03:33', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'dongying', '东营', '0', '0', '2014-10-17 08:09:03', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NX', '宁夏', '宁', 'zhongwei', '中卫', '0', '0', '2015-08-02 06:29:42', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'zhongshan', '中山', '1', '0', '2015-06-28 23:33:49', '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'linxia', '临夏', '1', '0', '2015-06-17 16:38:42', '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'linfen', '临汾', '1', '0', '2015-08-20 10:03:26', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'linyi', '临沂', '0', '0', '2014-09-25 20:28:39', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'lincang', '临沧', '0', '0', '2014-09-02 17:36:20', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'linhai', '临海', '0', '0', '2015-08-03 17:34:41', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'lingaoxian', '临高县', '0', '0', '2015-09-11 15:24:50', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'dandong', '丹东', '1', '0', '2015-06-25 10:37:28', '1', '1', '0', '0', '1', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'danyang', '丹阳', '0', '0', '2015-07-10 14:56:18', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'lishui', '丽水', '0', '0', '2015-03-30 14:52:11', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'lijiang', '丽江', '0', '0', '2015-07-20 17:05:32', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'yiwu', '义乌', '0', '0', '2015-05-13 15:58:53', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'wulanchabu', '乌兰察布', '0', '0', '2015-08-03 16:41:28', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'wuhai', '乌海', '0', '0', '2014-09-18 06:37:26', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'wulumuqi', '乌鲁木齐', '0', '0', '2015-01-20 10:15:25', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'ledong', '乐东', '0', '0', '2015-09-11 15:33:26', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'leqing', '乐清', '0', '0', '2015-08-03 14:57:40', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'rushan', '乳山', '0', '0', '2015-09-12 11:41:10', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'yunfu', '云浮', '1', '0', '2015-07-07 14:49:35', '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'wujiaqu', '五家渠', '0', '0', '2014-08-04 03:10:58', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'wuzhishan', '五指山', '0', '0', '2015-03-26 11:05:51', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'bozhou', '亳州', '0', '0', '2015-04-09 11:28:57', '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'xiantao', '仙桃', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'renqiu', '任丘', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'yichun_h', '伊春', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'yili', '伊犁', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'yuyao', '余姚', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'foshan', '佛山', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'jiamusi', '佳木斯', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'baoting', '保亭', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'baoding', '保定', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'baoshan', '保山', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'xinyang', '信阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'danzhou', '儋州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'kezilesu', '克孜勒苏', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'kelamayi', '克拉玛依', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'yanzhou', '兖州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'liuan', '六安', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GZ', '贵州', '贵', 'liupanshui', '六盘水', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'lanzhou', '兰州', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'xingan', '兴安', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'zhungeer', '准格尔', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'baotou', '包头', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('BJ', '北京', '京', 'beijing', '北京', '1', '0', null, '1', '1', '1', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'shiyan', '十堰', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'nanjing', '南京', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'nanan', '南安', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'nanping', '南平', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NC', '江西', '赣', 'nanchang', '南昌', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'nantong', '南通', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'nanyang', '南阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'boertala', '博尔塔拉', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'shamen', '厦门', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SC', '四川', '川', 'shuangliuxian', '双流县', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'shuangyashan', '双鸭山', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'tai1zhou', '台州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'hefei', '合肥', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JL', '吉林', '吉', 'jilin', '吉林', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'tulufan', '吐鲁番', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'lvliang', '吕梁', '1', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'qidong', '启东', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NX', '宁夏', '宁', 'wuzhong', '吴忠', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'zhoukou', '周口', '1', '0', null, '1', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'hulunbeier', '呼伦贝尔', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'huhehaote', '呼和浩特', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'hetian', '和田', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'xianning', '咸宁', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SX', '陕西', '陕', 'xianyang', '咸阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'hami', '哈密', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'haerbin', '哈尔滨', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'tangshan', '唐山', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'shangqiu', '商丘', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'kashi', '喀什', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'jiaxing', '嘉兴', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'jiayuguan', '嘉峪关', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('JL', '吉林', '吉', 'siping', '四平', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NX', '宁夏', '宁', 'guyuan', '固原', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'tumushuke', '图木舒克', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'tacheng', '塔城', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'zengcheng', '增城', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'daxinganling', '大兴安岭', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'datong', '大同', '1', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'daqing', '大庆', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'dali', '大理', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'dalian', '大连', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'tianshui', '天水', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('TJ', '天津', '津', 'tianjin', '天津', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'tianmen', '天门', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'taicang', '太仓', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'taiyuan', '太原', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'weihai', '威海', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'xiaogan', '孝感', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'ningde', '宁德', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'ningbo', '宁波', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'anqing', '安庆', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SX', '陕西', '陕', 'ankang', '安康', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'anyang', '安阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GZ', '贵州', '贵', 'anshun', '安顺', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'dinganxian', '定安县', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'dingxi', '定西', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'yixing', '宜兴', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'yichang', '宜昌', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SX', '陕西', '陕', 'baoji', '宝鸡', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'xuancheng', '宣城', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'su4zhou', '宿州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'suqian', '宿迁', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'shouguang', '寿光', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'tunchangxian', '屯昌县', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'chaohu', '巢湖', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'bayannaoer', '巴彦淖尔', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'bayinguoleng', '巴音郭楞', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'changzhou', '常州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'changshu', '常熟', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'pingliang', '平凉', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'pingdu', '平度', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'pinghu', '平湖', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'pingdingshan', '平顶山', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'guangzhou', '广州', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'qingyang', '庆阳', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'langfang', '廊坊', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SX', '陕西', '陕', 'yanan', '延安', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JL', '吉林', '吉', 'yanbian', '延边', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'kaifeng', '开封', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'zhangjiakou', '张家口', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'zhangjiagang', '张家港', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'zhangye', '张掖', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'xuzhou', '徐州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'dehong', '德宏', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'dezhou', '德州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'xinzhou', '忻州', '1', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'nujiang', '怒江', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'enshi', '恩施', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'huianxian', '惠安县', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'huizhou', '惠州', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'cixi', '慈溪', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SC', '四川', '川', 'chengdu', '成都', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'yangzhou', '扬州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'chengde', '承德', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'fushun', '抚顺', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XZ', '西藏', '藏', 'lasa', '拉萨', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'zhaoyuan', '招远', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'jieyang', '揭阳', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'wenshan', '文山', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'wenchang', '文昌', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'wendeng', '文登', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'xinxiang', '新乡', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'xintai', '新泰', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'wuxi', '无锡', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'rizhao', '日照', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'kunshan', '昆山', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'kunming', '昆明', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'changji', '昌吉', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'changjiang', '昌江', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'zhaotong', '昭通', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'jinzhong', '晋中', '1', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'jincheng', '晋城', '1', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'jinjiang', '晋江', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'puer', '普洱', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'qujing', '曲靖', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'shuozhou', '朔州', '1', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'zhaoyang', '朝阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '宁', 'benxi', '本溪', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'hangzhou', '杭州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JL', '吉林', '吉', 'songyuan', '松原', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('QH', '青海', '青', 'guoluoz', '果洛州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'zaozhuang', '枣庄', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUN', '湖南', '湘', 'zhuzhou', '株洲', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'tongxiang', '桐乡', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'meizhou', '梅州', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'chuxiong', '楚雄', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'wuwei', '武威', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'wuhan', '武汉', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GZ', '贵州', '贵', 'bijie', '毕节', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'yongkang', '永康', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SX', '陕西', '陕', 'hanzhong', '汉中', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'shantou', '汕头', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'shanwei', '汕尾', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'jiangdou', '江都', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'jiangmen', '江门', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'jiangyin', '江阴', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'chizhou', '池州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'shenyang', '沈阳', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'cangzhou', '沧州', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'heyuan', '河源', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'quanzhou', '泉州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'taian', '泰安', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'luoyang', '洛阳', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'jinan', '济南', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'jining', '济宁', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'jiyuan', '济源', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('QH', '青海', '青', 'haidong', '海东', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('QH', '青海', '青', 'haibei', '海北', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('QH', '青海', '青', 'hainanz', '海南州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'haikou', '海口', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'haining', '海宁', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('QH', '青海', '青', 'haixiz', '海西州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'haimen', '海门', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'zibo', '淄博', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'huaibei', '淮北', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'huainan', '淮南', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'shenzhen', '深圳', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'qingyuan', '清远', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'wenling', '温岭', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'wenzhou', '温州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SX', '陕西', '陕', 'weinan', '渭南', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'huzhou', '湖州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'zhanjiang', '湛江', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'liyang', '溧阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'chuzhou', '滁州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'tengzhou', '滕州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'binzhou', '滨州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'zhangzhou', '漳州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'weifang', '潍坊', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'qianjiang', '潜江', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'chaozhou', '潮州', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'chengmaixian', '澄迈县', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'puyang', '濮阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'yantai', '烟台', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'jiaozuo', '焦作', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'mudanjiang', '牡丹江', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('QH', '青海', '青', 'yushuz', '玉树州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'yuxi', '玉溪', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'yuhuanxian', '玉环县', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'zhuhai', '珠海', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'qiongzhong', '琼中', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'qionghai', '琼海', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'ruian', '瑞安', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'wafangdian', '瓦房店', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'gannan', '甘南', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('JL', '吉林', '吉', 'baicheng', '白城', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JL', '吉林', '吉', 'baishan', '白山', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'baisha', '白沙', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'baiyin', '白银', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HUN', '湖南', '湘', 'yiyang', '益阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'yancheng', '盐城', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'panjin', '盘锦', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NX', '宁夏', '宁', 'shizuishan', '石嘴山', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'shijiazhuang', '石家庄', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'shihezi', '石河子', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'shishi', '石狮', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'shennongjia', '神农架', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'fu1zhou', '福州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'fuqing', '福清', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'qinhuangdao', '秦皇岛', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'zhangqiu', '章丘', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'honghe', '红河', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'shaoxing', '绍兴', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'shaoxingxian', '绍兴县', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'suihua', '绥化', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'liaocheng', '聊城', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'zhaoqing', '肇庆', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'feicheng', '肥城', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'jiaozhou', '胶州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'zhoushan', '舟山', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'wuhu', '芜湖', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'su1zhou', '苏州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'maoming', '茂名', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'jingzhou', '荆州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'jingmen', '荆门', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'rongcheng', '荣成', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'futian', '莆田', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'laizhou', '莱州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'laiwu', '莱芜', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'heze', '菏泽', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'yingkou', '营口', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'huludao', '葫芦岛', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'penglai', '蓬莱', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'bengbu', '蚌埠', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'hengshui', '衡水', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'quzhou', '衢州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'xiangfan', '襄樊', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'xishuangbanna', '西双版纳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('QH', '青海', '青', 'xining', '西宁', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SX', '陕西', '陕', 'xian', '西安', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'xuchang', '许昌', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'zhucheng', '诸城', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'zhuji', '诸暨', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GZ', '贵州', '贵', 'guiyang', '贵阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'chifeng', '赤峰', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JL', '吉林', '吉', 'liaoyuan', '辽源', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'liaoyang', '辽阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'qianan', '迁安', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'yuncheng', '运城', '1', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'lianyungang', '连云港', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('YN', '云南', '云', 'diqing', '迪庆', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JL', '吉林', '吉', 'tonghua', '通化', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'tongzhou', '通州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'tongliao', '通辽', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GZ', '贵州', '贵', 'zunyi', '遵义', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'zunhua', '遵化', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'xingtai', '邢台', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('XZ', '西藏', '藏', 'naqu', '那曲', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HB', '河北', '冀', 'handan', '邯郸', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'zoucheng', '邹城', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'zoupingxian', '邹平县', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'zhengzhou', '郑州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'ereduosi', '鄂尔多斯', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'ezhou', '鄂州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'jiuquan', '酒泉', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('CQ', '重庆', '渝', 'chongqing', '重庆', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'jinhua', '金华', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'jinchang', '金昌', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('GZ', '贵州', '贵', 'tongren', '铜仁', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'tongling', '铜陵', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NX', '宁夏', '宁', 'yinchuan', '银川', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'xilinguole', '锡林郭勒', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'jinzhou', '锦州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JS', '江苏', '苏', 'zhenjiang', '镇江', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('ZJ', '浙江', '浙', 'changxingxian', '长兴县', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('JL', '吉林', '吉', 'changchun', '长春', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUN', '湖南', '湘', 'changsha', '长沙', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'changzhi', '长治', '1', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'fuxin', '阜新', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'fuyang', '阜阳', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'yangjiang', '阳江', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SAX', '山西', '晋', 'yangquan', '阳泉', '1', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'akesu', '阿克苏', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'aletai', '阿勒泰', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('NMG', '内蒙古', '蒙', 'alashan', '阿拉善', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('XJ', '新疆', '新', 'alaer', '阿拉尔', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GS', '甘肃', '甘', 'longnan', '陇南', '1', '0', null, '1', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HAN', '海南', '琼', 'lingshui', '陵水', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'suizhou', '随州', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'qingdao', '青岛', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('LN', '辽宁', '辽', 'anshan', '鞍山', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GD', '广东', '粤', 'shaoguan', '韶关', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'maanshan', '马鞍山', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'zhumadian', '驻马店', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'jixi', '鸡西', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HN', '河南', '豫', 'hebi', '鹤壁', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'hegang', '鹤岗', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'huanggang', '黄冈', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('QH', '青海', '青', 'huangnan', '黄南', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('AH', '安徽', '皖', 'huangshan', '黄山', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HUB', '湖北', '鄂', 'huangshi', '黄石', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'heihe', '黑河', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('GZ', '贵州', '贵', 'qiandongnan', '黔东南', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GZ', '贵州', '贵', 'qiannan', '黔南', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('GZ', '贵州', '贵', 'qianxinan', '黔西南', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('HLJ', '黑龙江', '黑', 'qiqihaer', '齐齐哈尔', '1', '0', null, '0', '1', '0', '1', '0', '0');
INSERT INTO `city_list` VALUES ('SD', '山东', '鲁', 'longkou', '龙口', '0', '0', null, '0', '1', '0', '0', '0', '0');
INSERT INTO `city_list` VALUES ('FJ', '福建', '闽', 'longyan', '龙岩', '0', '0', null, '0', '1', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for city_source_config
-- ----------------------------
DROP TABLE IF EXISTS `city_source_config`;
CREATE TABLE `city_source_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `source_class` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '数据源名称',
  `source_rule` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源配置规则',
  `source_index` varchar(3) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源查询顺序，倒序',
  `usable` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源是否可用 1 可用 0 不可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of city_source_config
-- ----------------------------
INSERT INTO `city_source_config` VALUES ('2', '成都', 'chengdu', '搜狐', 'SohuService', 'province=510000&city=510100&carNum=川%regid%&ecarBelong=51&ecarPart=%regid%&ecarType=02&evin=%btmid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('3', '青岛', 'qingdao', '搜狐', 'SohuService', 'province=370000&city=370200&carNum=鲁%regid%&ecarBelong=37&ecarPart=%regid%&ecarType=02&evin=%btmid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('4', '兰州', 'lanzhou', '搜狐', 'SohuService', 'province=620000&city=620100&carNum=甘%regid%&ecarBelong=62&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('5', '厦门', 'shamen', '搜狐', 'SohuService', 'province=350000&city=350200&carNum=闽%regid%&ecarBelong=35&ecarPart=%regid%&ecarType=02&evin=%btmid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('6', '福州', 'fu1zhou', '搜狐', 'SohuService', 'province=350000&city=350100&carNum=闽%regid%&ecarBelong=35&ecarPart=%regid%&ecarType=02&evin=%btmid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('7', '重庆', 'chongqing', '搜狐', 'SohuService', 'province=500000&city=500000&carNum=渝%regid%&ecarBelong=50&ecarPart=%regid%&ecarType=02&evin=%btmid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('8', '长春', 'changchun', '搜狐', 'SohuService', 'province=220000&city=220100&carNum=吉%regid%&ecarBelong=22&ecarPart=%regid%&ecarType=02&evin=%btmid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('9', '长沙', 'changsha', '搜狐', 'SohuService', 'province=430000&city=430100&carNum=湘%regid%&ecarBelong=43&ecarPart=%regid%&ecarType=02&engineNum=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('10', '银川', 'yinchuan', '搜狐', 'SohuService', 'province=640000&city=640100&carNum=宁%regid%&ecarBelong=64&ecarPart=%regid%&ecarType=02&evin=%btmid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('11', '延安', 'yanan', '搜狐', 'SohuService', 'province=610000&city=610600&carNum=陕%regid%&ecarBelong=61&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('12', '安康', 'ankang', '搜狐', 'SohuService', 'province=610000&city=610900&carNum=陕%regid%&ecarBelong=61&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('13', '呼和浩特', 'huhehaote', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('14', '包头', 'baotou', '搜狐', 'SohuService', 'province=150000&city=150200&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('15', '兴安', 'xingan', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('16', '准格尔', 'zhungeer', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('17', '赤峰', 'chifeng', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('18', '锡林郭勒', 'xilinguole', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('19', '阿拉善', 'alashan', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('20', '通辽', 'tongliao', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('21', '鄂尔多斯', 'eerduosi', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('22', '乌兰察布', 'wulanchabu', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('23', '呼伦贝尔', 'hulunbeier', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('24', '巴彦淖尔', 'bayannaoer', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('25', '乌海', 'wuhai', '搜狐', 'SohuService', 'province=150000&city=150100&carNum=蒙%regid%&ecarBelong=15&ecarPart=%regid%&ecarType=02&evin=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('26', '上海', 'shanghai', '搜狐', 'SohuService', 'province=310000&city=310000&carNum=沪%regid%&ecarBelong=31&ecarPart=%regid%&ecarType=02&engineNum=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('27', '北京', 'beijing', '搜狐', 'SohuService', 'province=110000&city=110000&carNum=京%regid%&ecarBelong=11&ecarPart=%regid%&ecarType=02&engineNum=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('28', '深圳', 'shenzhen', '搜狐', 'SohuService', 'province=440000&city=440300&carNum=粤%regid%&ecarBelong=44&ecarPart=%regid%&ecarType=02&engineNum=%engid%&evin=%btmid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('29', '珠海', 'zhuhai', '搜狐', 'SohuService', 'province=440000&city=440400&carNum=粤%regid%&ecarBelong=44&ecarPart=%regid%&ecarType=02&engineNum=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('30', '肇庆', 'zhaoqing', '搜狐', 'SohuService', 'province=440000&city=441201&carNum=粤%regid%&ecarBelong=44&ecarPart=%regid%&ecarType=02&engineNum=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('31', '中山', 'zhongshan', '搜狐', 'SohuService', 'province=440000&city=442000&carNum=粤%regid%&ecarBelong=44&ecarPart=%regid%&ecarType=02&engineNum=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('50', '福州', 'fu1zhou', 'SohuService', 'SohuService', 'province=350000&city=350100&carNum=闽%regid%&ecarBelong=35&ecarPart=%regid%&ecarType=02&evin=%btmid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('51', '益阳', 'yiyang', '搜狐', 'SohuService', 'province=430000&city=430900&carNum=湘%regid%&ecarBelong=43&ecarPart=%regid%&ecarType=02&engineNum=%engid%', '5', '0');
INSERT INTO `city_source_config` VALUES ('60', '深圳', 'shenzhen', '深圳市交管网', 'ShenzhenService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('61', '上海', 'shanghai', '上海市交管网', 'ShanghaiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('62', '广州', 'guangzhou', '广州市交管网', 'GuangzhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('63', '成都', 'chengdu', '成都市交管网', 'ChengduService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('78', '重庆', 'chongqing', '重庆市交管网', 'ChongqingService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('79', '南京', 'nanjing', '南京市交管网', 'NanjingService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('80', '西安', 'xian', '西安市交管网', 'XiAnService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('81', '武汉', 'wuhan', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('82', '宜昌', 'yichang', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('83', '荆州', 'jingzhou', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('84', '襄樊', 'xiangfan', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('85', '黄冈', 'huanggang', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('86', '十堰', 'shiyan', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('87', '黄石', 'huangshi', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('88', '随州', 'suizhou', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('89', '荆门', 'jingmen', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('90', '孝感', 'xiaogan', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('91', '鄂州', 'ezhou', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('92', '咸宁', 'xianning', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('93', '恩施', 'enshi', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('94', '神农架', 'shennongjia', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('95', '潜江', 'qianjiang', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('96', '天门', 'tianmen', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('97', '仙桃', 'xiantao', '湖北交管网', 'HuBeiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('98', '沈阳', 'shenyang', '沈阳市交管网', 'ShenyangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('99', '昆明', 'kunming', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('100', '玉溪', 'yuxi', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('101', '保山', 'baoshan', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('102', '曲靖', 'qujing', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('103', '红河', 'honghe', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('104', '丽江', 'lijiang', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('105', '昭通', 'zhaotong', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('106', '普洱', 'puer', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('107', '临沧', 'lincang', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('108', '大理', 'dali', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('109', '迪庆', 'diqing', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('110', '楚雄', 'chuxiong', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('111', '西双版纳', 'xishuangbanna', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('112', '文山', 'wenshan', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('113', '德宏', 'dehong', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('114', '怒江', 'nujiang', '云南省交管网', 'YunnanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('115', '杭州', 'hangzhou', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('116', '宁波', 'ningbo', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('117', '温州', 'wenzhou', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('118', '台州', 'tai1zhou', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('119', '慈溪', 'cixi', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('120', '余姚', 'yuyao', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('121', '乐清', 'leqing', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('122', '绍兴县', 'shaoxingxian', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('123', '瑞安', 'ruian', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('124', '嘉兴', 'jiaxing', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('125', '绍兴', 'shaoxing', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('126', '温岭', 'wenling', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('127', '桐乡', 'tongxiang', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('128', '海宁', 'haining', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('129', '诸暨', 'zhuji', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('130', '玉环县', 'yuhuanxian', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('131', '上虞', 'shangyu', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('132', '湖州', 'huzhou', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('133', '丽水', 'lishui', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('134', '衢州', 'quzhou', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('135', '舟山', 'zhoushan', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('136', '临海', 'linhai', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('137', '平湖', 'pinghu', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('138', '长兴县', 'changxingxian', '浙江省交管网', 'ZhejiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('139', '太原', 'taiyuan', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('140', '大同', 'datong', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('141', '运城', 'yuncheng', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('142', '长治', 'changzhi', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('143', '临汾', 'linfen', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('144', '晋城', 'jincheng', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('145', '阳泉', 'yangquan', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('146', '忻州', 'xinzhou', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('147', '晋中', 'jinzhong', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('148', '朔州', 'shuozhou', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('149', '吕梁', 'lvliang', '山西省交管网', 'ShanxiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('150', '贵阳', 'guiyang', '贵州省交管网', 'GuizhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('151', '遵义', 'zunyi', '贵州省交管网', 'GuizhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('152', '毕节', 'bijie', '贵州省交管网', 'GuizhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('153', '黔东南', 'qiandongnan', '贵州省交管网', 'GuizhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('154', '六盘水', 'liupanshui', '贵州省交管网', 'GuizhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('155', '安顺', 'anshun', '贵州省交管网', 'GuizhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('156', '铜仁', 'tongren', '贵州省交管网', 'GuizhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('157', '黔南', 'qiannan', '贵州省交管网', 'GuizhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('158', '黔西南', 'qianxinan', '贵州省交管网', 'GuizhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('159', '长春', 'changchun', '吉林省交管网', 'JilinService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('160', '吉林', 'jilin', '吉林省交管网', 'JilinService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('161', '四平', 'siping', '吉林省交管网', 'JilinService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('162', '通化', 'tonghua', '吉林省交管网', 'JilinService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('163', '白山', 'baishan', '吉林省交管网', 'JilinService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('164', '辽源', 'liaoyuan', '吉林省交管网', 'JilinService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('165', '松原', 'songyuan', '吉林省交管网', 'JilinService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('166', '白城', 'baicheng', '吉林省交管网', 'JilinService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('167', '延边', 'yanbian', '吉林省交管网', 'JilinService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('168', '合肥', 'hefei', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('169', '芜湖', 'wuhu', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('170', '阜阳', 'fuyang', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('171', '黄山', 'huangshan', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('172', '蚌埠', 'bengbu', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('173', '安庆', 'anqing', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('174', '马鞍山', 'maanshan', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('175', '亳州', 'bozhou', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('176', '滁州', 'chuzhou', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('177', '铜陵', 'tongling', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('178', '淮南', 'huainan', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('179', '淮北', 'huaibei', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('180', '六安', 'liuan', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('181', '巢湖', 'chaohu', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('182', '宿州', 'su4zhou', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('183', '宣城', 'xuancheng', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('184', '池州', 'chizhou', '安徽省交管网', 'AnhuiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('185', '大连', 'dalian', '大连市交管网', 'CityDalianService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('190', '石家庄', 'shijiazhuang', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('191', '唐山', 'tangshan', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('192', '保定', 'baoding', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('193', '邯郸', 'handan', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('194', '沧州', 'cangzhou', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('195', '廊坊', 'langfang', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('196', '承德', 'chengde', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('197', '秦皇岛', 'qinhuangdao', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('198', '邢台', 'xingtai', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('199', '衡水', 'hengshui', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('200', '张家口', 'zhangjiakou', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('201', '任丘', 'renqiu', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('202', '遵化', 'zunhua', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('203', '迁安', 'qianan', '河北省机动车信息网', 'HebeiService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('204', '兰州', 'lanzhou', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('205', '酒泉', 'jiuquan', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('206', '天水', 'tianshui', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('207', '张掖', 'zhangye', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('208', '白银', 'baiyin', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('209', '庆阳', 'qingyang', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('210', '嘉峪关', 'jiayuguan', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('211', '武威', 'wuwei', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('212', '平凉', 'pingliang', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('213', '金昌', 'jinchang', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('214', '甘南', 'gannan', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('215', '临夏', 'linxia', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('216', '陇南', 'longnan', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('217', '定西', 'dingxi', '甘肃省交管网', 'GansuService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('218', '海口', 'haikou', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('219', '三亚', 'sanya', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('220', '陵水', 'lingshui', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('221', '白沙', 'baisha', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('222', '琼海', 'qionghai', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('223', '琼中', 'qiongzhong', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('224', '澄迈县', 'chengmaixian', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('225', '昌江', 'changjiang', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('226', '文昌', 'wenchang', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('227', '屯昌县', 'tunchangxian', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('228', '定安县', 'dinganxian', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('229', '儋州', 'danzhou', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('230', '保亭', 'baoting', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('231', '五指山', 'wuzhishan', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('232', '乐东', 'ledong', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('233', '临高县', 'lingaoxian', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('234', '东方', 'dongfang', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('235', '万宁', 'wanning', '海南省交管网', 'HainanService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('236', '哈尔滨', 'haerbin', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('237', '大庆', 'daqing', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('238', '佳木斯', 'jiamusi', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('239', '牡丹江', 'mudanjiang', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('240', '齐齐哈尔', 'qiqihaer', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('241', '鸡西', 'jixi', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('242', '伊春', 'yichun_h', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('243', '黑河', 'heihe', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('244', '鹤岗', 'hegang', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('245', '绥化', 'suihua', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('246', '双鸭山', 'shuangyashan', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('247', '七台河', 'qitaihe', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('248', '大兴安岭', 'daxinganling', '黑龙江省交管网', 'HeilongjiangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('249', '银川', 'yinchuan', '宁夏自治区交管网', 'NingxiaService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('250', '吴忠', 'wuzhong', '宁夏自治区交管网', 'NingxiaService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('251', '石嘴山', 'shizuishan', '宁夏自治区交管网', 'NingxiaService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('252', '固原', 'guyuan', '宁夏自治区交管网', 'NingxiaService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('253', '中卫', 'zhongwei', '宁夏自治区交管网', 'NingxiaService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('254', '乌鲁木齐', 'wulumuqi', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('255', '巴音郭楞', 'bayinguoleng', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('256', '伊犁', 'yili', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('257', '克拉玛依', 'kelamayi', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('258', '阿克苏', 'akesu', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('259', '喀什', 'kashi', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('260', '哈密', 'hami', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('261', '和田', 'hetian', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('262', '昌吉', 'changji', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('263', '吐鲁番', 'tulufan', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('264', '阿勒泰', 'aletai', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('265', '塔城', 'tacheng', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('266', '博尔塔拉', 'boertala', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('267', '克孜勒苏', 'kezilesu', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('268', '石河子', 'shihezi', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('269', '阿拉尔', 'alaer', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('270', '图木舒克', 'tumushuke', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('271', '五家渠', 'wujiaqu', '新疆自治区交管网', 'XinjiangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('280', '济南', 'jinan', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('281', '青岛', 'qingdao', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('282', '烟台', 'yantai', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('283', '淄博', 'zibo', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('284', '临沂', 'linyi', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('285', '潍坊', 'weifang', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('286', '泰安', 'taian', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('287', '威海', 'weihai', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('288', '济宁', 'jining', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('289', '东营', 'dongying', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('290', '聊城', 'liaocheng', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('291', '滕州', 'tengzhou', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('292', '日照', 'rizhao', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('293', '德州', 'dezhou', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('294', '荣成', 'rongcheng', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('295', '莱州', 'laizhou', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('296', '莱芜', 'laiwu', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('297', '枣庄', 'zaozhuang', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('298', '菏泽', 'heze', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('299', '滨州', 'binzhou', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('300', '诸城', 'zhucheng', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('301', '蓬莱', 'penglai', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('302', '龙口', 'longkou', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('303', '寿光', 'shouguang', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('304', '章丘', 'zhangqiu', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('305', '胶州', 'jiaozhou', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('306', '平度', 'pingdu', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('307', '招远', 'zhaoyuan', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('308', '文登', 'wendeng', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('309', '邹城', 'zoucheng', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('310', '兖州', 'yanzhou', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('311', '乳山', 'rushan', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('312', '邹平县', 'zoupingxian', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('313', '新泰', 'xintai', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('314', '肥城', 'feicheng', '山东省交管网', 'WxShandongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('321', '湛江', 'zhanjiang', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('322', '韶关', 'shaoguan', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('323', '茂名', 'maoming', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('324', '清远', 'qingyuan', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('325', '梅州', 'meizhou', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('326', '河源', 'heyuan', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('327', '增城', 'zengcheng', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('328', '云浮', 'yunfu', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('329', '汕尾', 'shanwei', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('330', '东莞', 'dongguan', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('331', '佛山', 'foshan', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('332', '中山', 'zhongshan', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('333', '汕头', 'shantou', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('334', '珠海', 'zhuhai', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('335', '江门', 'jiangmen', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('336', '揭阳', 'jieyang', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('337', '惠州', 'huizhou', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('338', '潮州', 'chaozhou', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('339', '肇庆', 'zhaoqing', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('340', '阳江', 'yangjiang', '广东省交管网', 'WxGuangdongService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('350', '苏州', 'su1zhou', '苏州市交管网', 'CityWxSuzhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('351', '郑州', 'zhengzhou', '郑州市交管网', 'CityZhengzhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('352', '福州', 'fu1zhou', '福州市交管网', 'CityFuzhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('353', '常州', 'changzhou', '常州市交管网', 'CityChangzhouService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('354', '洛阳', 'luoyang', '洛阳市交管网', 'CityLuoyangService', '', '6', '1');
INSERT INTO `city_source_config` VALUES ('355', '厦门', 'shamen', '厦门市交管网', 'CityXiamenService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('356', '无锡', 'wuxi', '无锡市交管网', 'CityWuxiService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('357', '濮阳', 'puyang', '濮阳市交管网', 'CityPuyangService', '', '6', '0');
INSERT INTO `city_source_config` VALUES ('358', '许昌', 'xuchang', '许昌市交管网', 'CityXuchangService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('359', '徐州', 'xuzhou', '徐州市交管网', 'CityXuzhouService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('360', '扬州', 'yangzhou', '扬州市交管网', 'CityYangzhouService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('361', '镇江', 'zhenjiang', '镇江市交管网', 'CityZhenjiangService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('362', '南通', 'nantong', '南通市交管网', 'CityNantongService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('363', '盐城', 'yancheng', '盐城市交管网', 'CityYanchengService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('364', '连云港', 'lianyungang', '连云港市交管网', 'CityLianyungangService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('365', '鞍山', 'anshan', '鞍山市交管网', 'CityAnshanService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('366', '焦作', 'jiaozuo', '焦作市交管网', 'CityJiaozuoService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('367', '西宁', 'xining', '青海省交管网', 'QinghaiService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('368', '海东', 'haidong', '青海省交管网', 'QinghaiService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('369', '海北', 'haibei', '青海省交管网', 'QinghaiService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('370', '黄南', 'huangnan', '青海省交管网', 'QinghaiService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('371', '海南州', 'hainanz', '青海省交管网', 'QinghaiService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('372', '果洛州', 'guoluoz', '青海省交管网', 'QinghaiService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('373', '玉树州', 'yushuz', '青海省交管网', 'QinghaiService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('374', '海西州', 'haixiz', '青海省交管网', 'QinghaiService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('375', '昆山', 'kunshan', '苏州市交管网', 'CityWxSuzhouService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('376', '常熟', 'changshu', '苏州市交管网', 'CityWxSuzhouService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('377', '太仓', 'taicang', '苏州市交管网', 'CityWxSuzhouService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('378', '张家港', 'zhangjiagang', '苏州市交管网', 'CityWxSuzhouService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('379', '商丘', 'shangqiu', '商丘市交管网', 'CityShangqiuService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('381', '驻马店', 'zhumadian', '驻马店市交管网', 'CityZmdService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('382', '营口', 'yingkou', '营口市交管网', 'CityYingkouService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('383', '安阳', 'anyang', '安阳市交管网', 'CityAnyangService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('384', '开封', 'kaifeng', '开封市交管网', 'CityKaifengService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('385', '周口', 'zhoukou', '周口市交管网', 'CityZhoukouService', '', '7', '1');
INSERT INTO `city_source_config` VALUES ('386', '延边', 'yanbian', '延边市交管网', 'CityYanbianService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('387', '汉中', 'hanzhong', '汉中市交管网', 'CityHanzhongService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('388', '抚顺', 'fushun', '抚顺市交管网', 'CityFushunService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('389', '阜新', 'fuxin', '阜新市交管网', 'CityFuxinService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('390', '丹东', 'dandong', '丹东市交管网', 'CityDandongService', '', '7', '1');
INSERT INTO `city_source_config` VALUES ('391', '辽阳', 'liaoyang', '辽阳市交管网', 'CityLiaoyangService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('392', '葫芦岛', 'huludao', '葫芦岛市交管网', 'CityHuludaoService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('393', '济源', 'jiyuan', '济源市交管网', 'CityJiyuanService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('394', '丹阳', 'danyang', '镇江市交管网', 'CityZhenjiangService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('396', '瓦房店', 'wafangdian', '大连市交管网', 'CityDalianService', '', '7', '1');
INSERT INTO `city_source_config` VALUES ('397', '溧阳', 'liyang', '常州市交管网', 'CityChangzhouService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('398', '海门', 'haimen', '南通市交管网', 'CityNantongService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('399', '启东', 'qidong', '南通市交管网', 'CityNantongService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('400', '通州', 'tongzhou', '南通市交管网', 'CityNantongService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('401', '平顶山', 'pingdingshan', '平顶山市交管网', 'CityPingdingshanService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('402', '拉萨', 'lasa', '拉萨市交管网', 'LasaService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('403', '益阳', 'yiyang', '益阳市交管网', 'CityYiyangService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('404', '本溪', 'benxi', '本溪市交管网', 'CityBenxiService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('405', '宿迁', 'suqian', '宿迁市交管网', 'CitySuqianService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('407', '苏州', 'su1zhou', '苏州交管网', 'CitySuzhouService', '', '7', '0');
INSERT INTO `city_source_config` VALUES ('408', '昆山', 'kunshan', '苏州交管网', 'CitySuzhouService', '', '8', '0');
INSERT INTO `city_source_config` VALUES ('409', '常熟', 'changshu', '苏州交管网', 'CitySuzhouService', '', '8', '0');
INSERT INTO `city_source_config` VALUES ('410', '太仓', 'taicang', '苏州交管网', 'CitySuzhouService', '', '8', '0');
INSERT INTO `city_source_config` VALUES ('411', '张家港', 'zhangjiagang', '苏州交管网', 'CitySuzhouService', '', '8', '0');
INSERT INTO `city_source_config` VALUES ('412', '鹤壁', 'hebi', '鹤壁交管网', 'CityHebiService', '', '7', '1');
INSERT INTO `city_source_config` VALUES ('1001', '洛阳', 'luoyang', 'Car100Service', 'Car100Service', 'luoyang,河南,洛阳,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('1002', '焦作', 'jiaozuo', 'Car100Service', 'Car100Service', 'jiaozuo,河南,焦作,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1003', '开封', 'kaifeng', 'Car100Service', 'Car100Service', 'kaifeng,河南,开封,-1,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1004', '鹤壁', 'hebi', 'Car100Service', 'Car100Service', 'hebi,河南,鹤壁,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1005', '商丘', 'shangqiu', 'Car100Service', 'Car100Service', 'shangqiu,河南,商丘,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1006', '周口', 'zhoukou', 'Car100Service', 'Car100Service', 'zhoukou,河南,周口,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1007', '驻马店', 'zhumadian', 'Car100Service', 'Car100Service', 'zhumadian,河南,驻马店,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('1008', '济源', 'jiyuan', 'Car100Service', 'Car100Service', 'jiyuan,河南,济源,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1009', '济南', 'jinan', 'Car100Service', 'Car100Service', 'jinan,山东,济南,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1010', '青岛', 'qingdao', 'Car100Service', 'Car100Service', 'qingdao,山东,青岛,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1011', '淄博', 'zibo', 'Car100Service', 'Car100Service', 'zibo,山东,淄博,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1012', '临沂', 'linyi', 'Car100Service', 'Car100Service', 'linyi,山东,临沂,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1013', '泰安', 'taian', 'Car100Service', 'Car100Service', 'taian,山东,泰安,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1014', '济宁', 'jining', 'Car100Service', 'Car100Service', 'jining,山东,济宁,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1015', '东营', 'dongying', 'Car100Service', 'Car100Service', 'dongying,山东,东营,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1016', '聊城', 'liaocheng', 'Car100Service', 'Car100Service', 'liaocheng,山东,聊城,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1017', '日照', 'rizhao', 'Car100Service', 'Car100Service', 'rizhao,山东,日照,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1018', '德州', 'dezhou', 'Car100Service', 'Car100Service', 'dezhou,山东,德州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1019', '莱芜', 'laiwu', 'Car100Service', 'Car100Service', 'laiwu,山东,莱芜,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1020', '枣庄', 'zaozhuang', 'Car100Service', 'Car100Service', 'zaozhuang,山东,枣庄,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1021', '菏泽', 'heze', 'Car100Service', 'Car100Service', 'heze,山东,菏泽,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1022', '滨州', 'binzhou', 'Car100Service', 'Car100Service', 'binzhou,山东,滨州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1023', '成都', 'chengdu', 'Car100Service', 'Car100Service', 'chengdu,四川,成都,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1024', '西宁', 'xining', 'Car100Service', 'Car100Service', 'xining,青海,西宁,0,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('1025', '乌鲁木齐', 'wulumuqi', 'Car100Service', 'Car100Service', 'wulumuqi,新疆,乌鲁木齐,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1026', '克拉玛依', 'kelamayi', 'Car100Service', 'Car100Service', 'kelamayi,新疆,克拉玛依,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1027', '克孜勒苏', 'kezilesu', 'Car100Service', 'Car100Service', 'kezilesu,新疆,克孜勒苏,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1028', '石河子', 'shihezi', 'Car100Service', 'Car100Service', 'shihezi,新疆,石河子,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1029', '杭州', 'hangzhou', 'Car100Service', 'Car100Service', 'hangzhou,浙江,杭州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1030', '宁波', 'ningbo', 'Car100Service', 'Car100Service', 'ningbo,浙江,宁波,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1031', '台州', 'tai1zhou', 'Car100Service', 'Car100Service', 'tai1zhou,浙江,台州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1032', '绍兴县', 'shaoxingxian', 'Car100Service', 'Car100Service', 'shaoxingxian,浙江,绍兴县,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1033', '嘉兴', 'jiaxing', 'Car100Service', 'Car100Service', 'jiaxing,浙江,嘉兴,-1,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1034', '湖州', 'huzhou', 'Car100Service', 'Car100Service', 'huzhou,浙江,湖州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1035', '丽水', 'lishui', 'Car100Service', 'Car100Service', 'lishui,浙江,丽水,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1036', '衢州', 'quzhou', 'Car100Service', 'Car100Service', 'quzhou,浙江,衢州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1037', '舟山', 'zhoushan', 'Car100Service', 'Car100Service', 'zhoushan,浙江,舟山,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1038', '大庆', 'daqing', 'Car100Service', 'Car100Service', 'daqing,黑龙江,大庆,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1039', '佳木斯', 'jiamusi', 'Car100Service', 'Car100Service', 'jiamusi,黑龙江,佳木斯,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1040', '牡丹江', 'mudanjiang', 'Car100Service', 'Car100Service', 'mudanjiang,黑龙江,牡丹江,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1041', '齐齐哈尔', 'qiqihaer', 'Car100Service', 'Car100Service', 'qiqihaer,黑龙江,齐齐哈尔,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1042', '鸡西', 'jixi', 'Car100Service', 'Car100Service', 'jixi,黑龙江,鸡西,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1043', '伊春', 'yichun_h', 'Car100Service', 'Car100Service', 'yichun_h,黑龙江,伊春,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1044', '黑河', 'heihe', 'Car100Service', 'Car100Service', 'heihe,黑龙江,黑河,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1045', '鹤岗', 'hegang', 'Car100Service', 'Car100Service', 'hegang,黑龙江,鹤岗,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1046', '绥化', 'suihua', 'Car100Service', 'Car100Service', 'suihua,黑龙江,绥化,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1047', '双鸭山', 'shuangyashan', 'Car100Service', 'Car100Service', 'shuangyashan,黑龙江,双鸭山,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1048', '七台河', 'qitaihe', 'Car100Service', 'Car100Service', 'qitaihe,黑龙江,七台河,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1049', '大兴安岭', 'daxinganling', 'Car100Service', 'Car100Service', 'daxinganling,黑龙江,大兴安岭,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1050', '北京', 'beijing', 'Car100Service', 'Car100Service', 'beijing,北京,北京,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('1051', '宝鸡', 'baoji', 'Car100Service', 'Car100Service', 'baoji,陕西,宝鸡,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1052', '兰州', 'lanzhou', 'Car100Service', 'Car100Service', 'lanzhou,甘肃,兰州,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1053', '酒泉', 'jiuquan', 'Car100Service', 'Car100Service', 'jiuquan,甘肃,酒泉,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1054', '天水', 'tianshui', 'Car100Service', 'Car100Service', 'tianshui,甘肃,天水,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1055', '张掖', 'zhangye', 'Car100Service', 'Car100Service', 'zhangye,甘肃,张掖,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1056', '白银', 'baiyin', 'Car100Service', 'Car100Service', 'baiyin,甘肃,白银,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1057', '庆阳', 'qingyang', 'Car100Service', 'Car100Service', 'qingyang,甘肃,庆阳,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1058', '嘉峪关', 'jiayuguan', 'Car100Service', 'Car100Service', 'jiayuguan,甘肃,嘉峪关,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1059', '武威', 'wuwei', 'Car100Service', 'Car100Service', 'wuwei,甘肃,武威,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1060', '平凉', 'pingliang', 'Car100Service', 'Car100Service', 'pingliang,甘肃,平凉,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1061', '金昌', 'jinchang', 'Car100Service', 'Car100Service', 'jinchang,甘肃,金昌,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1062', '陇南', 'longnan', 'Car100Service', 'Car100Service', 'longnan,甘肃,陇南,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1063', '定西', 'dingxi', 'Car100Service', 'Car100Service', 'dingxi,甘肃,定西,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('1064', '吉林', 'jilin', 'Car100Service', 'Car100Service', 'jilin,吉林,吉林,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1065', '四平', 'siping', 'Car100Service', 'Car100Service', 'siping,吉林,四平,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1066', '通化', 'tonghua', 'Car100Service', 'Car100Service', 'tonghua,吉林,通化,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1067', '白山', 'baishan', 'Car100Service', 'Car100Service', 'baishan,吉林,白山,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1068', '辽源', 'liaoyuan', 'Car100Service', 'Car100Service', 'liaoyuan,吉林,辽源,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1069', '松原', 'songyuan', 'Car100Service', 'Car100Service', 'songyuan,吉林,松原,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1070', '白城', 'baicheng', 'Car100Service', 'Car100Service', 'baicheng,吉林,白城,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1071', '合肥', 'hefei', 'Car100Service', 'Car100Service', 'hefei,安徽,合肥,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1072', '芜湖', 'wuhu', 'Car100Service', 'Car100Service', 'wuhu,安徽,芜湖,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1073', '阜阳', 'fuyang', 'Car100Service', 'Car100Service', 'fuyang,安徽,阜阳,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1074', '黄山', 'huangshan', 'Car100Service', 'Car100Service', 'huangshan,安徽,黄山,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1075', '蚌埠', 'bengbu', 'Car100Service', 'Car100Service', 'bengbu,安徽,蚌埠,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1076', '安庆', 'anqing', 'Car100Service', 'Car100Service', 'anqing,安徽,安庆,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1077', '马鞍山', 'maanshan', 'Car100Service', 'Car100Service', 'maanshan,安徽,马鞍山,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1078', '亳州', 'bozhou', 'Car100Service', 'Car100Service', 'bozhou,安徽,亳州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1079', '滁州', 'chuzhou', 'Car100Service', 'Car100Service', 'chuzhou,安徽,滁州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1080', '铜陵', 'tongling', 'Car100Service', 'Car100Service', 'tongling,安徽,铜陵,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1081', '淮南', 'huainan', 'Car100Service', 'Car100Service', 'huainan,安徽,淮南,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1082', '淮北', 'huaibei', 'Car100Service', 'Car100Service', 'huaibei,安徽,淮北,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1083', '六安', 'liuan', 'Car100Service', 'Car100Service', 'liuan,安徽,六安,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1084', '巢湖', 'chaohu', 'Car100Service', 'Car100Service', 'chaohu,安徽,巢湖,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1085', '宿州', 'su4zhou', 'Car100Service', 'Car100Service', 'su4zhou,安徽,宿州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1086', '宣城', 'xuancheng', 'Car100Service', 'Car100Service', 'xuancheng,安徽,宣城,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1087', '池州', 'chizhou', 'Car100Service', 'Car100Service', 'chizhou,安徽,池州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1088', '兴安', 'xingan', 'Car100Service', 'Car100Service', 'xingan,内蒙古,兴安,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('1089', '赤峰', 'chifeng', 'Car100Service', 'Car100Service', 'chifeng,内蒙古,赤峰,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('1090', '锡林郭勒', 'xilinguole', 'Car100Service', 'Car100Service', 'xilinguole,内蒙古,锡林郭勒,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('1091', '阿拉善', 'alashan', 'Car100Service', 'Car100Service', 'alashan,内蒙古,阿拉善,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('1092', '通辽', 'tongliao', 'Car100Service', 'Car100Service', 'tongliao,内蒙古,通辽,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('1093', '上海', 'shanghai', 'Car100Service', 'Car100Service', 'shanghai,上海,上海,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('1094', '太原', 'taiyuan', 'Car100Service', 'Car100Service', 'taiyuan,山西,太原,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1095', '大同', 'datong', 'Car100Service', 'Car100Service', 'datong,山西,大同,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1096', '运城', 'yuncheng', 'Car100Service', 'Car100Service', 'yuncheng,山西,运城,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1097', '长治', 'changzhi', 'Car100Service', 'Car100Service', 'changzhi,山西,长治,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1098', '临汾', 'linfen', 'Car100Service', 'Car100Service', 'linfen,山西,临汾,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1099', '晋城', 'jincheng', 'Car100Service', 'Car100Service', 'jincheng,山西,晋城,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1100', '阳泉', 'yangquan', 'Car100Service', 'Car100Service', 'yangquan,山西,阳泉,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1101', '忻州', 'xinzhou', 'Car100Service', 'Car100Service', 'xinzhou,山西,忻州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1102', '晋中', 'jinzhong', 'Car100Service', 'Car100Service', 'jinzhong,山西,晋中,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1103', '朔州', 'shuozhou', 'Car100Service', 'Car100Service', 'shuozhou,山西,朔州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1104', '吕梁', 'lvliang', 'Car100Service', 'Car100Service', 'lvliang,山西,吕梁,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1105', '琼海', 'qionghai', 'Car100Service', 'Car100Service', 'qionghai,海南,琼海,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1106', '五指山', 'wuzhishan', 'Car100Service', 'Car100Service', 'wuzhishan,海南,五指山,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('1107', '锦州', 'jinzhou', 'Car100Service', 'Car100Service', 'jinzhou,辽宁,锦州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('1108', '葫芦岛', 'huludao', 'Car100Service', 'Car100Service', 'huludao,辽宁,葫芦岛,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('2001', '﻿贵阳', 'guiyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2002', '遵义', 'zunyi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2003', '毕节', 'bijie', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2004', '黔东南', 'qiandongnan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2005', '六盘水', 'liupanshui', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2006', '安顺', 'anshun', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2007', '铜仁', 'tongren', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2008', '黔南', 'qiannan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2009', '黔西南', 'qianxinan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2010', '新乡', 'xinxiang', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2011', '洛阳', 'luoyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2012', '焦作', 'jiaozuo', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2013', '安阳', 'anyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2014', '南阳', 'nanyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2015', '开封', 'kaifeng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2016', '鹤壁', 'hebi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2017', '濮阳', 'puyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2018', '平顶山', 'pingdingshan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2019', '三门峡', 'sanmenxia', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2020', '商丘', 'shangqiu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2021', '信阳', 'xinyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2022', '许昌', 'xuchang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2023', '周口', 'zhoukou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2024', '驻马店', 'zhumadian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2025', '济源', 'jiyuan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2026', '济南', 'jinan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2027', '青岛', 'qingdao', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2028', '烟台', 'yantai', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2029', '淄博', 'zibo', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2030', '临沂', 'linyi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2031', '潍坊', 'weifang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2032', '泰安', 'taian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2033', '威海', 'weihai', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2034', '济宁', 'jining', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2035', '东营', 'dongying', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2036', '聊城', 'liaocheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2037', '滕州', 'tengzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2038', '日照', 'rizhao', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2039', '德州', 'dezhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2040', '荣成', 'rongcheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2041', '莱州', 'laizhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2042', '莱芜', 'laiwu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2043', '枣庄', 'zaozhuang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2044', '菏泽', 'heze', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2045', '滨州', 'binzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2046', '诸城', 'zhucheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2047', '蓬莱', 'penglai', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2048', '龙口', 'longkou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2049', '寿光', 'shouguang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2050', '章丘', 'zhangqiu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2051', '胶州', 'jiaozhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2052', '平度', 'pingdu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2053', '招远', 'zhaoyuan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2054', '文登', 'wendeng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2055', '邹城', 'zoucheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2056', '兖州', 'yanzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2057', '乳山', 'rushan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2058', '邹平县', 'zoupingxian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2059', '新泰', 'xintai', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2060', '肥城', 'feicheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2061', '成都', 'chengdu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2062', '双流县', 'shuangliuxian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2063', '南京', 'nanjing', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2064', '无锡', 'wuxi', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2065', '常州', 'changzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2066', '苏州', 'su1zhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2067', '张家港', 'zhangjiagang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2068', '江阴', 'jiangyin', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2069', '扬州', 'yangzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2070', '徐州', 'xuzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2071', '南通', 'nantong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2072', '宜兴', 'yixing', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2073', '盐城', 'yancheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2074', '昆山', 'kunshan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2075', '常熟', 'changshu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2076', '镇江', 'zhenjiang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2077', '连云港', 'lianyungang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2078', '江都', 'jiangdou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2079', '丹阳', 'danyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2080', '太仓', 'taicang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2081', '溧阳', 'liyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2082', '海门', 'haimen', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2083', '启东', 'qidong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2084', '通州', 'tongzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2085', '西宁', 'xining', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2086', '乌鲁木齐', 'wulumuqi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2087', '巴音郭楞', 'bayinguoleng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2088', '伊犁', 'yili', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2089', '克拉玛依', 'kelamayi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2090', '阿克苏', 'akesu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2091', '喀什', 'kashi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2092', '哈密', 'hami', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2093', '和田', 'hetian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2094', '昌吉', 'changji', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2095', '吐鲁番', 'tulufan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2096', '阿勒泰', 'aletai', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2097', '塔城', 'tacheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2098', '博尔塔拉', 'boertala', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2099', '克孜勒苏', 'kezilesu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2100', '石河子', 'shihezi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2101', '阿拉尔', 'alaer', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2102', '图木舒克', 'tumushuke', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2103', '五家渠', 'wujiaqu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2104', '福州', 'fu1zhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2105', '厦门', 'shamen', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2106', '泉州', 'quanzhou', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2107', '晋江', 'jinjiang', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2108', '石狮', 'shishi', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2109', '莆田', 'futian', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2110', '漳州', 'zhangzhou', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2111', '南安', 'nanan', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2112', '三明', 'sanming', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2113', '龙岩', 'longyan', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2114', '南平', 'nanping', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2115', '宁德', 'ningde', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2116', '福清', 'fuqing', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2117', '惠安县', 'huianxian', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2118', '杭州', 'hangzhou', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2119', '宁波', 'ningbo', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2120', '温州', 'wenzhou', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2121', '义乌', 'yiwu', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2122', '台州', 'tai1zhou', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2123', '慈溪', 'cixi', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2124', '余姚', 'yuyao', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2125', '永康', 'yongkang', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2126', '乐清', 'leqing', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2127', '绍兴县', 'shaoxingxian', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2128', '瑞安', 'ruian', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2129', '嘉兴', 'jiaxing', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2130', '金华', 'jinhua', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2131', '绍兴', 'shaoxing', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2132', '温岭', 'wenling', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2133', '桐乡', 'tongxiang', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2134', '海宁', 'haining', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2135', '诸暨', 'zhuji', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2136', '玉环县', 'yuhuanxian', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2137', '上虞', 'shangyu', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2138', '湖州', 'huzhou', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2139', '丽水', 'lishui', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2140', '衢州', 'quzhou', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2141', '舟山', 'zhoushan', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2142', '临海', 'linhai', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2143', '平湖', 'pinghu', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2144', '长兴县', 'changxingxian', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2145', '武汉', 'wuhan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2146', '宜昌', 'yichang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2147', '荆州', 'jingzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2148', '襄樊', 'xiangfan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2149', '黄冈', 'huanggang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2150', '十堰', 'shiyan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2151', '黄石', 'huangshi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2152', '随州', 'suizhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2153', '荆门', 'jingmen', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2154', '孝感', 'xiaogan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2155', '鄂州', 'ezhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2156', '咸宁', 'xianning', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2157', '恩施', 'enshi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2158', '神农架', 'shennongjia', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2159', '潜江', 'qianjiang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2160', '天门', 'tianmen', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2161', '仙桃', 'xiantao', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2162', '那曲', 'naqu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2163', '哈尔滨', 'haerbin', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2164', '大庆', 'daqing', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2165', '佳木斯', 'jiamusi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2166', '牡丹江', 'mudanjiang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2167', '齐齐哈尔', 'qiqihaer', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2168', '鸡西', 'jixi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2169', '伊春', 'yichun_h', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2170', '黑河', 'heihe', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2171', '鹤岗', 'hegang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2172', '绥化', 'suihua', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2173', '双鸭山', 'shuangyashan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2174', '七台河', 'qitaihe', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2175', '大兴安岭', 'daxinganling', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2176', '重庆', 'chongqing', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2177', '昆明', 'kunming', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2178', '玉溪', 'yuxi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2179', '保山', 'baoshan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2180', '曲靖', 'qujing', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2181', '红河', 'honghe', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2182', '丽江', 'lijiang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2183', '昭通', 'zhaotong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2184', '普洱', 'puer', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2185', '临沧', 'lincang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2186', '大理', 'dali', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2187', '迪庆', 'diqing', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2188', '楚雄', 'chuxiong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2189', '西双版纳', 'xishuangbanna', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2190', '文山', 'wenshan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2191', '德宏', 'dehong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2192', '怒江', 'nujiang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2193', '北京', 'beijing', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2194', '西安', 'xian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2195', '宝鸡', 'baoji', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2196', '安康', 'ankang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2197', '咸阳', 'xianyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2198', '汉中', 'hanzhong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2199', '渭南', 'weinan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2200', '延安', 'yanan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2201', '兰州', 'lanzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2202', '酒泉', 'jiuquan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2203', '天水', 'tianshui', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2204', '张掖', 'zhangye', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2205', '白银', 'baiyin', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2206', '庆阳', 'qingyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2207', '嘉峪关', 'jiayuguan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2208', '武威', 'wuwei', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2209', '平凉', 'pingliang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2210', '金昌', 'jinchang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2211', '甘南', 'gannan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2212', '临夏', 'linxia', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2213', '陇南', 'longnan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2214', '定西', 'dingxi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2215', '石家庄', 'shijiazhuang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2216', '唐山', 'tangshan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2217', '保定', 'baoding', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2218', '邯郸', 'handan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2219', '沧州', 'cangzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2220', '廊坊', 'langfang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2221', '承德', 'chengde', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2222', '秦皇岛', 'qinhuangdao', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2223', '邢台', 'xingtai', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2224', '衡水', 'hengshui', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2225', '张家口', 'zhangjiakou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2226', '任丘', 'renqiu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2227', '遵化', 'zunhua', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2228', '迁安', 'qianan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2229', '长春', 'changchun', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2230', '吉林', 'jilin', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2231', '四平', 'siping', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2232', '通化', 'tonghua', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2233', '白山', 'baishan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2234', '辽源', 'liaoyuan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2235', '松原', 'songyuan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2236', '白城', 'baicheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2237', '延边', 'yanbian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2238', '广州', 'guangzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2239', '深圳', 'shenzhen', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2240', '东莞', 'dongguan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2241', '佛山', 'foshan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2242', '中山', 'zhongshan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2243', '汕头', 'shantou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2244', '珠海', 'zhuhai', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2245', '江门', 'jiangmen', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2246', '揭阳', 'jieyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2247', '惠州', 'huizhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2248', '潮州', 'chaozhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2249', '肇庆', 'zhaoqing', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2250', '阳江', 'yangjiang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2251', '湛江', 'zhanjiang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2252', '韶关', 'shaoguan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2253', '茂名', 'maoming', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2254', '清远', 'qingyuan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2255', '梅州', 'meizhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2256', '河源', 'heyuan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2257', '增城', 'zengcheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2258', '云浮', 'yunfu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2259', '汕尾', 'shanwei', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2260', '银川', 'yinchuan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2261', '吴忠', 'wuzhong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2262', '石嘴山', 'shizuishan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2263', '固原', 'guyuan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2264', '中卫', 'zhongwei', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2265', '长沙', 'changsha', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2266', '株洲', 'zhuzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2267', '益阳', 'yiyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2268', '合肥', 'hefei', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2269', '芜湖', 'wuhu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2270', '阜阳', 'fuyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2271', '黄山', 'huangshan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2272', '蚌埠', 'bengbu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2273', '安庆', 'anqing', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2274', '马鞍山', 'maanshan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2275', '亳州', 'bozhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2276', '滁州', 'chuzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2277', '铜陵', 'tongling', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2278', '淮南', 'huainan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2279', '淮北', 'huaibei', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2280', '六安', 'liuan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2281', '巢湖', 'chaohu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2282', '宿州', 'su4zhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2283', '宣城', 'xuancheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2284', '池州', 'chizhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2285', '呼和浩特', 'huhehaote', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2286', '包头', 'baotou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2287', '兴安', 'xingan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2288', '准格尔', 'zhungeer', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2289', '赤峰', 'chifeng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2290', '锡林郭勒', 'xilinguole', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2291', '阿拉善', 'alashan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2292', '通辽', 'tongliao', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2293', '鄂尔多斯', 'eerduosi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2294', '乌兰察布', 'wulanchabu', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2295', '呼伦贝尔', 'hulunbeier', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2296', '巴彦淖尔', 'bayannaoer', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2297', '乌海', 'wuhai', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2298', '上海', 'shanghai', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2299', '太原', 'taiyuan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2300', '大同', 'datong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2301', '运城', 'yuncheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2302', '长治', 'changzhi', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2303', '临汾', 'linfen', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2304', '晋城', 'jincheng', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2305', '阳泉', 'yangquan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2306', '忻州', 'xinzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2307', '晋中', 'jinzhong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2308', '朔州', 'shuozhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2309', '吕梁', 'lvliang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2310', '海口', 'haikou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2311', '三亚', 'sanya', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2312', '陵水', 'lingshui', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2313', '白沙', 'baisha', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2314', '琼海', 'qionghai', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2315', '琼中', 'qiongzhong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2316', '澄迈县', 'chengmaixian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2317', '昌江', 'changjiang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2318', '文昌', 'wenchang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2319', '屯昌县', 'tunchangxian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2320', '定安县', 'dinganxian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2321', '儋州', 'danzhou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2322', '保亭', 'baoting', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2323', '五指山', 'wuzhishan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2324', '乐东', 'ledong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2325', '临高县', 'lingaoxian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2326', '东方', 'dongfang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2327', '万宁', 'wanning', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2328', '沈阳', 'shenyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2329', '大连', 'dalian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2330', '鞍山', 'anshan', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2331', '丹东', 'dandong', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2332', '锦州', 'jinzhou', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2333', '抚顺', 'fushun', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2334', '辽阳', 'liaoyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2335', '营口', 'yingkou', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2336', '阜新', 'fuxin', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2337', '葫芦岛', 'huludao', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2338', '朝阳', 'zhaoyang', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2339', '盘锦', 'panjin', '微车', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('2340', '瓦房店', 'wafangdian', 'WecheService', 'WecheService', '', '4', '0');
INSERT INTO `city_source_config` VALUES ('3001', '﻿贵阳', 'guiyang', 'OpenCarHomeService', 'OpenCarHomeService', 'guiyang,520100,贵州,﻿贵阳,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3002', '遵义', 'zunyi', 'OpenCarHomeService', 'OpenCarHomeService', 'zunyi,520300,贵州,遵义,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3003', '毕节', 'bijie', 'OpenCarHomeService', 'OpenCarHomeService', 'bijie,522400,贵州,毕节,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3004', '黔东南', 'qiandongnan', 'OpenCarHomeService', 'OpenCarHomeService', 'qiandongnan,522600,贵州,黔东南,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3005', '六盘水', 'liupanshui', 'OpenCarHomeService', 'OpenCarHomeService', 'liupanshui,520200,贵州,六盘水,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3006', '安顺', 'anshun', 'OpenCarHomeService', 'OpenCarHomeService', 'anshun,520400,贵州,安顺,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3007', '铜仁', 'tongren', 'OpenCarHomeService', 'OpenCarHomeService', 'tongren,522200,贵州,铜仁,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3008', '黔南', 'qiannan', 'OpenCarHomeService', 'OpenCarHomeService', 'qiannan,522700,贵州,黔南,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3009', '黔西南', 'qianxinan', 'OpenCarHomeService', 'OpenCarHomeService', 'qianxinan,522300,贵州,黔西南,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3010', '洛阳', 'luoyang', 'OpenCarHomeService', 'OpenCarHomeService', 'luoyang,410300,河南,洛阳,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3011', '焦作', 'jiaozuo', 'OpenCarHomeService', 'OpenCarHomeService', 'jiaozuo,410800,河南,焦作,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3012', '商丘', 'shangqiu', 'OpenCarHomeService', 'OpenCarHomeService', 'shangqiu,411400,河南,商丘,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3013', '青岛', 'qingdao', 'OpenCarHomeService', 'OpenCarHomeService', 'qingdao,370200,山东,青岛,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3014', '烟台', 'yantai', 'OpenCarHomeService', 'OpenCarHomeService', 'yantai,370600,山东,烟台,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3015', '淄博', 'zibo', 'OpenCarHomeService', 'OpenCarHomeService', 'zibo,370300,山东,淄博,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3016', '临沂', 'linyi', 'OpenCarHomeService', 'OpenCarHomeService', 'linyi,371300,山东,临沂,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3017', '泰安', 'taian', 'OpenCarHomeService', 'OpenCarHomeService', 'taian,370900,山东,泰安,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3018', '威海', 'weihai', 'OpenCarHomeService', 'OpenCarHomeService', 'weihai,371000,山东,威海,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3019', '济宁', 'jining', 'OpenCarHomeService', 'OpenCarHomeService', 'jining,370800,山东,济宁,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3020', '东营', 'dongying', 'OpenCarHomeService', 'OpenCarHomeService', 'dongying,370500,山东,东营,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3021', '聊城', 'liaocheng', 'OpenCarHomeService', 'OpenCarHomeService', 'liaocheng,371500,山东,聊城,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3022', '日照', 'rizhao', 'OpenCarHomeService', 'OpenCarHomeService', 'rizhao,371100,山东,日照,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3023', '德州', 'dezhou', 'OpenCarHomeService', 'OpenCarHomeService', 'dezhou,371400,山东,德州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3024', '莱芜', 'laiwu', 'OpenCarHomeService', 'OpenCarHomeService', 'laiwu,371200,山东,莱芜,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3025', '枣庄', 'zaozhuang', 'OpenCarHomeService', 'OpenCarHomeService', 'zaozhuang,370400,山东,枣庄,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3026', '菏泽', 'heze', 'OpenCarHomeService', 'OpenCarHomeService', 'heze,371700,山东,菏泽,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3027', '滨州', 'binzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'binzhou,371600,山东,滨州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3028', '成都', 'chengdu', 'OpenCarHomeService', 'OpenCarHomeService', 'chengdu,510100,四川,成都,0,8', '3', '0');
INSERT INTO `city_source_config` VALUES ('3029', '南京', 'nanjing', 'OpenCarHomeService', 'OpenCarHomeService', 'nanjing,320100,江苏,南京,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3030', '常州', 'changzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'changzhou,320400,江苏,常州,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3031', '扬州', 'yangzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'yangzhou,321000,江苏,扬州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3032', '徐州', 'xuzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'xuzhou,320300,江苏,徐州,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3033', '镇江', 'zhenjiang', 'OpenCarHomeService', 'OpenCarHomeService', 'zhenjiang,321100,江苏,镇江,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3034', '连云港', 'lianyungang', 'OpenCarHomeService', 'OpenCarHomeService', 'lianyungang,320700,江苏,连云港,6,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3035', '福州', 'fu1zhou', 'OpenCarHomeService', 'OpenCarHomeService', 'fu1zhou,350100,福建,福州,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3036', '泉州', 'quanzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'quanzhou,350500,福建,泉州,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3037', '莆田', 'futian', 'OpenCarHomeService', 'OpenCarHomeService', 'futian,350300,福建,莆田,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3038', '漳州', 'zhangzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'zhangzhou,350600,福建,漳州,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3039', '三明', 'sanming', 'OpenCarHomeService', 'OpenCarHomeService', 'sanming,350400,福建,三明,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3040', '龙岩', 'longyan', 'OpenCarHomeService', 'OpenCarHomeService', 'longyan,350800,福建,龙岩,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3041', '南平', 'nanping', 'OpenCarHomeService', 'OpenCarHomeService', 'nanping,350700,福建,南平,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3042', '宁德', 'ningde', 'OpenCarHomeService', 'OpenCarHomeService', 'ningde,350900,福建,宁德,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3043', '杭州', 'hangzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'hangzhou,330100,浙江,杭州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3044', '宁波', 'ningbo', 'OpenCarHomeService', 'OpenCarHomeService', 'ningbo,330200,浙江,宁波,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3045', '温州', 'wenzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'wenzhou,330300,浙江,温州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3046', '台州', 'tai1zhou', 'OpenCarHomeService', 'OpenCarHomeService', 'tai1zhou,331000,浙江,台州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3047', '金华', 'jinhua', 'OpenCarHomeService', 'OpenCarHomeService', 'jinhua,330700,浙江,金华,0,3', '3', '0');
INSERT INTO `city_source_config` VALUES ('3048', '湖州', 'huzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'huzhou,330500,浙江,湖州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3049', '丽水', 'lishui', 'OpenCarHomeService', 'OpenCarHomeService', 'lishui,331100,浙江,丽水,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3050', '衢州', 'quzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'quzhou,330800,浙江,衢州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3051', '舟山', 'zhoushan', 'OpenCarHomeService', 'OpenCarHomeService', 'zhoushan,330900,浙江,舟山,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3052', '宜昌', 'yichang', 'OpenCarHomeService', 'OpenCarHomeService', 'yichang,420500,湖北,宜昌,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3053', '荆州', 'jingzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'jingzhou,421000,湖北,荆州,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3054', '黄冈', 'huanggang', 'OpenCarHomeService', 'OpenCarHomeService', 'huanggang,421100,湖北,黄冈,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3055', '十堰', 'shiyan', 'OpenCarHomeService', 'OpenCarHomeService', 'shiyan,420300,湖北,十堰,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3056', '黄石', 'huangshi', 'OpenCarHomeService', 'OpenCarHomeService', 'huangshi,420200,湖北,黄石,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3057', '随州', 'suizhou', 'OpenCarHomeService', 'OpenCarHomeService', 'suizhou,421300,湖北,随州,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3058', '荆门', 'jingmen', 'OpenCarHomeService', 'OpenCarHomeService', 'jingmen,420800,湖北,荆门,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3059', '孝感', 'xiaogan', 'OpenCarHomeService', 'OpenCarHomeService', 'xiaogan,420900,湖北,孝感,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3060', '鄂州', 'ezhou', 'OpenCarHomeService', 'OpenCarHomeService', 'ezhou,420700,湖北,鄂州,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3061', '咸宁', 'xianning', 'OpenCarHomeService', 'OpenCarHomeService', 'xianning,421200,湖北,咸宁,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3062', '恩施', 'enshi', 'OpenCarHomeService', 'OpenCarHomeService', 'enshi,422800,湖北,恩施,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3063', '神农架', 'shennongjia', 'OpenCarHomeService', 'OpenCarHomeService', 'shennongjia,423400,湖北,神农架,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3064', '潜江', 'qianjiang', 'OpenCarHomeService', 'OpenCarHomeService', 'qianjiang,423200,湖北,潜江,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3065', '天门', 'tianmen', 'OpenCarHomeService', 'OpenCarHomeService', 'tianmen,423300,湖北,天门,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3066', '仙桃', 'xiantao', 'OpenCarHomeService', 'OpenCarHomeService', 'xiantao,423100,湖北,仙桃,0,5', '3', '0');
INSERT INTO `city_source_config` VALUES ('3080', '重庆', 'chongqing', 'OpenCarHomeService', 'OpenCarHomeService', 'chongqing,500100,重庆,重庆,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3081', '玉溪', 'yuxi', 'OpenCarHomeService', 'OpenCarHomeService', 'yuxi,530400,云南,玉溪,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3082', '保山', 'baoshan', 'OpenCarHomeService', 'OpenCarHomeService', 'baoshan,530500,云南,保山,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3083', '曲靖', 'qujing', 'OpenCarHomeService', 'OpenCarHomeService', 'qujing,530300,云南,曲靖,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3084', '红河', 'honghe', 'OpenCarHomeService', 'OpenCarHomeService', 'honghe,532500,云南,红河,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3085', '丽江', 'lijiang', 'OpenCarHomeService', 'OpenCarHomeService', 'lijiang,530700,云南,丽江,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3086', '昭通', 'zhaotong', 'OpenCarHomeService', 'OpenCarHomeService', 'zhaotong,530600,云南,昭通,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3087', '普洱', 'puer', 'OpenCarHomeService', 'OpenCarHomeService', 'puer,530800,云南,普洱,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3088', '临沧', 'lincang', 'OpenCarHomeService', 'OpenCarHomeService', 'lincang,530900,云南,临沧,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3089', '大理', 'dali', 'OpenCarHomeService', 'OpenCarHomeService', 'dali,532900,云南,大理,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3090', '迪庆', 'diqing', 'OpenCarHomeService', 'OpenCarHomeService', 'diqing,533400,云南,迪庆,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3091', '楚雄', 'chuxiong', 'OpenCarHomeService', 'OpenCarHomeService', 'chuxiong,532300,云南,楚雄,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3092', '西双版纳', 'xishuangbanna', 'OpenCarHomeService', 'OpenCarHomeService', 'xishuangbanna,532800,云南,西双版纳,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3093', '文山', 'wenshan', 'OpenCarHomeService', 'OpenCarHomeService', 'wenshan,532600,云南,文山,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3094', '德宏', 'dehong', 'OpenCarHomeService', 'OpenCarHomeService', 'dehong,533100,云南,德宏,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3095', '怒江', 'nujiang', 'OpenCarHomeService', 'OpenCarHomeService', 'nujiang,533300,云南,怒江,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3096', '北京', 'beijing', 'OpenCarHomeService', 'OpenCarHomeService', 'beijing,110100,北京,北京,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3098', '兰州', 'lanzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'lanzhou,620100,甘肃,兰州,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3099', '酒泉', 'jiuquan', 'OpenCarHomeService', 'OpenCarHomeService', 'jiuquan,620900,甘肃,酒泉,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3100', '天水', 'tianshui', 'OpenCarHomeService', 'OpenCarHomeService', 'tianshui,620500,甘肃,天水,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3101', '张掖', 'zhangye', 'OpenCarHomeService', 'OpenCarHomeService', 'zhangye,620700,甘肃,张掖,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3102', '白银', 'baiyin', 'OpenCarHomeService', 'OpenCarHomeService', 'baiyin,620400,甘肃,白银,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3103', '庆阳', 'qingyang', 'OpenCarHomeService', 'OpenCarHomeService', 'qingyang,621000,甘肃,庆阳,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3104', '嘉峪关', 'jiayuguan', 'OpenCarHomeService', 'OpenCarHomeService', 'jiayuguan,620200,甘肃,嘉峪关,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3105', '武威', 'wuwei', 'OpenCarHomeService', 'OpenCarHomeService', 'wuwei,620600,甘肃,武威,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3106', '平凉', 'pingliang', 'OpenCarHomeService', 'OpenCarHomeService', 'pingliang,620800,甘肃,平凉,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3107', '金昌', 'jinchang', 'OpenCarHomeService', 'OpenCarHomeService', 'jinchang,620300,甘肃,金昌,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3108', '甘南', 'gannan', 'OpenCarHomeService', 'OpenCarHomeService', 'gannan,623000,甘肃,甘南,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3109', '临夏', 'linxia', 'OpenCarHomeService', 'OpenCarHomeService', 'linxia,622900,甘肃,临夏,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3110', '陇南', 'longnan', 'OpenCarHomeService', 'OpenCarHomeService', 'longnan,621200,甘肃,陇南,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3111', '定西', 'dingxi', 'OpenCarHomeService', 'OpenCarHomeService', 'dingxi,621100,甘肃,定西,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3112', '石家庄', 'shijiazhuang', 'OpenCarHomeService', 'OpenCarHomeService', 'shijiazhuang,130100,河北,石家庄,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3113', '唐山', 'tangshan', 'OpenCarHomeService', 'OpenCarHomeService', 'tangshan,130200,河北,唐山,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3114', '保定', 'baoding', 'OpenCarHomeService', 'OpenCarHomeService', 'baoding,130600,河北,保定,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3115', '邯郸', 'handan', 'OpenCarHomeService', 'OpenCarHomeService', 'handan,130400,河北,邯郸,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3116', '沧州', 'cangzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'cangzhou,130900,河北,沧州,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3117', '廊坊', 'langfang', 'OpenCarHomeService', 'OpenCarHomeService', 'langfang,131000,河北,廊坊,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3118', '承德', 'chengde', 'OpenCarHomeService', 'OpenCarHomeService', 'chengde,130800,河北,承德,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3119', '秦皇岛', 'qinhuangdao', 'OpenCarHomeService', 'OpenCarHomeService', 'qinhuangdao,130300,河北,秦皇岛,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3120', '邢台', 'xingtai', 'OpenCarHomeService', 'OpenCarHomeService', 'xingtai,130500,河北,邢台,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3121', '衡水', 'hengshui', 'OpenCarHomeService', 'OpenCarHomeService', 'hengshui,131100,河北,衡水,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3122', '张家口', 'zhangjiakou', 'OpenCarHomeService', 'OpenCarHomeService', 'zhangjiakou,130700,河北,张家口,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3123', '长春', 'changchun', 'OpenCarHomeService', 'OpenCarHomeService', 'changchun,220100,吉林,长春,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3124', '吉林', 'jilin', 'OpenCarHomeService', 'OpenCarHomeService', 'jilin,220200,吉林,吉林,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3125', '四平', 'siping', 'OpenCarHomeService', 'OpenCarHomeService', 'siping,220300,吉林,四平,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3126', '通化', 'tonghua', 'OpenCarHomeService', 'OpenCarHomeService', 'tonghua,220500,吉林,通化,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3127', '白山', 'baishan', 'OpenCarHomeService', 'OpenCarHomeService', 'baishan,220600,吉林,白山,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3128', '辽源', 'liaoyuan', 'OpenCarHomeService', 'OpenCarHomeService', 'liaoyuan,220400,吉林,辽源,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3129', '松原', 'songyuan', 'OpenCarHomeService', 'OpenCarHomeService', 'songyuan,220700,吉林,松原,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3130', '白城', 'baicheng', 'OpenCarHomeService', 'OpenCarHomeService', 'baicheng,220800,吉林,白城,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3131', '延边', 'yanbian', 'OpenCarHomeService', 'OpenCarHomeService', 'yanbian,222400,吉林,延边,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3132', '广州', 'guangzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'guangzhou,440100,广东,广州,4,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3133', '惠州', 'huizhou', 'OpenCarHomeService', 'OpenCarHomeService', 'huizhou,441300,广东,惠州,4,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3134', '银川', 'yinchuan', 'OpenCarHomeService', 'OpenCarHomeService', 'yinchuan,640100,宁夏,银川,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3135', '吴忠', 'wuzhong', 'OpenCarHomeService', 'OpenCarHomeService', 'wuzhong,640300,宁夏,吴忠,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3136', '石嘴山', 'shizuishan', 'OpenCarHomeService', 'OpenCarHomeService', 'shizuishan,640200,宁夏,石嘴山,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3137', '固原', 'guyuan', 'OpenCarHomeService', 'OpenCarHomeService', 'guyuan,640400,宁夏,固原,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3138', '中卫', 'zhongwei', 'OpenCarHomeService', 'OpenCarHomeService', 'zhongwei,640500,宁夏,中卫,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3139', '合肥', 'hefei', 'OpenCarHomeService', 'OpenCarHomeService', 'hefei,340100,安徽,合肥,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3140', '芜湖', 'wuhu', 'OpenCarHomeService', 'OpenCarHomeService', 'wuhu,340200,安徽,芜湖,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3141', '阜阳', 'fuyang', 'OpenCarHomeService', 'OpenCarHomeService', 'fuyang,341200,安徽,阜阳,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3142', '黄山', 'huangshan', 'OpenCarHomeService', 'OpenCarHomeService', 'huangshan,341000,安徽,黄山,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3143', '蚌埠', 'bengbu', 'OpenCarHomeService', 'OpenCarHomeService', 'bengbu,340300,安徽,蚌埠,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3144', '安庆', 'anqing', 'OpenCarHomeService', 'OpenCarHomeService', 'anqing,340800,安徽,安庆,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3145', '马鞍山', 'maanshan', 'OpenCarHomeService', 'OpenCarHomeService', 'maanshan,340500,安徽,马鞍山,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3146', '亳州', 'bozhou', 'OpenCarHomeService', 'OpenCarHomeService', 'bozhou,341600,安徽,亳州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3147', '滁州', 'chuzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'chuzhou,341100,安徽,滁州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3148', '铜陵', 'tongling', 'OpenCarHomeService', 'OpenCarHomeService', 'tongling,340700,安徽,铜陵,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3149', '淮南', 'huainan', 'OpenCarHomeService', 'OpenCarHomeService', 'huainan,340400,安徽,淮南,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3150', '淮北', 'huaibei', 'OpenCarHomeService', 'OpenCarHomeService', 'huaibei,340600,安徽,淮北,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3151', '六安', 'liuan', 'OpenCarHomeService', 'OpenCarHomeService', 'liuan,341500,安徽,六安,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3152', '巢湖', 'chaohu', 'OpenCarHomeService', 'OpenCarHomeService', 'chaohu,341400,安徽,巢湖,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3153', '宿州', 'su4zhou', 'OpenCarHomeService', 'OpenCarHomeService', 'su4zhou,341300,安徽,宿州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3154', '宣城', 'xuancheng', 'OpenCarHomeService', 'OpenCarHomeService', 'xuancheng,341800,安徽,宣城,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3155', '池州', 'chizhou', 'OpenCarHomeService', 'OpenCarHomeService', 'chizhou,341700,安徽,池州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3156', '上海', 'shanghai', 'OpenCarHomeService', 'OpenCarHomeService', 'shanghai,310100,上海,上海,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3157', '太原', 'taiyuan', 'OpenCarHomeService', 'OpenCarHomeService', 'taiyuan,140100,山西,太原,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3158', '大同', 'datong', 'OpenCarHomeService', 'OpenCarHomeService', 'datong,140200,山西,大同,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3159', '运城', 'yuncheng', 'OpenCarHomeService', 'OpenCarHomeService', 'yuncheng,140800,山西,运城,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3160', '长治', 'changzhi', 'OpenCarHomeService', 'OpenCarHomeService', 'changzhi,140400,山西,长治,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3161', '临汾', 'linfen', 'OpenCarHomeService', 'OpenCarHomeService', 'linfen,141000,山西,临汾,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3162', '晋城', 'jincheng', 'OpenCarHomeService', 'OpenCarHomeService', 'jincheng,140500,山西,晋城,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3163', '阳泉', 'yangquan', 'OpenCarHomeService', 'OpenCarHomeService', 'yangquan,140300,山西,阳泉,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3164', '忻州', 'xinzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'xinzhou,140900,山西,忻州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3165', '晋中', 'jinzhong', 'OpenCarHomeService', 'OpenCarHomeService', 'jinzhong,140700,山西,晋中,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3166', '朔州', 'shuozhou', 'OpenCarHomeService', 'OpenCarHomeService', 'shuozhou,140600,山西,朔州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3167', '吕梁', 'lvliang', 'OpenCarHomeService', 'OpenCarHomeService', 'lvliang,141100,山西,吕梁,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3168', '海口', 'haikou', 'OpenCarHomeService', 'OpenCarHomeService', 'haikou,460100,海南,海口,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3169', '三亚', 'sanya', 'OpenCarHomeService', 'OpenCarHomeService', 'sanya,460200,海南,三亚,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3170', '陵水', 'lingshui', 'OpenCarHomeService', 'OpenCarHomeService', 'lingshui,462400,海南,陵水,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3171', '白沙', 'baisha', 'OpenCarHomeService', 'OpenCarHomeService', 'baisha,462100,海南,白沙,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3172', '琼海', 'qionghai', 'OpenCarHomeService', 'OpenCarHomeService', 'qionghai,461200,海南,琼海,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3173', '琼中', 'qiongzhong', 'OpenCarHomeService', 'OpenCarHomeService', 'qiongzhong,462600,海南,琼中,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3174', '澄迈县', 'chengmaixian', 'OpenCarHomeService', 'OpenCarHomeService', 'chengmaixian,461900,海南,澄迈县,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3175', '昌江', 'changjiang', 'OpenCarHomeService', 'OpenCarHomeService', 'changjiang,462200,海南,昌江,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3176', '文昌', 'wenchang', 'OpenCarHomeService', 'OpenCarHomeService', 'wenchang,461400,海南,文昌,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3177', '屯昌县', 'tunchangxian', 'OpenCarHomeService', 'OpenCarHomeService', 'tunchangxian,461800,海南,屯昌县,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3178', '定安县', 'dinganxian', 'OpenCarHomeService', 'OpenCarHomeService', 'dinganxian,461700,海南,定安县,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3179', '儋州', 'danzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'danzhou,461300,海南,儋州,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3180', '保亭', 'baoting', 'OpenCarHomeService', 'OpenCarHomeService', 'baoting,462500,海南,保亭,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3181', '五指山', 'wuzhishan', 'OpenCarHomeService', 'OpenCarHomeService', 'wuzhishan,461100,海南,五指山,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3182', '乐东', 'ledong', 'OpenCarHomeService', 'OpenCarHomeService', 'ledong,462300,海南,乐东,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3183', '临高县', 'lingaoxian', 'OpenCarHomeService', 'OpenCarHomeService', 'lingaoxian,462000,海南,临高县,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3184', '东方', 'dongfang', 'OpenCarHomeService', 'OpenCarHomeService', 'dongfang,461600,海南,东方,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3185', '万宁', 'wanning', 'OpenCarHomeService', 'OpenCarHomeService', 'wanning,461500,海南,万宁,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3186', '沈阳', 'shenyang', 'OpenCarHomeService', 'OpenCarHomeService', 'shenyang,210100,辽宁,沈阳,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3187', '大连', 'dalian', 'OpenCarHomeService', 'OpenCarHomeService', 'dalian,210200,辽宁,大连,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3188', '鞍山', 'anshan', 'OpenCarHomeService', 'OpenCarHomeService', 'anshan,210300,辽宁,鞍山,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3189', '锦州', 'jinzhou', 'OpenCarHomeService', 'OpenCarHomeService', 'jinzhou,210700,辽宁,锦州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3190', '抚顺', 'fushun', 'OpenCarHomeService', 'OpenCarHomeService', 'fushun,210400,辽宁,抚顺,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3191', '辽阳', 'liaoyang', 'OpenCarHomeService', 'OpenCarHomeService', 'liaoyang,211000,辽宁,辽阳,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3192', '营口', 'yingkou', 'OpenCarHomeService', 'OpenCarHomeService', 'yingkou,210800,辽宁,营口,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3193', '阜新', 'fuxin', 'OpenCarHomeService', 'OpenCarHomeService', 'fuxin,210900,辽宁,阜新,-1,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3194', '葫芦岛', 'huludao', 'OpenCarHomeService', 'OpenCarHomeService', 'huludao,211400,辽宁,葫芦岛,0,-1', '3', '0');
INSERT INTO `city_source_config` VALUES ('3195', '朝阳', 'zhaoyang', 'OpenCarHomeService', 'OpenCarHomeService', 'zhaoyang,211300,辽宁,朝阳,0,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3196', '盘锦', 'panjin', 'OpenCarHomeService', 'OpenCarHomeService', 'panjin,211100,辽宁,盘锦,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3200', '新乡', 'xinxiang', 'OpenCarHomeService', 'OpenCarHomeService', 'xinxiang,410700,河南,新乡,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3201', '开封', 'kaifeng', 'OpenCarHomeService', 'OpenCarHomeService', 'kaifeng,410200,河南,开封,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3202', '三门峡', 'sanmenxia', 'OpenCarHomeService', 'OpenCarHomeService', 'sanmenxia,411200,河南,三门峡,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3203', '周口', 'zhoukou', 'OpenCarHomeService', 'OpenCarHomeService', 'zhoukou,411600,河南,周口,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3204', '济源', 'jiyuan', 'OpenCarHomeService', 'OpenCarHomeService', 'jiyuan,419000,河南,济源,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3205', '济南', 'jinan', 'OpenCarHomeService', 'OpenCarHomeService', 'jinan,370100,山东,济南,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3206', '潍坊', 'weifang', 'OpenCarHomeService', 'OpenCarHomeService', 'weifang,370700,山东,潍坊,0,17', '3', '0');
INSERT INTO `city_source_config` VALUES ('3207', '无锡', 'wuxi', 'OpenCarHomeService', 'OpenCarHomeService', 'wuxi,320200,江苏,无锡,4,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3208', '乌鲁木齐', 'wulumuqi', 'OpenCarHomeService', 'OpenCarHomeService', 'wulumuqi,650100,新疆,乌鲁木齐,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3209', '巴音郭楞', 'bayinguoleng', 'OpenCarHomeService', 'OpenCarHomeService', 'bayinguoleng,652800,新疆,巴音郭楞,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3210', '伊犁', 'yili', 'OpenCarHomeService', 'OpenCarHomeService', 'yili,654000,新疆,伊犁,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3211', '克拉玛依', 'kelamayi', 'OpenCarHomeService', 'OpenCarHomeService', 'kelamayi,650200,新疆,克拉玛依,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3212', '阿克苏', 'akesu', 'OpenCarHomeService', 'OpenCarHomeService', 'akesu,652900,新疆,阿克苏,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3213', '喀什', 'kashi', 'OpenCarHomeService', 'OpenCarHomeService', 'kashi,653100,新疆,喀什,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3214', '哈密', 'hami', 'OpenCarHomeService', 'OpenCarHomeService', 'hami,652200,新疆,哈密,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3215', '和田', 'hetian', 'OpenCarHomeService', 'OpenCarHomeService', 'hetian,653200,新疆,和田,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3216', '昌吉', 'changji', 'OpenCarHomeService', 'OpenCarHomeService', 'changji,652300,新疆,昌吉,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3217', '吐鲁番', 'tulufan', 'OpenCarHomeService', 'OpenCarHomeService', 'tulufan,652100,新疆,吐鲁番,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3218', '阿勒泰', 'aletai', 'OpenCarHomeService', 'OpenCarHomeService', 'aletai,654300,新疆,阿勒泰,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3219', '塔城', 'tacheng', 'OpenCarHomeService', 'OpenCarHomeService', 'tacheng,654200,新疆,塔城,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3220', '博尔塔拉', 'boertala', 'OpenCarHomeService', 'OpenCarHomeService', 'boertala,652700,新疆,博尔塔拉,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3221', '克孜勒苏', 'kezilesu', 'OpenCarHomeService', 'OpenCarHomeService', 'kezilesu,653000,新疆,克孜勒苏,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3222', '石河子', 'shihezi', 'OpenCarHomeService', 'OpenCarHomeService', 'shihezi,655100,新疆,石河子,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3223', '绍兴县', 'shaoxingxian', 'OpenCarHomeService', 'OpenCarHomeService', 'shaoxingxian,330600,浙江,绍兴县,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3224', '嘉兴', 'jiaxing', 'OpenCarHomeService', 'OpenCarHomeService', 'jiaxing,330400,浙江,嘉兴,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3225', '那曲', 'naqu', 'OpenCarHomeService', 'OpenCarHomeService', 'naqu,542400,西藏,那曲,0,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3226', '昆明', 'kunming', 'OpenCarHomeService', 'OpenCarHomeService', 'kunming,530100,云南,昆明,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3227', '深圳', 'shenzhen', 'OpenCarHomeService', 'OpenCarHomeService', 'shenzhen,440300,广东,深圳,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3228', '中山', 'zhongshan', 'OpenCarHomeService', 'OpenCarHomeService', 'zhongshan,442000,广东,中山,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3229', '珠海', 'zhuhai', 'OpenCarHomeService', 'OpenCarHomeService', 'zhuhai,440400,广东,珠海,4,0', '3', '0');
INSERT INTO `city_source_config` VALUES ('3230', '潮州', 'chaozhou', 'OpenCarHomeService', 'OpenCarHomeService', 'chaozhou,445100,广东,潮州,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('3231', '益阳', 'yiyang', 'OpenCarHomeService', 'OpenCarHomeService', 'yiyang,430900,湖南,益阳,0,4', '3', '0');
INSERT INTO `city_source_config` VALUES ('3232', '乌海', 'wuhai', 'OpenCarHomeService', 'OpenCarHomeService', 'wuhai,150300,内蒙古,乌海,0,6', '3', '0');
INSERT INTO `city_source_config` VALUES ('9999', '北京', 'beijing', '北京市交管网', 'WxBeijingService', '', '6', '1');

-- ----------------------------
-- Table structure for drivers_user
-- ----------------------------
DROP TABLE IF EXISTS `drivers_user`;
CREATE TABLE `drivers_user` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '省份',
  `city` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '城市',
  `citypinyin` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '城市拼音',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `create_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `modf_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '修改时间',
  `query_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '最近查询时间',
  `query_sum` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '查询次数',
  `product_iden` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '产品标识',
  `phone_imei` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '手机标识',
  `phone_model` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '手机类型',
  `phone_system` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '手机系统',
  PRIMARY KEY (`userid`),
  KEY `PROVINCE_A` (`province`),
  KEY `CITY_A` (`city`),
  KEY `license_a` (`driver_license`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of drivers_user
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_1
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_1`;
CREATE TABLE `source_request_log_1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_1
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_10
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_10`;
CREATE TABLE `source_request_log_10` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_10
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_11
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_11`;
CREATE TABLE `source_request_log_11` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_11
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_12
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_12`;
CREATE TABLE `source_request_log_12` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_12
-- ----------------------------
INSERT INTO `source_request_log_12` VALUES ('2', '北京', '', '北京', 'beijing', '北京交管网', '赵峰', '110102198403201553', '2007-08-02', '选填', '', '', 'ok', '', '2015-12-03 16:58:50');
INSERT INTO `source_request_log_12` VALUES ('3', '河北', '', '张家口', 'zhangjiakou', '河北省交管网', '选填', '131182199110102018', '选填', '130700762435', '', '', 'ok', '', '2015-12-03 16:58:50');
INSERT INTO `source_request_log_12` VALUES ('4', '河北', '', '张家口', 'zhangjiakou', '河北省交管网', '选填', '131182199110102018', '选填', '130700762435', '', '', 'ok', '', '2015-12-28 09:46:42');
INSERT INTO `source_request_log_12` VALUES ('5', '河北', '', '张家口', 'zhangjiakou', '河北省交管网', '选填', '131182199110102018', '选填', '130700762435', '', '', 'ok', '', '2015-12-28 09:46:42');
INSERT INTO `source_request_log_12` VALUES ('6', '河北', '', '张家口', 'zhangjiakou', '河北省交管网', '选填', '131182199110102018', '选填', '130700762435', '', '', 'ok', '', '2015-12-28 09:46:42');

-- ----------------------------
-- Table structure for source_request_log_2
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_2`;
CREATE TABLE `source_request_log_2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_2
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_3
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_3`;
CREATE TABLE `source_request_log_3` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_3
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_4
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_4`;
CREATE TABLE `source_request_log_4` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_4
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_5
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_5`;
CREATE TABLE `source_request_log_5` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_5
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_6
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_6`;
CREATE TABLE `source_request_log_6` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_6
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_7
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_7`;
CREATE TABLE `source_request_log_7` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_7
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_8
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_8`;
CREATE TABLE `source_request_log_8` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_8
-- ----------------------------

-- ----------------------------
-- Table structure for source_request_log_9
-- ----------------------------
DROP TABLE IF EXISTS `source_request_log_9`;
CREATE TABLE `source_request_log_9` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份名称',
  `pro` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '省份简称',
  `city` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '城市名称',
  `citypy` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '城市拼音',
  `source` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT '数据源名称',
  `driver_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶人姓名',
  `driver_license` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证号',
  `lissue_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '初次领证日期',
  `lissue_archive` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证档案编号',
  `is_effective` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证是否长期有效',
  `effective_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '驾驶证有效截止日期',
  `status` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '请求状态',
  `result` text COLLATE utf8_bin COMMENT '响应结果',
  `req_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`),
  KEY `PROVINCE` (`province`),
  KEY `CITY` (`city`),
  KEY `REQTIME` (`req_time`),
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin PACK_KEYS=0;

-- ----------------------------
-- Records of source_request_log_9
-- ----------------------------
