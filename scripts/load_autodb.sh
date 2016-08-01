#! /bin/bash

FILENAME=../../mysql_pass

USERNAME=$(cat $FILENAME | sed -n 1p)
PASSWORD=$(cat $FILENAME | sed -n 2p)
DATABASE=$(cat $FILENAME | sed -n 3p)

mysql -u $USERNAME --password=$PASSWORD $DATABASE < iitbazaar.sql
