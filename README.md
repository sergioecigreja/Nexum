<h1>Nexum</h1>

The intent of this project is to easily share Android notifications with a macbook on the same network.
The idea is to broadcast a message with UDP everytime a notification is detected, which should be received by the Nexum app on the Macbook.
<br/>
These steps should be achieved before releasing a first version:
1. Send a string from the android app to the macbook app via udp.
2. Listen to android notifications and send them to the macbook app.
3. Macbook app becomes a background service that notifies the User using the standard macOS notifications.
4. User can select which apps send the notification to macOS, with the intent to increase productivity since irrelevant notifications don't go through.
