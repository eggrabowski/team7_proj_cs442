<!--Test view for the php functions. Nothing related to android-->

<html>
<head><title>View Items</title>
<style>
body {
  font: normal medium/1.4 sans-serif;
}
table {
  border-collapse: collapse;
  width: 20%;
   margin-left: auto;
    margin-right: auto;
}
tr > td {
  padding: 0.25rem;
  text-align: center;
  border: 1px solid #ccc;
}
tr:nth-child(even) {
  background: #FAE1EE;
}
tr:nth-child(odd) {
  background: #edd3ff;
}
tr#header{
background: #c1e2ff;
}
div.header{
padding: 10px;
background: #e0ffc1;
width:30%;
color: #008000;
margin:5px;
}
div.refresh{
margin-top:10px;
width: 5%;
margin-left: auto;
margin-right: auto;
}
div#norecord{
margin-top:10px;
width: 15%;
margin-left: auto;
margin-right: auto;
}
</style>
<script>
function refreshPage(){
location.reload();
}
</script>
</head>
<body>
<center>
<div class="header">
Android SQLite and MySQL Sync Results
</div>
</center>
<?php
    include_once 'db_functions.php';
    $db = new DB_Functions();
    $items = $db->getAllItems();
    if ($items != false)
        $no_of_items = mysql_num_rows($items);
    else
        $no_of_items = 0;
?>
<?php
    if ($no_of_items > 0) {
?>
<table>
<tr id="header"><td>item_number</td><td>item_price</td><td>listing_start_day</td><td>listing_end_date</td><td>item_name</td><td>description</td><td>listing_user_email</td><td>item_picture_thumbnail</td><td>item_picture</td><td>category_number</td></tr>
<?php
    while ($row = mysql_fetch_array($items)) {
?> 
<tr>
<td><span><?php echo $row["item_number"] ?></span></td>
<td><span><?php echo $row["item_price"] ?></span></td>
<td><span><?php echo $row["listing_start_day"] ?></span></td>
<td><span><?php echo $row["listing_end_date"] ?></span></td>
<td><span><?php echo $row["item_name"] ?></span></td>
<td><span><?php echo $row["description"] ?></span></td>
<td><span><?php echo $row["listing_user_email"] ?></span></td>
<td><span><img src="data:image/jpeg;base64,<?php echo $row["item_picture_thumbnail"]; ?>" /></span></td>
<td><span><img src="data:image/jpeg;base64,<?php echo $row["item_picture"]; ?>" /></span></td>
<td><span><?php echo $row["category_number"] ?></span></td>
</tr>
<?php } ?>
</table>
<?php }else{ ?>
<div id="norecord">
No records in MySQL DB
</div>
<?php } ?>
<div class="refresh">
<button onclick="refreshPage()">Refresh</button>
</div>
</body>
</html>
