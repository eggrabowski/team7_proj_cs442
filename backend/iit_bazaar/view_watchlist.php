<html>
<head><title>View Watchlists</title>
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
    $watchlists = $db->getAllWatchlists();
    if ($items != false)
        $no_of_watchlists = mysql_num_rows($watchlists);
    else
        $no_of_watchlists = 0;
?>
<?php
    if ($no_of_watchlists > 0) {
?>
<table>
<tr id="header"><td>Id</td><td>Watchlist</td></tr>
<?php
    while ($row = mysql_fetch_array($watchlists)) {
?> 

getAllWatchlists

<tr>
<td><span><?php echo $row["item_number"] ?></span></td>
<td><span><?php echo $row["listing_start_day"] ?></span></td>
<td><span><?php echo $row["listing_end_date"] ?></span></td>
<td><span><?php echo $row["item_name"] ?></span></td>
<td><span><?php echo $row["description"] ?></span></td>
<td><span><?php echo $row["listing_user_email"] ?></span></td>
<td><span><img src="data:image/jpeg;base64,<?php echo base64_encode($row["item_picture"]); ?>" /></span></td>
<td><span><img src="data:image/jpeg;base64,<?php echo base64_encode($row["item_picture_thumbnail"]); ?>" /></span></td>
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
