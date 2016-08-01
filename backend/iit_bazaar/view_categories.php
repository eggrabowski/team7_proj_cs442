<!--Test view for the php functions. Nothing related to android-->

<html>
<head><title>View Categories</title>
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
    $categories = $db->get_categories();
    if ($categories != false)
        $no_of_categories = mysql_num_rows($categories);
    else
        $no_of_categories = 0;
?>
<?php
    if ($no_of_categories > 0) {
?>
<table>
<tr id="header"><td>category_number</td><td>parent_category_number</td><td>category_name</td></tr>
<?php
    while ($row = mysql_fetch_array($categories)) {
?> 
<tr>
<td><span><?php echo $row["category_number"] ?></span></td>
<td><span><?php echo $row["parent_category_number"] ?></span></td>
<td<span><?php echo $row["category_name"] ?></span></td>
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
<? php
mysql_free_result($categories);
?>
</body>
</html>
