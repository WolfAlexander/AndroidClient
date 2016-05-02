# AndroidClient

This app is a part of a project in "Project and project method" course at KTH. 
Goal with this project is to study and practice working in a Scrum project.

Our group's assigment is using Raspberry PI for server and Tellstick Duo for communication with different devices 
create a simple Smart Home system to controll different devices from an Android app (app is my and Ulrika Lageström's responsibility).

Primary functionality of this AndroidClient: 
* To list and controll devices
* To add new devices
* To see schedule of scheduled on/off events
* To add new scheduled events

Due to limited time that all that will be nessesary for this cource, but a lot more work can be done further.

Structure: Model-View-Controller
With Android app it is not possible to follow MVC strictly (as I understood) because Activities is kind of Views and Controllers in same
box, but we tried to create some kind MVC to simplify our work - so we see each Activity as View and its own Controller and then we have
a general controller for the functional part of the app.

Other patterns:
* Singleton - there is not need for many object to have multiple instances, for example UIFactory
* Factory - factory pattern is used for creation of different GUI object that had to be created programatically since the depended on data recieve from sever
* Observer - to display and update data recieved from server, for example list of devices

Connection to sever:
The idea from the beginning was to user SSL Sockets, but some problem of occured with sertificates on client side so that was 
putted away for now since other functionality is more crucial for this cource. So we user usual Sockets. We communicate with server
using our own DTO:s so we have a kind of our own "protocol".

This app is my and Ulrika's first attempt to create Android Apps, but it works well.
Rest of the project can be found here: https://github.com/grupp6kth
