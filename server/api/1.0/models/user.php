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

	function setGlobalParams() {
		require('config/main.php');
		$this->globalParams = $GLOBAL_PARAMS;
		$this->errorList = $ERROR_LIST;
	}

	function __construct() {
		$this->setGlobalParams();
		$this->db = new DB;
	}

	function setUserData($_login, $_password, $_email) {
		$this->login = $_login;
		$this->password = $_password;
		$this->email = $_email;

		$this->db->connect();

		if (!$this->db->isExists('users', 'name', $this->login)) {
			die(getRespond(false, 5, $this->errorList[5], NULL));
		}

		$this->db->close();

	}

	function setAccessToken($_access_token) {

		$this->accessToken = $_access_token;

		$this->db->connect();

		$sessionSql = $this->db->query('SELECT * FROM `sessions` WHERE `access_token`="'.$this->accessToken.'"');

		$cnt = 0;
		while ($row = mysqli_fetch_object($sessionSql)) {
			$this->id = $row->id;
			$cnt++;
		}

		if ($cnt == 0) {
			$db->close();
			die(getRespond(false, 1, $this->errorList[1], NULL));
		}

		$userSql = $this->db->query('SELECT * FROM `users` WHERE `id`='.$this->id);
		$row = mysqli_fetch_object($userSql);
		$this->login = $row->name;
		$this->password = $row->password;
		$this->email = $row->email;

		$this->db->close();
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

	function destroySession($token) {
		$this->db->connect();
		$this->db->query('DELETE FROM `sessions` WHERE `access_token`="'.$token.'"');
		$this->db->close();
	}

	function isValidSession($token) {
		$this->db->connect();
		$res = fasle;
		if ($this->db->isExists('sessions', 'access_token', $token)) {
			$res = true;
		}

		$this->db->close();

		return $res;
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

	function logout() {
		$this->destroySession($this->accessToken);
	}
}