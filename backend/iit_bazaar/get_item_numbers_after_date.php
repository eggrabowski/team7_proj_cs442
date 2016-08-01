<?php
include_once './db_functions.php';
$db = new DB_Functions();

$date = $_POST["date"];
$list = $_POST["list"];

error_log("raw post list " . $rawlist,0);

if (get_magic_quotes_gpc()){
$date = stripslashes($date);
$list = stripslashes($list);
}



$result = $db->getItemNumbersAfterDate($date, $list);

error_log("flat result " . strval($result),0);

$toResult =  implode(",",$result);

error_log("flat result " . $toResult,0);
echo $toResult;
?>
