<?php
require('config/main.php');

require_once('models/user.php');
require_once('models/property.php');
require_once('services/respond.php');

$postData = file_get_contents('php://input');
$received = json_decode($postData, true);

$token = htmlspecialchars($received['access_token']);

$user = new User;
$user->setAccessToken($token);
//die(getRespond(true, "", ""));

$isLocality = ($received['is_locality'] == '1' ? true : false);
$lat = $received['lat'];
$lng = $received['lng'];
$radius = $received['radius'];

$property = new Property;

$propertyData = $property->get($isLocality, $lat, $lng, $radius);

die(getRespond(true, 0, '', $propertyData));

