<?php
 $con = mysqli_connect("localhost", "user", "password", "dbname");
 $result = mysqli_query($con, "SELECT * FROM quiz2;");
 $response = array();

 while ($row = mysqli_fetch_array($result)) {
  array_push($response, array("qCategory"=>$row[0], "qQuiz"=>$row[1], "correctAnswer"=>$row[2], "wrongAnswer"=>$row[3], "createID"=>$row[4], "solvedID"=>$row[5]));
 }

 echo json_encode(array("response"=>$response));
 mysqli_close($con);
?>
