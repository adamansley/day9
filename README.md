 To Test, Edit, and Deploy Hubbub in it's start-of-day9 state:

In the NetBeans services tab, right-click on JavaDB and choose "Create Database".

Name the database "hubbubjpa" and assign the usual username and password.

After cloning Hubbub, open WEB-INF/boot-derby.sql, select your hubbubjpa connection
in the connection dropdown, then execute the script by clicking on the little db drum
on the right.

Now you may deploy Hubbub in run or debug mode.

Features Implemented:
1) Guest Timeline
2) Registered User Timeline
3) Wall for Registered Users with Follow/Unfollow Links
4) Login and Logout
5) Register to Join
6) Profile for Registered Users with Owner Updates
7) Avatar Setting/Reverting via File Upload
8) Avatar Icons Next to Each Post
9) Follow and Unfollow other Hubbub Users
