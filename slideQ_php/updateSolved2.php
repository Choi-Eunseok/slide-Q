<?php
	$con = mysqli_connect("localhost", "user", "password", "dbname");

	$qQuiz = $_POST["qQuiz"];
	$solvedID = $_POST["solvedID"];

	$sql = "UPDATE `quiz2` SET `solvedID` = '".$solvedID."' WHERE `qQuiz` = '" .$qQuiz."';" ;

//UPDATE `quiz2` SET `solvedID` = '11' WHERE `quiz2`.`qCategory` = 'a';
	mysqli_query($con, $sql);

	mysqli_close($con);
?>
