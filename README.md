# DNetApp
 Weather Monitoring for DNet Exercise

# App Usage
## Testing using app-debug.apk
1. Download a copy of app located at AppDebug folder in this repository
2. Install the apk file to you Android device.
3. As you install and open the app, when prompted for location permissions, just choose "Allow"
4. Weather details are updated per minute. This is to avoid overuse of API since it is only using a free tier version

## Testing from Android Studio
1. Checkout this repository to your harddrive
2. Install Android Studio from https://developer.android.com/studio/?gclid=EAIaIQobChMIrZDXq-jf5QIVWKqWCh0aZwGrEAAYASAAEgLgcPD_BwE
3. Open project via Android Studio after installation.
4. Do gradle sync before you build by right clicking the project the choose "Synchronize 'app'"
5. Go to Tools -> SDK Manager then choose the appropriate SDK/API version of your target device.
6. If you want to run the app via emulator, go to Tools -> AVD Manager, then create an emulator based on the version of your target device
7. If you want to run the app on a real device, enable USB Debugging on your target device then connect it to USB of you PC running Android Studio, then choose your target device when running the app and if it appears to the Device Choices.
8. Weather details are updated per minute. This is to avoid overuse of API since it is only using a free tier version.