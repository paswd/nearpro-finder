<?php
require('config/main.php');

require_once('models/user.php');
require_once('models/property.php');
require_once('services/respond.php');

$postData = file_get_contents('php://input');
$received = json_decode($postData, true);

checkAccessToken($received['access_token']);

$token = htmlspecialchars($received['session_token']);
$email = htmlspecialchars($received['email']);

$user = new User;
$user->setAccessToken($token);
//die(getRespond(true, "", ""));
$user->edit($email);

die(getRespond(true, 0, '', $propertyData));

