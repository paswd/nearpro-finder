<?php
require('config/main.php');

require_once('models/user.php');
require_once('services/respond.php');

$postData = file_get_contents('php://input');
$received = json_decode($postData, true);

checkAccessToken($received['access_token']);

$token = htmlspecialchars($received['session_token']);

$user = new User;
$user->destroySession($token);
//die(getRespond(true, "", ""));

die(getRespond(true, 0, "", NULL));

