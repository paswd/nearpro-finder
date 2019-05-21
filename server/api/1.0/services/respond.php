<?php

function getRespond($status, $errorNum, $errorMsg, $data) {
	$data = [
		'status' => ($status ? 1 : 0),
		'error_num' => $errorNum,
		'error_msg' => $errorMsg,
		'data' => $data
	];

	return json_encode($data, JSON_UNESCAPED_UNICODE);
}

function checkAccessToken($token) {
	require('config/main.php');

	if (empty($token) || $token != $GLOBAL_PARAMS['access_token']) {
		die(getRespond(false, 1, $ERROR_LIST[1], NULL));
	}
}