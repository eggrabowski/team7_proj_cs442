<?php
include_once './db_functions.php';

//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application

//error_log("hello great wizard",0);
//error_log(print_r($_POST,true),0);

$json = $_POST["sellItemsJSON"];
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

error_log(print_r($data,true),0);

//error_log("Rec. Picture " . $data[$i]->picture,0);

//Store Item into MySQL DB
$res = $db->storeSellItem(
                        $data[0], //listing_start_day,
                        $data[1], //listing_end_date,
                        $data[2], //item_name,
                        $data[3], //description,
                        $data[4], //listing_user_email,
                        $data[5], //picture,
                        $data[6], //picture_thumbnail
			$data[7]  //item_price
                        $data[8]  //category_number
                        );

error_log("return ID is :" . $res, 0);


            //$b["item_number"] = $res;
            //array_push($a,$b);
//Post JSON response back to Android Application
//$toReturn = json_encode($a);
//error_log("json sent: " . $toReturn, 0);
echo $res
?>
