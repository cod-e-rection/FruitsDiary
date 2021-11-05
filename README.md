# Project Name Fruit Diary

Introduction

My Fruits Diary is a mobile application where you can store the number of fruits you have eaten
each day. The user is able to add date entries and for each date choose the fruits eaten on that
specific day. The application consumes a webservice where the current entries and fruits are
located. There are no requirements or limitations other than the requirements listed below under
”Software Requirements”.

// Fruits to be saved on sharedPref

Service class has all the api calls to be executed
API- we have the endpoints url to hit

I have organized it to be under MainActivity, and the project uses navigation (fragment)
There are bottom navigation tabs, the about me, which is a static Fragment.
HomeEntriesFragment which is the main and serves our purpose.

We hit the endpoint to get the entered dates/id and the fruits kept on the diary + vitamins on it.
We are able to add new entries (date) to keep track, or we can edit the existing ones.
Also we can remove/delete a given entry from the list.
