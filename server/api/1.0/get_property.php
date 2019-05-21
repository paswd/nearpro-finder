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

$country = $received['country'];
$region = $received['region'];
$type = $received['type'];
$priceMin = $received['price_min'];
$priceMax = $received['price_max'];

settype($country, 'integer');
settype($region, 'integer');
settype($type, 'integer');
settype($priceMin, 'double');
settype($priceMax, 'double');

settype($lat, 'double');
settype($lng, 'double');
settype($radius, 'double');

$property = new Property;

$propertyData = $property->get($country, $region, $type, $priceMin, $priceMax,
	$isLocality, $lat, $lng, $radius);

die(getRespond(true, 0, '', $propertyData));

