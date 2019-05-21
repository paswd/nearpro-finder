<?php
require('config/main.php');

require_once('models/user.php');
require_once('services/respond.php');

$postData = file_get_contents('php://input');
$received = json_decode($postData, true);

checkAccessToken($received['access_token']);

if (empty($received['access_token']) || $received['access_token'] != $GLOBAL_PARAMS['access_token']) {
	die(getRespond(false, 1, $ERROR_LIST[1], NULL));
}
//die(getRespond(true, "", ""));

$user = new User;
$user->setUserData(
	htmlspecialchars($received['login']),
	htmlspecialchars($received['password']),
	htmlspecialchars($received['email']),
	true
);

die($user->register());

