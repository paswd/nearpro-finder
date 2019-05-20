<?php
require_once('services/db.php');

class RegionList {
	private $db;

	function __construct() {
		$this->db = new DB;
	}

	function get($countryId) {
		$this->db->connect();
		$res = $this->db->query('SELECT * FROM `regions` WHERE `country_id`='.$countryId);

		$this->list = [];

		while ($row = mysqli_fetch_object($res)) {
			$data = [
				'id' => $row->id,
				'name' => $row->name
			];
			$this->list[] = $data;
		}

		$this->db->close();

		return $this->list;
	}

}