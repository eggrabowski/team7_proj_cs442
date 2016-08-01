<?php
include_once './db_functions.php';
//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application
$item = $_POST["item_number"];
//Remove Slashes
if (get_magic_quotes_gpc()){
$item = stripslashes($item);
}
//Decode JSON into an Array
//Util arrays to create response JSON
//Loop through an Array and insert data read from JSON into MySQL DB

error_log("get posted item: " . $item, 0);

//Store User into MySQL DB
$res = $db->getItemByNumber( 
                        $item
                        );
    //Based on inserttion, create JSON response
//Post JSON response back to Android Application
error_log("get posted item response:[" . $res ."]", 0);

if (!$res) {
$res = json_encode (json_decode ("{}"));
error_log("getItem empty json", 0);
}else{
	//$res["item_picture"]=base64_encode($res["item_picture"]);
	$res = json_encode ($res);
}

echo $res
?>
