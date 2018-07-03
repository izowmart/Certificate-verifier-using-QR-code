<?php 
include "db.php";
	 if (isset($_POST['id']) && !empty($_POST['id'])) {
	
	 	$id = $_POST['id'];
		
		$sql = "SELECT * FROM certificates WHERE id = ?";
		$stmt =$pdo->prepare($sql);
		$stmt->execute([$id]);

		if ($stmt->rowCount()) {
			while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
				$studentData[] = $row;
				
				foreach ($studentData as $key ) {
					$singleParsed = "";

					$singleParsed .= "Name           : " .$key['name'] . "\n" ;
                	$singleParsed .= "Institution    : " .$key['institution'] . "\n"; 
                	$singleParsed .= "Reg_Number   	 : " .$key['reg_number'] . "\n"; 
                	$singleParsed .= "Grade       	 : " .$key['grade'] . "\n"; 
                	$singleParsed .= "Date of issue  : " .$key['date'] . "\n";

                	echo $singleParsed;
				}

			}
		}else{
			echo "Wrong Qr Code!";
		}
	 }else{
	 	echo "No scanned results sent yet.";
	 	}

 ?>		