<?php
include_once './db_functions.php';
//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application
$user = $_POST["user"];
//Remove Slashes
if (get_magic_quotes_gpc()){
$user = stripslashes($user);
}
//Decode JSON into an Array
//Util arrays to create response JSON
//Loop through an Array and insert data read from JSON into MySQL DB

error_log("get posted user: " . $user, 0);

//Store User into MySQL DB
$res = $db->getUser( 
                        $user
                        );
    //Based on inserttion, create JSON response
//Post JSON response back to Android Application
error_log("get posted user response:[" . $res ."]", 0);

if (!$res) { 
$res = json_encode (json_decode ("{}"));
error_log("getUser empty json", 0);
}else{
//$res["picture_thumbnail"]=base64_encode($res["picture_thumbnail"]);
$res = json_encode ($res);
}

echo $res
?>
