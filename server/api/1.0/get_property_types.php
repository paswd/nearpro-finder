<?php
require('config/main.php');

require_once('models/user.php');
require_once('models/property_types.php');
require_once('services/respond.php');

$postData = file_get_contents('php://input');
$received = json_decode($postData, true);

$token = htmlspecialchars($received['access_token']);

$user = new User;
$user->setAccessToken($token);
//die(getRespond(true, "", ""));

$propertyTypes = new PropertyTypes;

$regionData = $propertyTypes->get();

die(getRespond(true, 0, "", $regionData));

