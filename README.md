FrozenLED
=========

An android app that helps you manage your device's notification LED.

Compatibility
=============

On paper, this should run on 4.3 and above. Practically, the way Android handles the notification LED is not standardized among versions and manufacturers skins so it might not work on all devices. For example, CyanogenMod gives the LED to the first notification but a notification with higher priority can take control. Stock android simply lets the first posted notification to control the LED until it's cleared.

The app can be "fixed" in a couple of way. Maybe set a transperant LED notification at startup to reserve the LED. Or an xposed module.
