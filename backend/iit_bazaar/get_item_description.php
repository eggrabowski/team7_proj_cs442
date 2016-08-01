<?php
include_once './db_functions.php';

//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application

//error_log("hello great wizard",0);
//error_log(print_r($_POST,true),0);

$json = $_POST["itemsJSON"];
//$json = $_POST["item_number"];

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
//Loop through an Array and insert data read from JSON into MySQL DB

//error_log($json,0);

for($i=0; $i<count($data) ; $i++)
{
//error_log("Rec. Picture " . $data[$i]->picture,0);

//Store Item into MySQL DB
$res = $db->storeItem(
                        $data[$i]->listing_start_day,
                        $data[$i]->listing_end_date,
                        $data[$i]->item_name,
                        $data[$i]->description,
                        $data[$i]->listing_user_email,
                        $data[$i]->picture,
                        $data[$i]->picture_thumbnail,
			$data[$i]->item_price,
			$data[$i]->category_number
                        );

error_log("return ID is :" . $res, 0);

$toReturn = $res;
            //$b["item_number"] = $res;
            //array_push($a,$b);
}
//Post JSON response back to Android Application
//$toReturn = json_encode($a);
//error_log("json sent: " . $toReturn, 0);
echo $toReturn;
?>
