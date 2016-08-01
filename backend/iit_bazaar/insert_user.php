<?php
include_once './db_functions.php';
//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application
$json = $_POST["userJSON"];
//Remove Slashes
if (get_magic_quotes_gpc()){
$json = stripslashes($json);
}
//Decode JSON into an Array
$data = json_decode($json);

//Store User into MySQL DB
$res = $db->storeUser(  
                        $data[0], //email,
                        $data[1], //first_name,
			$data[2], //last_name,
                        $data[3], //major_department,
                        $data[4], //picture,
                        $data[5]  //picture_thumbnail
                        );

error_log("user return ID is :" . $res, 0);
echo $res
?>
