<?php
include_once './db_functions.php';
$db = new DB_Functions();

$json = $_POST["dateJSON"];

if (get_magic_quotes_gpc()){
$json = stripslashes($json);
}

$date = json_decode($json);

$items=array();
$fields=array();

$result = $db->getItemAfterDate($date);

	while ($item = mysql_fetch_assoc($result)) {
 //while ($item=mysql_fetch_array($result)) {
	$fields = $item;
	$fields["item_number"] = $item["item_number"];
	$fields["listing_end_date"] = $item["listing_end_date"];
	$fields["item_name"] = $item["item_name"];
	$fields["description"] = $item["description"];
	$fields["item_price"] = $item["item_price"];
	$fields["item_picture_thumbnail"] = $item["item_picture_thumbnail"];//base64_encode($item["item_picture_thumbnail"]);
        $fields["category_number"] = $item["category_number"];
	array_push($items,$fields);
}

$toReturn = json_encode($items);

error_log($toReturn,0);
mysql_free_result($result);
echo $toReturn;

?>
