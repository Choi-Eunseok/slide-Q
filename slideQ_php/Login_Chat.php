<?php
	$con = mysqli_connect("localhost", "user", "password", "dbname");

	$userID = $_POST["userID"];
	$userPW = $_POST["userPW"];

	$statement = mysqli_prepare($con, "SELECT * FROM USER_CHAT WHERE userID = ? AND userPW = ?");
	mysqli_stmt_bind_param($statement, "ss", $userID, $userPW);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $userID, $userPW, $userNAME, $userAGE);

	$response = array();
	$response["success"] = false;

	while(mysqli_stmt_fetch($statement)){
		$response["success"] = true;
		$response["userID"] = $userID;
		$response["userPW"] = $userPW;
		$response["userNAME"] = $userNAME;
		$response["userAGE"] = $userAGE;
	}

	echo json_encode($response);
?>
