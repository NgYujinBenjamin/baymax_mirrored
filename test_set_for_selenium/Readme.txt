Please Take Note:

- When setting up, create a folder called "test" in the directory of C:/, unzip this file and copy the contents of the 3 files over the directory.

- Selenium Test Script will be utilizing the 3 files as part of the automate testing.

- Everytime when completing the full cycle of the test suite, proceed to accessMaster.bat in Baymaxdb directory then depending on (Windows/Mac) after start up the startMaster.bat and up running.

Steps:
	1) Type in the script in the command prompt or terminal,
	> 	"use baymaxdb;"

	2) Next to view all available users in the user table,
	>	"select * from users;"

	3) Then delete the user 'mary' that is created and converted to admin during 		the test suite operation, as admin are not shown or can be deleted by 			another admin when trying to view all users.
	>	"DELETE FROM users WHERE username="mary";

