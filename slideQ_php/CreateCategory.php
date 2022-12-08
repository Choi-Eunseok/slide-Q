<?php
	$con = mysqli_connect("localhost", "user", "password", "dbname");

	$categoryName = $_POST["categoryName"];
	$exampleQuestion = $_POST["exampleQuestion"];
	$creatorID = $_POST["creatorID"];

	$statement = mysqli_prepare($con, "INSERT INTO QuizCategory VALUES (?, ?, ?)");
	mysqli_stmt_bind_param($statement, "sss", $categoryName, $exampleQuestion, $creatorID);
	mysqli_stmt_execute($statement);

	$response = array();
	$response["success"] = true;

	echo json_encode($response);
?>
