<?php
//require_once('config/main.php');
require_once('services/respond.php');

class DB {
	private $connection;

	private $host;
	private $database;
	private $user;
	private $password;

	function __construct() {
		require('config/db.php');

		$this->host = $db_host;
		$this->database = $db_name;
		$this->user = $db_user;
		$this->password = $db_password;
	}

	function connect() {
		$this->connection = mysqli_connect(
			$this->host,
			$this->user,
			$this->password,
			$this->database
		);
	}

	function close() {
		mysqli_close($this->connection);
	}

	function query($query) {
		return mysqli_query($this->connection, $query);
	}

	function isExists($table, $field, $value) {
		//die('SELECT * FROM `'.$table.'` WHERE `'.$field.'`="'.$value.'"');
		$res = $this->query('SELECT * FROM `'.$table.'` WHERE `'.$field.'`="'.$value.'"');
		//die($res);
		return mysqli_num_rows($res) > 0;
	}
}