<?php
	$con = mysqli_connect("localhost", "user", "password", "dbname");

	$quizCategory = $_POST["quizCategory"];
	$quizQuiz = $_POST["quizQuiz"];
	$Num = $_POST["Num"];
	$quizCateNum = $_POST["quizCateNum"];
	$correctAnswer = $_POST["correctAnswer"];
	$wrongAnswer = $_POST["wrongAnswer"];

	$statement = mysqli_prepare($con, "INSERT INTO QUIZ VALUES (?, ?, ?, ?, ?, ?)");
	mysqli_stmt_bind_param($statement, "ssssss", $quizCategory, $quizQuiz, $Num, $quizCateNum, $correctAnswer, $wrongAnswer);
	mysqli_stmt_execute($statement);

	$response = array();
	$response["success"] = true;

	echo json_encode($response);
?>
