<?php 
include "db.php";
// using prepared staments to insert reports into your database
if (isset($_POST["name"]) && !empty($_POST["name"]) && isset($_POST["email"]) && !empty($_POST["email"]) && isset($_POST["descr"]) && !empty($_POST["descr"]) && isset($_POST["place"]) && !empty($_POST["place"])&& isset($_POST["image"]) && !empty($_POST["image"])&& isset($_POST["radiogroup"]) && !empty($_POST["radiogroup"])){
	
	$name = $_POST["name"];
	$email = $_POST["email"];
	$descr = $_POST["descr"];
	$place = $_POST["place"];
	$image = $_POST["image"];
	$radiogroup = $_POST["radiogroup"];

	$path = "images/$email.jpeg";
	$url = "http://192.168.0.13/Certificate_verification/$path";

	$decoded_img = base64_decode($image);

	$sql = "INSERT INTO reports(name,email,descr,place,image,radiogroup) VALUES(?,?,?,?,?,?)";
	$stmt = $pdo->prepare($sql);
	$stmt->execute([$name,$email,$descr,$place,$url,$radiogroup]);
	file_put_contents($path, $decoded_img);
		if (isset($stmt)) {
			echo "Data inserted successfuly";
		}else{
			echo "failed! data not inserted into mysql database";
		}
}else{
	echo "Please fill all the fields";
}
