<?php
include_once './db_functions.php';

//Create Object for DB_Functions clas
$db = new DB_Functions(); 

$data = $_POST["item_number"];

//Remove Slashes
if (get_magic_quotes_gpc()){
$data = stripslashes($data);
}

$res = $db->unlistItem(
			$data  //item_price
                        );

error_log("return ID is :" . $res, 0);


echo $res
?>
