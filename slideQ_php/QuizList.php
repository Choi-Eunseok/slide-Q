<?php
 $con = mysqli_connect("localhost", "user", "password", "dbname");
 $result = mysqli_query($con, "SELECT * FROM QUIZ;");
 $response = array();

 while ($row = mysqli_fetch_array($result)) {
  array_push($response, array("quizCategory"=>$row[0], "quizQuiz"=>$row[1], "Num"=>$row[2], "quizCateNum"=>$row[3], "correctAnswer"=>$row[4], "wrongAnswer"=>$row[5]));
 }

 echo json_encode(array("response"=>$response));
 mysqli_close($con);
?>
