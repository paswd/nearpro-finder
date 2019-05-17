<?php
require_once('services/db.php');

class User {
	private $db;
	private $id;
	private $login;
	private $password;
	private $email;

	private $accessToken;
	

	private $globalParams;
	private $errorList;

	/*function __construct() {
		$this->login = '';
		$this->password = '';
		$this->email = '';
	}*/

	function __construct($_login, $_password, $_email) {
		require('config/main.php');
		$this->globalParams = $GLOBAL_PARAMS;
		$this->errorList = $ERROR_LIST;

		$this->login = $_login;
		$this->password = $_password;
		$this->email = $_email;

		$this->db = new DB;
	}

	function getLogin() {
		return $this->login;
	}

	function getPassword() {
		return $this->password;
	}

	function getEmail() {
		return $this->email;
	}


	function enctyptPassword() {
		return $this->password;
	}

	function createSession() {
		$this->db->connect();
		while (true) {
			$generated = md5(sha1(rand()));

			if (!$this->db->isExists('sessions', 'access_token', $generated)) {
				$this->db->query('INSERT INTO `sessions` VALUES(NULL, '.$this->id.', "'.$generated.'")');
				$this->accessToken = $generated;
				break;
			}
		}

		$this->db->query('INSERT INTO `sessions` VALUES(NULL, '.$this->id.', "'.$this->accessToken.'", "")');
		$this->db->close();
	}

	function isEmpty() {
		return empty($this->login) || empty($this->password) || empty($this->email);
	}

	function auth() {
		$this->db->connect();
		$res = $this->db->query('SELECT * FROM `users` WHERE `name`="'.$this->login.'"');
		$row = mysqli_fetch_object($res);

		if ($this->enctyptPassword() != $row->password) {
			return getRespond(false, 5, $this->errorList[5], NULL);
		}

		$this->id = $row->id;
		$this->email = $row->email;
		$this->db->close();

		$this->createSession();

		$res = [
			'session_token' => $this->accessToken
		];

		return getRespond(true, 0, '', $res);
	}

	function register() {
		if ($this->isEmpty()) {
			return getRespond(false, 3, $this->errorList[3], '');
		}

		$this->db->connect();

		if ($this->db->isExists('users', 'name', $this->login)) {
			$this->db->close();
			return getRespond(false, 4, $this->errorList[4], '');
		}

		$this->db->query('INSERT INTO `users` VALUES(
			NULL, "'.$this->login.'", "'.$this->enctyptPassword().'", "'.$this->email.'"
		)');
		$this->db->close();

		return $this->auth();
		//return getRespond(true, 0, '', $res);
	}
}