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