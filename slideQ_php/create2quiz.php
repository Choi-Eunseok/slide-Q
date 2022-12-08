<?php
 	$con = mysqli_connect("localhost", "user", "password", "dbname");

	$qCategory = $_POST["qCategory"];
	$qQuiz = $_POST["qQuiz"];
	$correctAnswer = $_POST["correctAnswer"];
	$wrongAnswer = $_POST["wrongAnswer"];
	$createID = $_POST["createID"];
	$solvedID = $_POST["solvedID"];

	$statement = mysqli_prepare($con, "INSERT INTO quiz2 VALUES (?, ?, ?, ?, ?, ?)");
	mysqli_stmt_bind_param($statement, "ssssss", $qCategory, $qQuiz, $correctAnswer, $wrongAnswer, $createID, $solvedID);
	mysqli_stmt_execute($statement);

	$response = array();
	$response["success"] = true;

	echo json_encode($response);
?>
