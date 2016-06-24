# TicketMaster for Amazon Fire TV 

The application for browsing TicketMaster events 
 

![](/FireTV/screens/homescreen.png)

## Prerequisites

1. Amazon Fire TV device https://www.amazon.com/Amazon-Fire-TV-Streaming-Media-Player/dp/B00U3FPN4U connected with Internet
2. Android SDK and Android Studio https://developer.android.com/studio/index.html. Getting Started Developing Apps and Games for Amazon Fire TV https://developer.amazon.com/public/solutions/devices/fire-tv/docs/getting-started-developing-apps-and-games-for-amazon-fire-tv
3. Connecting to Fire TV Through ADB https://developer.amazon.com/public/solutions/devices/fire-tv/docs/connecting-adb-to-fire-tv-device 

## Build 
```
$ cd ./FireTV
$ gradlew clean build
```

## Install

Run commands
```
adb connect <Fire TV ip-address>:5555
adb push .\app\build\outputs\apk\app-debug.apk /data/local/tmp/com.ticketmaster.amazon
adb shell pm install -r "/data/local/tmp/com.ticketmaster.amazon"
adb shell am start -n "com.ticketmaster.amazon/com.ticketmaster.amazon.activity.HomeScreenActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
```
