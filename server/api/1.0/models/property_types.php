<?php
require_once('services/db.php');

class PropertyTypes {
	private $db;

	function __construct() {
		$this->db = new DB;
	}

	function get() {
		$this->db->connect();
		$res = $this->db->query('SELECT * FROM `property_types`');

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