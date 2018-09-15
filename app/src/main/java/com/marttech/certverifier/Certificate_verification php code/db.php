<?php 
	try {
		$pdo = new PDO('mysql:host=localhost;dbname=certificate_verification','izowmart','mightmight');
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	} catch (PDOException $e) {
		echo "Connection Failed! ". $e->getMessage();
	}
 