<?php
require('config/main.php');

require_once('services/user.php');
require_once('services/respond.php');

$postData = file_get_contents('php://input');
$received = json_decode($postData, true);

if (empty($received['token']) || $received['token'] != $GLOBAL_PARAMS['auth-token']) {
	die(getRespond(false, 1, $ERROR_LIST[1], NULL));
}
//die(getRespond(true, "", ""));

$user = new User(
	htmlspecialchars($received['login']),
	htmlspecialchars($received['password']),
	''
);

die($user->auth());

