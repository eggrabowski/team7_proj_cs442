<?php
 
class DB_Functions {
 
    private $db;
 
    //put your code here
    // constructor
    function __construct() {
        include_once './db_connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
 
    }

     /**
     * Storing new user
     * returns user details
     */

    public function storeUser(  
                                $Email,
                                $FirstName,
                                $LastName,
                                $MajorDepartment,
                                $Picture,
                                $PictureThumbnail) {
        // Insert user into database
        $query = "INSERT INTO user VALUES('$Email','$FirstName','$LastName','$MajorDepartment',FROM_BASE64('$PictureThumbnail'),FROM_BASE64('$Picture'))";

	error_log("query: ".$query,0);
	$result = mysql_query($query);

        if ($result) {
            error_log("Insertion success!", 0);
            return 0;
        } else {
		$error = mysql_errno();
		return -$error;
        }
    }



    /**
     * Getting a user
     */
    public function getUser($email) {
        $result = mysql_query("select email, first_name, last_name, major_department,TO_BASE64(picture) AS picture, TO_BASE64(picture_thumbnail) AS picture_thumbnail FROM user WHERE email='$email'");
	if ($result) {
          $result = mysql_fetch_assoc($result);
            return $result;
        } else {
                $error = mysql_errno();
                return -$error;
        }
    }


    /**
     * Getting all uses
     */
    public function getAllUsers() {
        $result = mysql_query("select email, first_name, last_name, major_department,TO_BASE64(picture) AS picture, TO_BASE64(picture_thumbnail) AS picture_thumbnail  FROM user");
        return $result;
    }
 
    /**
     * Storing new item
     * returns item details
     */
    public function storeItem(
				                $listing_start_day,
                                $listing_end_date,
                                $item_name,
                                $description,
                                $listing_user_email,
                                $picture,
				$picture_thumbnail,
				$price,
				$category) 
    {

    	$sqlquery="INSERT INTO item VALUES(NULL,'$listing_start_day','$listing_end_date','$item_name','$description','$listing_user_email',FROM_BASE64('$picture_thumbnail'),FROM_BASE64('$picture'),'$price','$category')";
    	
// error_log("Last item number inserted ".mysql_insert_id(), 0);

    	error_log("query2: " . $sqlquery,0);
    	// Insert item into database
            $result = mysql_query($sqlquery);
     
            if ($result) {

		$insertID = mysql_insert_id();
                error_log("Insertion success! - id is ".$insertID, 0);

		return $insertID;
            } else {
			$errcode = mysql_errno();
			return -$errcode;
                }            
     }
    

    /**
     * Getting all items
     */

    public function getAllItems() {
        $result = mysql_query("SELECT item_number, listing_start_date, listing_end_date, item_name, description, listing_user_email, TO_BASE64(item_picture) AS item_picture, TO_BASE64(item_picture_thumbnail) AS item_picture_thumbnail, item_price, category_number FROM item");
        return $result;
    }

    /**
     * Getting all categories for a particular id
     */

    public function get_categories() {
	      error_log("get_categories - category id: " . $category_id,0);

	$query = "SELECT * FROM category ORDER BY category_name ASC";

	  error_log("get_categories - query: " . $query,0);

 	 $result = mysql_query($query);

    if ($result) {
            return $result;
        } else {
	error_log("did not get categories",0);
                $error = mysql_errno();
                return -$error;
        }

    }

    /**
     * Getting all watchlists
     */



    public function getAllWatchlists() {

	$result = mysql_query("SELECT item.*, watchlist.item_number, watchlist.user_email FROM item JOIN item.item_number=watchlist.item_number");
        return $result;

	}


    public function getWatchlist() {

        $result = mysql_query("SELECT item.*, watchlist.item_number, watchlist.user_email FROM item JOIN item.item_number=watchlist.item_number");
        return $result;

        }

    public function getWatchListByEmail($email) {
	$result = mysql_query("SELECT i.item_number, i.listing_start_date, i.listing_end_date, i.item_name, i.description, i.listing_user_email, TO_BASE64(i.item_picture) AS item_picture, TO_BASE64(i.item_picture_thumbnail) AS item_picture_thumbnail, i.item_price, i.category_number FROM item i, watchlist w WHERE i.item_number=w.item_number AND w.user_email='$email'");
        if ($result) {
          //$result = mysql_fetch_assoc($result);
            return $result;
        } else {
                $error = mysql_errno();
                return -$error;
        }
    }

    public function addWatchlistPair($email, $item_number) {
        $sqlquery="INSERT INTO watchlist VALUES('$email','$item_number')";
        $result = mysql_query($sqlquery);
        return $result;
    }

    public function deleteWatchlistPair ($email, $item_number) {
        $sqlquery="DELETE FROM watchlist WHERE user_email='$email' AND item_number=$item_number";
        $result = mysql_query($sqlquery);
        return $result;
    }

    /**
     * Getting all logs
     */

    public function getAllLogs() {

        $result = mysql_query("SELECT * FROM log ORDER BY log_date ASC");
        return $result;

        }

    /**
     *  Getting item by number
     */

    public function getItemByNumber ($item_number) {
        $result = mysql_query("SELECT item_number, listing_start_date, listing_end_date, item_name, description, listing_user_email, TO_BASE64(item_picture) AS item_picture, TO_BASE64(item_picture_thumbnail) AS item_picture_thumbnail, item_price, category_number FROM item WHERE item_number='$item_number'");
        if ($result) {
          $result = mysql_fetch_assoc($result);
            return $result;
        } else {
                $error = mysql_errno();
                return -$error;
        }
    }

    /**
     *  Getting current items -- call this to get items into item table
     */

    public function getItemAfterDate ($date) {
	$result = mysql_query("SELECT item_number, listing_start_date, listing_end_date, item_name, description, listing_user_email, TO_BASE64(item_picture) AS item_picture, TO_BASE64(item_picture_thumbnail) AS item_picture_thumbnail, item_price, category_number FROM item WHERE listing_end_date > $date ORDER BY listing_end_date ASC");
	if ($result) {
          //$result = mysql_fetch_assoc($result);
            return $result;
        } else {
                $error = mysql_errno();
                return -$error;
        }
    }


	public function getCurrentSellingItem($email, $date){


        $result = mysql_query("SELECT item_number, listing_start_date, listing_end_date, item_name, description, listing_user_email, TO_BASE64(item_picture) AS item_picture, TO_BASE64(item_picture_thumbnail) AS item_picture_thumbnail, item_price, category_number FROM item WHERE listing_end_date > $date and listing_user_email='$email' ORDER BY listing_end_date ASC");
        return $result;

	}


        public function getSellingItems($email){


        $result = mysql_query("SELECT item_number, listing_start_date, listing_end_date, item_name, description, listing_user_email, TO_BASE64(item_picture) AS item_picture, TO_BASE64(item_picture_thumbnail) AS item_picture_thumbnail, item_price, category_number FROM item WHERE listing_user_email='$email' ORDER BY listing_end_date ASC");
        return $result;

        }
	



  /**
     *  Getting current item number excluding ones passed in -- call this to get items into item table
     */

    public function getItemNumbersAfterDate ($date, $list) {

	error_log("getItemNumbersAfterDate: input list is" . $list,0);	

	$sqlquery = "select GROUP_CONCAT(item_number) from item where item_number not in ($list) and listing_end_date > $date";

	error_log("getItemNumbersAfterDate: " . $sqlquery,0);

        $result = mysql_query($sqlquery);


        if ($result) {
          $result = mysql_fetch_assoc($result);
            return $result;
        } else {
                $error = mysql_errno();
                return -$error;
        }
    }




   /**
     * unlistItem
     * 
     */
    public function unlistItem(
                                                $item_number
                                )
    {

	error_log("unlistItem " . $item_number, 0);

        $sqlquery="UPDATE item SET item.listing_end_date=0 WHERE item.item_number='$item_number'";


        error_log("query2: " . $sqlquery,0);
          
		  $result = mysql_query($sqlquery);

            if ($result) {

                return 1;
            } else {
                        $errcode = mysql_errno();
                        return -$errcode;
                }
     }









	/**
     * Storing new item
     * returns item detail
	*this shouldn exist, use a better query instead
     */
    public function storeSellItem(
				                $listing_start_day,
                                $listing_end_date,
                                $item_name,
                                $description,
                                $listing_user_email,
                                $picture,
				$picture_thumbnail,
				$price,
				$category_number) 
    {

    	$sqlquery="INSERT INTO selling_item VALUES(NULL,'$listing_start_day','$listing_end_date','$item_name','$description','$listing_user_email',FROM_BASE64('$picture_thumbnail'),FROM_BASE64('$picture'),'$price','$category_number')";
    	
// error_log("Last item number inserted ".mysql_insert_id(), 0);

    	error_log("query2: " . $sqlquery,0);
    	// Insert item into database
            $result = mysql_query($sqlquery);
     
            if ($result) {
                $insertID = mysql_insert_id();
                error_log("Insertion to sell table success! - id is ".$insertID, 0);
                return $insertID;
            } else {
			    $errcode = mysql_errno();
			    return -$errcode;
            }            
     }


}
 
?>
