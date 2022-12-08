<?php
	$con = mysqli_connect("localhost", "user", "password", "dbname");

	$qQuiz = $_POST["qQuiz"];
	$solvedID = $_POST["solvedID"];

	$result = mysqli_query($con, "SELECT * FROM quiz2 WHERE qQuiz = '$qQuiz'");
	while ($row = mysqli_fetch_array($result)) {
		$orgin = $row['solvedID'];
		if(strpos($orgin, $solvedID)==false){
			$send = $orgin.",".$solvedID;

			$sql = "UPDATE quiz2 SET solvedID = '$send' WHERE qQuiz = '$qQuiz'" ;

			mysqli_query($con, $sql);

			echo ($send);
		}else {
			echo $row['solvedID'];
		}
	}
	mysqli_close($con);
?>
