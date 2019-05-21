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

	function sessionCleaner() {
		$this->db->connect();
		$this->db->query('DELETE FROM `sessions` WHERE `timeout` < '.date('U'));
		$this->db->close();
	}

	function __construct() {
		$this->setGlobalParams();
		$this->db = new DB;
		$this->sessionCleaner();
	}

	function encryptPassword($password) {
		return $password;
	}

	function getTimeout() {
		return date('U') + $this->globalParams['session_timeout'];
	}

	function setUserData($_login, $_password, $_email) {
		$this->login = $_login;
		$this->password = $this->encryptPassword($_password);
		$this->email = $_email;

		$this->db->connect();

		if (!$this->db->isExists('users', 'name', $this->login)) {
			die(getRespond(false, 5, $this->errorList[5], NULL));
		}

		$this->db->close();

	}

	function update() {
		$this->db->connect();
		$userSql = $this->db->query('SELECT * FROM `users` WHERE `id`='.$this->id);
		$row = mysqli_fetch_object($userSql);
		$this->login = $row->name;
		$this->password = $row->password;
		$this->email = $row->email;
		$this->db->close();
	}

	function registerFollowing($propertyId) {
		$this->db->connect();
		$this->db->query('INSERT INTO `user_followings` VALUES(NULL,
			'.$this->id.', '.$propertyId.', '.date('U').')');
		$this->db->close();
	}


	function setAccessToken($_access_token) {

		$this->accessToken = $_access_token;

		$this->db->connect();

		$sessionSql = $this->db->query('SELECT * FROM `sessions` WHERE `access_token`="'.$this->accessToken.'"
			AND (`timeout`>'.date('U').' OR `timeout`=0)');

		$cnt = 0;
		$sessionId = 0;
		while ($row = mysqli_fetch_object($sessionSql)) {
			$sessionId = $row->id;
			$this->id = $row->user_id;
			$cnt++;
		}

		if ($cnt == 0) {
			$this->db->close();
			die(getRespond(false, 1, $this->errorList[1], NULL));
		}

		$this->db->query('UPDATE `sessions` SET `timeout`='.
			$this->getTimeout()
			.' WHERE `access_token`="'.$this->accessToken.'"');

		$this->db->close();

		$this->update();
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

	function createSession() {
		$this->db->connect();
		while (true) {
			$generated = md5(sha1(rand()));

			if (!$this->db->isExists('sessions', 'access_token', $generated)) {
				$this->db->query('INSERT INTO `sessions` VALUES(NULL, '.$this->id.', "'.$generated.'",
					'.date('U').', '.$this->getTimeout().')');
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

		/*$this->accessToken = $_access_token;

		$this->db->connect();

		if (!$this->db->isExists('sessions', 'access_token', $this->accessToken)) {
			$this->db->close();
			die(getRespond(false, 1, $this->errorList[1], NULL));
		}

		$this->db->close();*/
	}

	function isEmpty() {
		return empty($this->login) || empty($this->password) || empty($this->email);
	}

	function auth() {
		$this->db->connect();
		$res = $this->db->query('SELECT * FROM `users` WHERE `name`="'.$this->login.'"');
		$row = mysqli_fetch_object($res);

		if ($this->password != $row->password) {
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
			NULL, "'.$this->login.'", "'.$this->password.'", "'.$this->email.'"
		)');
		$this->db->close();

		return $this->auth();
		//return getRespond(true, 0, '', $res);
	}

	function logout() {
		$this->destroySession($this->accessToken);
	}

	function edit($email) {
		$this->db->connect();

		$this->db->query('UPDATE `users` SET `email`="'.$email.'" WHERE `name`="'.$this->login.'"');
		$this->db->close();

		$this->update();
	}

	function changePassword($oldPassword, $newPassword) {
		if ($this->encryptPassword($oldPassword) != $this->password) {
			die(getRespond(false, 7, $this->errorList[7], NULL));
		}
		$enctypted = $this->encryptPassword($newPassword);
		$this->db->query('UPDATE `users` SET `password`="'.$encrypted.'" WHERE `name`="'.$this->login.'"');
		$this->password = $enctypted;
	}
}