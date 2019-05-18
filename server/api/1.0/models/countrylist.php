<?php
require_once('services/db.php');
require_once('models/regionlist.php');

class CountryList {
	private $db;
	private $regionList;

	function __construct() {
		$this->db = new DB;
		$this->regionList = new RegionList;
	}

	function get() {
		$this->db->connect();
		$res = $this->db->query('SELECT * FROM `countries`');

		$this->list = [];

		while ($row = mysqli_fetch_object($res)) {
			$this->list[] = [
				'id' => $row->id,
				'name' => $row->name,
				'code' => $row->code,
				'regions' => $this->regionList->get($row->id)
			];;
		}

		$this->db->close();
		/*$this->list = [];

		$this->list[] = [
			'test1' => 'test1'
		];
		$this->list[] = [
			'test2' => 'test2'
		];*/

		return $this->list;
	}
}