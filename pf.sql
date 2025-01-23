-- --------------------------------------------------------
-- 主機:                           127.0.0.1
-- 伺服器版本:                        10.11.2-MariaDB - mariadb.org binary distribution
-- 伺服器作業系統:                      Win64
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 正在傾印表格  pf.access 的資料：~2 rows (近似值)
/*!40000 ALTER TABLE `access` DISABLE KEYS */;
REPLACE INTO `access` (`access_id`, `username`, `access`, `password`, `update_time`) VALUES
	(1, 'admin', 'admin', '0988771596', '2025-01-19 17:57:52'),
	(2, 'user', 'user', 'user', '2025-01-19 17:57:23');
/*!40000 ALTER TABLE `access` ENABLE KEYS */;

-- 正在傾印表格  pf.alert_record_aio 的資料：~1 rows (近似值)
/*!40000 ALTER TABLE `alert_record_aio` DISABLE KEYS */;
REPLACE INTO `alert_record_aio` (`alert_id`, `point_id`, `alert_value`, `start_time`, `return_time`, `update_time`) VALUES
	(1, 1, 90.00, '2025-01-21 17:38:38', NULL, '2025-01-21 17:38:38');
/*!40000 ALTER TABLE `alert_record_aio` ENABLE KEYS */;

-- 正在傾印表格  pf.alert_record_dio 的資料：~1 rows (近似值)
/*!40000 ALTER TABLE `alert_record_dio` DISABLE KEYS */;
REPLACE INTO `alert_record_dio` (`alert_id`, `point_id`, `alert_status`, `start_time`, `return_time`, `update_time`) VALUES
	(2, 7, 'ON', '2025-01-21 17:43:12', '2025-01-21 17:49:53', '2025-01-21 17:49:55');
/*!40000 ALTER TABLE `alert_record_dio` ENABLE KEYS */;

-- 正在傾印表格  pf.alert_set_aio 的資料：~4 rows (近似值)
/*!40000 ALTER TABLE `alert_set_aio` DISABLE KEYS */;
REPLACE INTO `alert_set_aio` (`set_id`, `point_id`, `alert_status`, `alert_upper`, `alert_lower`, `update_time`) VALUES
	(1, 1, 'OFF', 20.00, 40.00, '2025-01-22 16:03:39'),
	(2, 2, 'ON', 80.00, 30.00, '2025-01-19 18:14:15'),
	(3, 3, 'ON', 80.00, 30.00, '2025-01-19 18:14:23'),
	(4, 4, 'ON', 80.00, 30.00, '2025-01-19 18:14:31');
/*!40000 ALTER TABLE `alert_set_aio` ENABLE KEYS */;

-- 正在傾印表格  pf.alert_set_dio 的資料：~2 rows (近似值)
/*!40000 ALTER TABLE `alert_set_dio` DISABLE KEYS */;
REPLACE INTO `alert_set_dio` (`set_id`, `point_id`, `alert_status`, `alert_define`, `update_time`) VALUES
	(1, 5, 'ON', 'OFF', '2025-01-22 15:57:36'),
	(2, 6, 'OFF', 'ON', '2025-01-19 18:14:52');
/*!40000 ALTER TABLE `alert_set_dio` ENABLE KEYS */;

-- 正在傾印表格  pf.device_info 的資料：~6 rows (近似值)
/*!40000 ALTER TABLE `device_info` DISABLE KEYS */;
REPLACE INTO `device_info` (`device_id`, `type_id`, `device_name`, `site_id`, `info`) VALUES
	(1, 1, 'sms-01', 1, NULL),
	(2, 1, 'sms-02', 1, NULL),
	(3, 1, 'sms-03', 2, NULL),
	(4, 1, 'sms-04', 3, NULL),
	(5, 2, 'es-01', 1, NULL),
	(6, 2, 'es-02', 2, NULL);
/*!40000 ALTER TABLE `device_info` ENABLE KEYS */;

-- 正在傾印表格  pf.device_type 的資料：~2 rows (近似值)
/*!40000 ALTER TABLE `device_type` DISABLE KEYS */;
REPLACE INTO `device_type` (`type_id`, `type_name`, `info`) VALUES
	(1, 'Soil Moisture Sensor', 'soil moisture sensor'),
	(2, 'Electric Sluice', NULL);
/*!40000 ALTER TABLE `device_type` ENABLE KEYS */;

-- 正在傾印表格  pf.gateway_info 的資料：~1 rows (近似值)
/*!40000 ALTER TABLE `gateway_info` DISABLE KEYS */;
REPLACE INTO `gateway_info` (`gateway_id`, `gateway_name`, `site_id`, `ip`, `port`) VALUES
	(1, 'IO Module', 1, '192.168.1.100', 502);
/*!40000 ALTER TABLE `gateway_info` ENABLE KEYS */;

-- 正在傾印表格  pf.modbus_point 的資料：~6 rows (近似值)
/*!40000 ALTER TABLE `modbus_point` DISABLE KEYS */;
REPLACE INTO `modbus_point` (`modbus_id`, `gateway_id`, `point_id`, `function`, `slave`, `address`, `format`) VALUES
	(1, 1, 1, 'holding', 1, 0, 'floatCDAB'),
	(2, 1, 2, 'holding', 1, 2, 'floatCDAB'),
	(3, 1, 3, 'holding', 1, 4, 'floatCDAB'),
	(4, 1, 4, 'holding', 1, 6, 'floatCDAB'),
	(5, 1, 5, 'coil', 1, 10, 'signed'),
	(6, 1, 6, 'coil', 1, 11, 'signed');
/*!40000 ALTER TABLE `modbus_point` ENABLE KEYS */;

-- 正在傾印表格  pf.point_info 的資料：~8 rows (近似值)
/*!40000 ALTER TABLE `point_info` DISABLE KEYS */;
REPLACE INTO `point_info` (`point_id`, `device_id`, `point_name`, `comm`, `io_type`, `arithmetic`, `correct`, `unit`) VALUES
	(1, 1, 'soil-01', 'modbus', 'AI', NULL, 0.00, '%'),
	(2, 2, 'soil-02', 'modbus', 'AI', NULL, 0.00, '%'),
	(3, 3, 'soil-03', 'modbus', 'AI', NULL, 0.00, '%'),
	(4, 4, 'soil-04', 'modbus', 'AI', NULL, 0.00, '%'),
	(5, 5, 'es-c-01', 'modbus', 'DO', NULL, 0.00, NULL),
	(6, 6, 'es-c-02', 'modbus', 'DO', NULL, 0.00, NULL),
	(7, 5, 'es-s-01', 'modbus', 'DI', NULL, 0.00, NULL),
	(8, 6, 'es-s-02', 'modbus', 'DI', NULL, 0.00, NULL);
/*!40000 ALTER TABLE `point_info` ENABLE KEYS */;

-- 正在傾印表格  pf.schedule_cycle_do 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `schedule_cycle_do` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_cycle_do` ENABLE KEYS */;

-- 正在傾印表格  pf.schedule_once_do 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `schedule_once_do` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_once_do` ENABLE KEYS */;

-- 正在傾印表格  pf.site_info 的資料：~5 rows (近似值)
/*!40000 ALTER TABLE `site_info` DISABLE KEYS */;
REPLACE INTO `site_info` (`site_id`, `site_name`) VALUES
	(1, '1F'),
	(2, '2F'),
	(3, '3F'),
	(4, '4F'),
	(5, 'RF');
/*!40000 ALTER TABLE `site_info` ENABLE KEYS */;

-- 正在傾印表格  pf_record_ai.1 的資料：~3 rows (近似值)
/*!40000 ALTER TABLE `1` DISABLE KEYS */;
REPLACE INTO `1` (`record_id`, `value`, `time`) VALUES
	(1, 20.00, '2025-01-22 10:00:00'),
	(2, 21.00, '2025-01-22 10:01:00'),
	(3, 22.00, '2025-01-22 10:02:00');
/*!40000 ALTER TABLE `1` ENABLE KEYS */;

-- 正在傾印表格  pf_record_ai.2 的資料：~1 rows (近似值)
/*!40000 ALTER TABLE `2` DISABLE KEYS */;
REPLACE INTO `2` (`record_id`, `value`, `time`) VALUES
	(1, 50.00, '2025-01-22 10:19:08');
/*!40000 ALTER TABLE `2` ENABLE KEYS */;

-- 正在傾印表格  pf_record_ai.3 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `3` DISABLE KEYS */;
/*!40000 ALTER TABLE `3` ENABLE KEYS */;

-- 正在傾印表格  pf_record_ai.4 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `4` DISABLE KEYS */;
/*!40000 ALTER TABLE `4` ENABLE KEYS */;

-- 正在傾印表格  pf_record_ai.value_record 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `value_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `value_record` ENABLE KEYS */;

-- 正在傾印表格  pf_record_ao.value_record 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `value_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `value_record` ENABLE KEYS */;

-- 正在傾印表格  pf_record_di.status_record 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `status_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `status_record` ENABLE KEYS */;

-- 正在傾印表格  pf_record_do.5 的資料：~1 rows (近似值)
/*!40000 ALTER TABLE `5` DISABLE KEYS */;
REPLACE INTO `5` (`record_id`, `status`, `time`) VALUES
	(1, 'ON', '2025-01-20 21:14:15');
/*!40000 ALTER TABLE `5` ENABLE KEYS */;

-- 正在傾印表格  pf_record_do.6 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `6` DISABLE KEYS */;
/*!40000 ALTER TABLE `6` ENABLE KEYS */;

-- 正在傾印表格  pf_record_do.status_record 的資料：~0 rows (近似值)
/*!40000 ALTER TABLE `status_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `status_record` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
