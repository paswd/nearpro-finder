<?php
require_once('services/db.php');


class Property {
	private $db;

	function setGlobalParams() {
		require('config/main.php');
		$this->globalParams = $GLOBAL_PARAMS;
		$this->errorList = $ERROR_LIST;
	}

	function __construct() {
		$this->setGlobalParams();
		$this->db = new DB;
	}

	function getDistanceSql($_lat, $_lng) {
		$latDb = '`lat`';
		$lngDb = '`lng`';

		//$latDbAngle = 55.849490496203096;
		//$lngDbAngle = 37.51476265490055;
		//$latDb = '('.$latDbAngle.' * PI() / 180)';
		//$lngDb = '('.$lngDbAngle.' * PI() / 180)';

		$lat = '('.$_lat.' * PI() / 180)';
		$lng = '('.$_lng.' * PI() / 180)';

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

		$angularDiff = 'atan('.$numerator.' / '.$denominator.')';

		return '('.$angularDiff.' * '.$this->globalParams['geo']['earth_radius'].')';
	}

	function get($isLocality = false, $lat = 0, $lng = 0, $radius = 0) {
		$this->db->connect();
		//$res = $this->db->query('SELECT * FROM `property_types`');


		$where = '';

		if ($isLocality && $radius > 0) {
			$dist = $this->getDistanceSql($lat, $lng);
			$where = ' WHERE '.$dist.' < '.$radius;
		}

		//die('SELECT '.$where.' as `result`');

		//die('SELECT * FROM `property`'.$where);

		$res = $this->db->query('SELECT * FROM `property`'.$where);
	

		while ($row = mysqli_fetch_object($res)) {
			$data = [
				'id' => $row->id,
				'name' => $row->name,
				'price' => $row->price,
				'type' => $row->type,
				'img_src' => $row->img_src,
				'lat' => $row->lat,
				'lng' => $row->lng,
				'region' => $row->region,
				'address' => $row->address,
				'description' => $row->description 
			];
			$list[] = $data;
		}
		$this->db->close();
		//echo count($list);
		//die();

		//$this->db->close();

		return $list;
	}
}