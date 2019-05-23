<?php
require_once('services/db.php');


class Property {
	private $db;
	private $globalParams;
	private $errorList;

	function setGlobalParams() {
		require('config/main.php');
		$this->globalParams = $GLOBAL_PARAMS;
		$this->errorList = $ERROR_LIST;
	}

	function __construct() {
		$this->setGlobalParams();
		$this->db = new DB;
	}

	function convertDegreeToRadianSql($angle) {
		return '('.$angle.' * PI() / 180)';
	}

	function getDistanceSql($_lat, $_lng) {
		$latDbAngle = '`lat`';
		$lngDbAngle = '`lng`';

		//$latDbAngle = 55.849490496203096;
		//$lngDbAngle = 37.51476265490055;
		$latDb = $this->convertDegreeToRadianSql($latDbAngle);
		$lngDb = $this->convertDegreeToRadianSql($lngDbAngle);

		$lat = $this->convertDegreeToRadianSql($_lat);
		$lng = $this->convertDegreeToRadianSql($_lng);

		$lngDiff = 'abs('.$lng.' - '.$lngDb.')';

		$numerator = 'sqrt(
				power(
						cos('.$latDb.') * sin('.$lngDiff.'),
						2.
				) + power(
						cos('.$lat.') * sin('.$latDb.') -
								sin('.$lat.') * cos('.$latDb.') * cos('.$lngDiff.'),
						2.
				)
		)';
		$denominator = '(sin('.$lat.') * sin('.$latDb.') +
				cos('.$lat.') * cos('.$latDb.') * cos('.$lngDiff.'))';

		$angularDiff = 'abs(atan('.$numerator.' / '.$denominator.'))';

		return '('.$angularDiff.' * '.$this->globalParams['geo']['earth_radius'].')';
	}

	function convertWhereToString($whereArr) {
		if (count($whereArr) == 0) {
			return '';
		}
		$res = ' WHERE ';
		$first = true;
		foreach ($whereArr as $value) {
			if (!$first) {
				$res .= ' AND ';
			}
			$res .= $value;
			$first = false;
		}

		return $res;
	}

	function get($country = 0, $region = 0, $type = 0, $priceMin = 0, $priceMax = 0,
			$isLocality = false, $lat = 0, $lng = 0, $radius = 0) {
		$this->db->connect();
		//$res = $this->db->query('SELECT * FROM `property_types`');

		$whereArr = [];
		$dist = '';
		$join = ' INNER JOIN (SELECT `id` AS `region_id`, `country_id` FROM `regions`) AS `regions_list` ON `property`.`region` = `regions_list`.`region_id`';
		$orderby = ' ORDER BY `id`';

		if ($country > 0) {
			$whereArr[] = '`country_id` = '.$country;
		}
		if ($region > 0) {
			$whereArr[] = '`region` = '.$region;
		}
		if ($type > 0) {
			$whereArr[] = '`type` = '.$type;
		}
		if ($priceMin > 0) {
			$whereArr[] = '`price` >= '.$priceMin;
		}
		if ($priceMax > 0) {
			$whereArr[] = '`price` <= '.$priceMax;
		}

		if ($isLocality) {
			$orderby = ' ORDER BY `distance`';
			$dist = $this->getDistanceSql($lat, $lng);
			$distList = '((SELECT `id` AS `property_id`, '.$dist.' AS `distance` FROM `property`) AS `dist_list`)';
			$join .= ' INNER JOIN '.$distList.' ON `dist_list`.`property_id` = `property`.`id`';
			
			if ($radius > 0) {
				$whereArr[] = ' `distance` < '.$radius;
			}
		}
		//die('SELECT * FROM `property` INNER JOIN '.$distList.' ON `dist_list`.`property_id` = `property`.`id`');
		//die('SELECT * FROM `property`'.$join.$where);

		//die('SELECT '.$where.' as `result`');

		//die('SELECT * FROM `property`'.$where);
		$where = $this->convertWhereToString($whereArr);
		//die('SELECT * FROM `property`'.$join.$where.$orderby);
		$res = $this->db->query('SELECT * FROM `property`'.$join.$where.$orderby);
	

		while ($row = mysqli_fetch_object($res)) {
			$data = [
				'id' => $row->id,
				'name' => $row->name,
				'price' => $row->price,
				'type' => $row->type,
				'img_src' => $this->globalParams['property_img_dir'].$row->img_src,
				'lat' => $row->lat,
				'lng' => $row->lng,
				'country' => $row->country_id,
				'region' => $row->region,
				'address' => $row->address,
				'description' => $row->description
			];
			if ($isLocality) {
				$data['distance'] = $row->distance;
			}
			$list[] = $data;
		}
		$this->db->close();
		//echo count($list);
		//die();

		//$this->db->close();

		return $list;
	}
	function getById($id) {
		$this->db->connect();
		//$res = $this->db->query('SELECT * FROM `property_types`');

		$join = ' INNER JOIN (SELECT `id` AS `region_id`, `country_id` FROM `regions`) AS `regions_list` ON `property`.`region` = `regions_list`.`region_id`';
		$where = ' WHERE `id` = '.$id;
		//die('SELECT * FROM `property`'.$join.$where);
		$res = $this->db->query('SELECT * FROM `property`'.$join.$where);

		if (mysqli_num_rows($res) == 0)  {
			return [];
		}

		$row = mysqli_fetch_object($res);
		return [
				'id' => $row->id,
				'name' => $row->name,
				'price' => $row->price,
				'type' => $row->type,
				'img_src' => $this->globalParams['property_img_dir'].$row->img_src,
				'lat' => $row->lat,
				'lng' => $row->lng,
				'country' => $row->country_id,
				'region' => $row->region,
				'address' => $row->address,
				'description' => $row->description
			];
	}
}