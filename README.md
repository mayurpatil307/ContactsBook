This repository contains an Android Contacts app that allows users to access and manage their local contacts. 
The app is built using Kotlin as the development language and follows modern Android development practices.

Features
The app's minimum SDK version is 21, and the target SDK version is 33.
The left panel of the app displays a list of three items with default icons: Contacts, Call Log, and SMS Inbox.
Contacts are divided into two tabs: Local Contacts and Google Contacts.
Local Contacts: Access local contacts from the device.
Google Contacts: Requires permissions and Google account selection.
Call Log is divided into three tabs: Incoming Calls, Outgoing Calls, and Missed Calls, using TabLayout and ViewPager.
SMS Inbox shows two lines in the list, and clicking on an SMS item opens a dialog with SMS details.
Clicking on any left panel item opens the corresponding details on the main screen.
Each action in the header bar has a refresh option to refresh the selected data.
The landing page is always set to Contacts for the first time, and the app remembers the last visited item for subsequent launches.
Contacts' photos are displayed in a circular shape using a third-party image loading library.
Clicking on any contact opens a dialog with the title "Action" and displays the contact's name. The dialog provides options to call or send an SMS to the selected contact.
Clicking on any call log item opens a dialer action for the selected call log item.
The Toolbar collapses at the Tab level when scrolling the list.
Contacts, call log, and SMS are fetched from the device itself (not with static data).
Runtime permissions are requested on application launch.
The app uses clean code and follows the MVVM architecture pattern.
LiveData, Kotlin Flow and Kotlin Coroutines are used for efficient data handling.
The Swipe to Refresh feature allows users to fetch new data from the device for contacts, call log, and SMS


The app incorporates the following components:
Toolbar
Navigation Drawer
Fragments
RecyclerView
CardView
Request Runtime Permissions model
View Binding
