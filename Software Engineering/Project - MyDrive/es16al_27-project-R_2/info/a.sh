#!/bin/sh
user="root"
db="drivedb"
mvn clean compile package dbclean:dbclean exec:java -Dexec.args="src/main/resources/other.xml"

mysql -u "$user" -p "$db" <<EOF
select * from drivedb.user;
select * from drivedb.my_drive;
select * from drivedb.my_drive_file;
EOF
