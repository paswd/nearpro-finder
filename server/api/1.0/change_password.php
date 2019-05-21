<?php
require('config/main.php');

require_once('models/user.php');
require_once('models/property.php');
require_once('services/respond.php');

$postData = file_get_contents('php://input');
$received = json_decode($postData, true);

$token = htmlspecialchars($received['access_token']);
$oldPassword = htmlspecialchars($received['old_password']);
$newPassword = htmlspecialchars($received['new_password']);

$user = new User;
$user->setAccessToken($token);
//die(getRespond(true, "", ""));
$user->changePassword($oldPassword, $newPassword);

die(getRespond(true, 0, '', $propertyData));

