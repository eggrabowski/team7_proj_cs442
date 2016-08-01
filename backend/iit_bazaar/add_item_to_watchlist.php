<?php
include_once './db_functions.php';

//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application
$json = $_POST["watchlistPairJSON"];

//Remove Slashes
if (get_magic_quotes_gpc()){
$json = stripslashes($json);
}
//Decode JSON into an Array
$data = json_decode($json);

//error_log(print_r($data,true));
//Util arrays to create response JSON
$a=array();
$b=array();

$res = $db->addWatchlistPair(
                        $data[0], //email
                        $data[1]  //item_number
                        );

echo $res ? 'OK' : 'ERROR';

?>
