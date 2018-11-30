#!/bin/sh
user="mydrive"
db="drivedb"
#mvn clean compile package dbclean:dbclean exec:java -Dexec.args="src/main/resources/other.xml"

mysql -u "$user" -p "$db" <<EOF
select * from drivedb.USER;
select * from drivedb.MY_DRIVE;
select * from drivedb.MY_DRIVE_FILE;
EOF
