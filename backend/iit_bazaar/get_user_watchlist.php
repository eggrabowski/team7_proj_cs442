<?php
include_once './db_functions.php';
$db = new DB_Functions();

$email = $_POST["email"];

$items=array();
$fields=array();

$result = $db->getWatchListByEmail($email);

	while ($item = mysql_fetch_assoc($result)) {
	$fields = $item;
	$fields["item_number"] = $item["item_number"];
	$fields["listing_end_date"] = $item["listing_end_date"];
	$fields["item_name"] = $item["item_name"];
	$fields["description"] = $item["description"];
	$fields["item_price"] = $item["item_price"];
	$fields["item_picture_thumbnail"] = $item["item_picture_thumbnail"];
    	$fields["price"] = $item["price"];
	//$fields["listing_user_email"] = $item["listing_user_email"];
	array_push($items,$fields);
}

$toReturn = json_encode($items);

error_log($toReturn,0);
mysql_free_result($result);
echo $toReturn;

?>

