<?php
require('config/main.php');

require_once('models/user.php');
require_once('models/countrylist.php');
require_once('services/respond.php');

$postData = file_get_contents('php://input');
$received = json_decode($postData, true);

$token = htmlspecialchars($received['access_token']);

$user = new User;
$user->setAccessToken($token);

die(getRespond(true, 0, "", NULL));

