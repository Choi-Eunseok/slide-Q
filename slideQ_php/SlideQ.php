<?php
  $con = mysqli_connect("localhost", "user", "password", "dbname");

  $address = $_POST["address"];
  if($address == "searchID"){

    $userID = $_POST["userID"];
    $echo_statement = false;
    $result = mysqli_query($con, "SELECT * FROM USER WHERE userID = '$userID'");
    $response = array();
    while ($row = mysqli_fetch_array($result)) {
      $statement = true;
      $echo_statement = true;
      echo($row['userName']);
      // array_push($response, array("statement"=>$statement, "userName"=>$row['userName']));
    }
    if($echo_statement == false){
      // $statement = false;
      // array_push($response, array("statement"=>$statement));
      echo("true");
    }
    // echo json_encode(array("response"=>$response));

  }else if($address == "register"){

    $userID = $_POST["userID"];
  	$userPassword = $_POST["userPassword"];
  	$userName = $_POST["userName"];
  	$userAge = $_POST["userAge"];
  	$statement = mysqli_prepare($con, "INSERT INTO USER VALUES (?, ?, ?, ?)");
  	mysqli_stmt_bind_param($statement, "sssi", $userID, $userPassword, $userName, $userAge);
  	mysqli_stmt_execute($statement);
  	$response = array();
  	$response["success"] = true;
  	echo json_encode($response);

  }else if($address == "login"){

    $userID = $_POST["userID"];
  	$userPassword = $_POST["userPassword"];
  	$statement = mysqli_prepare($con, "SELECT * FROM USER WHERE userID = ? AND userPassword = ?");
  	mysqli_stmt_bind_param($statement, "ss", $userID, $userPassword);
  	mysqli_stmt_execute($statement);
  	mysqli_stmt_store_result($statement);
  	mysqli_stmt_bind_result($statement, $userID, $userPassword, $userName, $userAge);
  	$response = array();
  	$response["success"] = false;
  	while(mysqli_stmt_fetch($statement)){
  		$response["success"] = true;
  		$response["userID"] = $userID;
  		$response["userPassword"] = $userPassword;
  		$response["userName"] = $userName;
  		$response["userAge"] = $userAge;
  	}
  	echo json_encode($response);

  }else if($address == "userList"){

    $result = mysqli_query($con, "SELECT * FROM USER;");
    $response = array();
    while ($row = mysqli_fetch_array($result)) {
     array_push($response, array("userID"=>$row[0], "userPassword"=>$row[1], "userName"=>$row[2], "userAge"=>$row[3]));
    }
    echo json_encode(array("response"=>$response));

  }else if($address == "createCategory"){

    $categoryName = $_POST["categoryName"];
    $exampleQuestion = $_POST["exampleQuestion"];
    $creatorID = $_POST["creatorID"];
    $statement = mysqli_prepare($con, "INSERT INTO QuizCategory VALUES (?, ?, ?)");
    mysqli_stmt_bind_param($statement, "sss", $categoryName, $exampleQuestion, $creatorID);
    mysqli_stmt_execute($statement);
    $response = array();
    $response["success"] = true;
    echo json_encode($response);

  }else if($address == "categoryList"){

    $result = mysqli_query($con, "SELECT * FROM QuizCategory;");
    $response = array();
    while ($row = mysqli_fetch_array($result)) {
     array_push($response, array("categoryName"=>$row[0], "exampleQuestion"=>$row[1]));
    }
    echo json_encode(array("response"=>$response));

  }else if($address == "createQuiz"){

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

  }else if($address == "quizList"){

    $result = mysqli_query($con, "SELECT * FROM quiz2;");
    $response = array();
    while ($row = mysqli_fetch_array($result)) {
     array_push($response, array("qCategory"=>$row[0], "qQuiz"=>$row[1], "correctAnswer"=>$row[2], "wrongAnswer"=>$row[3], "createID"=>$row[4], "solvedID"=>$row[5]));
    }
    echo json_encode(array("response"=>$response));

  }else if($address == "updateSolved"){

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

  }else if($address == "userDelete"){

    $userID = $_POST["userID"];
    $statement = mysqli_prepare($con, "DELETE FROM USER WHERE userID = ?");
    mysqli_stmt_bind_param($statement, "s", $userID);
    mysqli_stmt_execute($statement);
    $response = array();
    $response["success"] = true;
    echo json_encode($response);

  }

  mysqli_close($con);
?>
