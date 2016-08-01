<?php
/**
* Verifies a PayPal Live/Sandbox IPN.
* 
* It returns NULL if freak error occurs (no connection, bad reply, bad IPN).
* It returns BOOL to signal verification success or failure.
* 
* @author Claude "CodeAngry" Adrian
* 
* @param array/null $IPN
* @return bool/null
*/
function VerifyPaypalIPN(array $IPN = null){
	   error_log("RUNNING", 0);

    if(empty($IPN)){
        $IPN = $_POST;
	
	error_log("EMPTY IPN", 0);

	    foreach ($_POST as $key => $value) {
error_log($key."=".$value,0);

//[Fri Nov 27 17:42:34.884180 2015] [:error] [pid 15911] [client 104.194.100.143:57372] mc_gross=6.99
//[Fri Nov 27 17:42:34.884229 2015] [:error] [pid 15911] [client 104.194.100.143:57372] protection_eligibility=Ineligible
//[Fri Nov 27 17:42:34.884276 2015] [:error] [pid 15911] [client 104.194.100.143:57372] address_status=confirmed
//[Fri Nov 27 17:42:34.884316 2015] [:error] [pid 15911] [client 104.194.100.143:57372] payer_id=BG43B994VAFWJ
//[Fri Nov 27 17:42:34.884360 2015] [:error] [pid 15911] [client 104.194.100.143:57372] tax=0.00
//[Fri Nov 27 17:42:34.884401 2015] [:error] [pid 15911] [client 104.194.100.143:57372] address_street=1 Main St
//[Fri Nov 27 17:42:34.884440 2015] [:error] [pid 15911] [client 104.194.100.143:57372] payment_date=15:42:17 Nov 27, 2015 PST
//[Fri Nov 27 17:42:34.884480 2015] [:error] [pid 15911] [client 104.194.100.143:57372] payment_status=Pending
//[Fri Nov 27 17:42:34.884525 2015] [:error] [pid 15911] [client 104.194.100.143:57372] charset=windows-1252
//[Fri Nov 27 17:42:34.884570 2015] [:error] [pid 15911] [client 104.194.100.143:57372] address_zip=95131
//[Fri Nov 27 17:42:34.884615 2015] [:error] [pid 15911] [client 104.194.100.143:57372] first_name=default
//[Fri Nov 27 17:42:34.884655 2015] [:error] [pid 15911] [client 104.194.100.143:57372] address_country_code=US
//[Fri Nov 27 17:42:34.884694 2015] [:error] [pid 15911] [client 104.194.100.143:57372] address_name=default user
//[Fri Nov 27 17:42:34.884739 2015] [:error] [pid 15911] [client 104.194.100.143:57372] notify_version=3.8
//[Fri Nov 27 17:42:34.884785 2015] [:error] [pid 15911] [client 104.194.100.143:57372] custom=
//[Fri Nov 27 17:42:34.884830 2015] [:error] [pid 15911] [client 104.194.100.143:57372] payer_status=verified
//[Fri Nov 27 17:42:34.884870 2015] [:error] [pid 15911] [client 104.194.100.143:57372] address_country=United States
//[Fri Nov 27 17:42:34.884925 2015] [:error] [pid 15911] [client 104.194.100.143:57372] address_city=San Jose
//[Fri Nov 27 17:42:34.884969 2015] [:error] [pid 15911] [client 104.194.100.143:57372] quantity=1
//[Fri Nov 27 17:42:34.885014 2015] [:error] [pid 15911] [client 104.194.100.143:57372] payer_email=default@iit.edu
//[Fri Nov 27 17:42:34.885060 2015] [:error] [pid 15911] [client 104.194.100.143:57372] verify_sign=AFcWxV21C7fd0v3bYYYRCpSSRl31AL6.3oZlZmJvRwirWnbHYwzYeDt6
//[Fri Nov 27 17:42:34.885107 2015] [:error] [pid 15911] [client 104.194.100.143:57372] txn_id=35B29021J09358213
//[Fri Nov 27 17:42:34.885153 2015] [:error] [pid 15911] [client 104.194.100.143:57372] payment_type=instant
//[Fri Nov 27 17:42:34.885193 2015] [:error] [pid 15911] [client 104.194.100.143:57372] last_name=user
//[Fri Nov 27 17:42:34.885238 2015] [:error] [pid 15911] [client 104.194.100.143:57372] address_state=CA
//[Fri Nov 27 17:42:34.885279 2015] [:error] [pid 15911] [client 104.194.100.143:57372] receiver_email=blah@foo.net
//[Fri Nov 27 17:42:34.885318 2015] [:error] [pid 15911] [client 104.194.100.143:57372] pending_reason=unilateral
//[Fri Nov 27 17:42:34.885357 2015] [:error] [pid 15911] [client 104.194.100.143:57372] txn_type=web_accept
//[Fri Nov 27 17:42:34.885402 2015] [:error] [pid 15911] [client 104.194.100.143:57372] item_name=test jkhkhk
//[Fri Nov 27 17:42:34.885483 2015] [:error] [pid 15911] [client 104.194.100.143:57372] mc_currency=USD
//[Fri Nov 27 17:42:34.885522 2015] [:error] [pid 15911] [client 104.194.100.143:57372] item_number=
//[Fri Nov 27 17:42:34.885559 2015] [:error] [pid 15911] [client 104.194.100.143:57372] residence_country=US
//[Fri Nov 27 17:42:34.885603 2015] [:error] [pid 15911] [client 104.194.100.143:57372] test_ipn=1
//[Fri Nov 27 17:42:34.885641 2015] [:error] [pid 15911] [client 104.194.100.143:57372] handling_amount=0.00
//[Fri Nov 27 17:42:34.885679 2015] [:error] [pid 15911] [client 104.194.100.143:57372] transaction_subject=
//[Fri Nov 27 17:42:34.885717 2015] [:error] [pid 15911] [client 104.194.100.143:57372] payment_gross=6.99
//[Fri Nov 27 17:42:34.885760 2015] [:error] [pid 15911] [client 104.194.100.143:57372] shipping=0.00
//[Fri Nov 27 17:42:34.885799 2015] [:error] [pid 15911] [client 104.194.100.143:57372] auth=Alcv8Wo.aV7K-lDIsPlmj4MGtS3qBvfu6daWIsqUwTpy81hNBCmHCK-DQvnMFzwapmHr3Kd1PDNPPu.hSO.vNDg
//[Fri Nov 27 17:42:35.808141 2015] [:error] [pid 15911] [client 104.194.100.143:57372] VERIFIED



}
    }
    if(empty($IPN['verify_sign'])){
	  error_log("verifysign", 0);
        return null;
    }
    $IPN['cmd'] = '_notify-validate';
    $PaypalHost = (empty($IPN['test_ipn']) ? 'www' : 'www.sandbox').'.paypal.com';
    $cURL = curl_init();
    curl_setopt($cURL, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($cURL, CURLOPT_SSL_VERIFYHOST, false);
    curl_setopt($cURL, CURLOPT_URL, "https://{$PaypalHost}/cgi-bin/webscr");
    curl_setopt($cURL, CURLOPT_ENCODING, 'gzip');
    curl_setopt($cURL, CURLOPT_BINARYTRANSFER, true);
    curl_setopt($cURL, CURLOPT_POST, true); // POST back
    curl_setopt($cURL, CURLOPT_POSTFIELDS, $IPN); // the $IPN
    curl_setopt($cURL, CURLOPT_HEADER, false);
    curl_setopt($cURL, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($cURL, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_0);
    curl_setopt($cURL, CURLOPT_FORBID_REUSE, true);
    curl_setopt($cURL, CURLOPT_FRESH_CONNECT, true);
    curl_setopt($cURL, CURLOPT_CONNECTTIMEOUT, 30);
    curl_setopt($cURL, CURLOPT_TIMEOUT, 60);
    curl_setopt($cURL, CURLINFO_HEADER_OUT, true);
    curl_setopt($cURL, CURLOPT_HTTPHEADER, array(
        'Connection: close',
        'Expect: ',
    ));
    $Response = curl_exec($cURL);
    $Status = (int)curl_getinfo($cURL, CURLINFO_HTTP_CODE);
    curl_close($cURL);
    if(empty($Response) or !preg_match('~^(VERIFIED|INVALID)$~i', $Response = trim($Response)) or !$Status){
	error_log("GREENULL", 0);
        return null;
    }
    if(intval($Status / 100) != 2){
	error_log("STATUSFALSE", 0);
        return false;
    }
	error_log("VERIFIED", 0);
    return !strcasecmp($Response, 'VERIFIED');
}

VerifyPaypalIPN();

?>
