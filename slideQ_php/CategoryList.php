<?php
 $con = mysqli_connect("localhost", "user", "password", "dbname");
 $result = mysqli_query($con, "SELECT * FROM QuizCategory;");
 $response = array();

 while ($row = mysqli_fetch_array($result)) {
  array_push($response, array("categoryName"=>$row[0], "exampleQuestion"=>$row[1]));
 }

 echo json_encode(array("response"=>$response));
 mysqli_close($con);
?>
