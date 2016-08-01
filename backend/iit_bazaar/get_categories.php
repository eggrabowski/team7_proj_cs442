<?php
include_once './db_functions.php';

//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application

error_log("Categories from the wire " . $data,0);

$resCategories=array();
$fields=array();

    $categories = $db->get_categories();
    if ($categories != false)
        $no_of_categories = mysql_num_rows($categories);
    else
        $no_of_categories = 0;
    if ($no_of_categories > 0) {
    while ($row = mysql_fetch_array($categories)) {

	$fields["category_number"] = $row["category_number"];
	$fields["parent_category_number"] = $row["parent_category_number"];
	$fields["category_name"] = $row["category_name"];
	array_push($resCategories,$fields);
 }
}
mysql_free_result($categories);

$toReturn = json_encode($resCategories);

echo $toReturn;
?>
