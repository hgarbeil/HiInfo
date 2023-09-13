# HiInfo
Hawaii Weather and Surf Info Android Studio Base Application

This AndroidStudio based app draws information from NOAA and OpenWeathermap to obtain weather and ocean conditions relevant to surfing on Oahu. This information 
includes low and high tides and tide predictions, sunrise and sunset times, moon phase, buoy observations and surf observations. Currently tuned to south shore surfing, 
the code can be modified to allow user input or hardwire different surf regions and breaks.

The interface makes use of Android CardView elements. As I have an LG Rebel 4 phone, the UI design is targeted to low resolution devices but should work fine on higher res devices. Note that local.properties file which is not included in the git repository should include the following two lines, modified for your installation.


### for android studio installation

sdk.dir=/Users/user/Library/Android/sdk  #location of your android sdk

### for OpenWeathermap API

OW_APIKEY = <your OpenWeathermap one-call API key> #google on how to get an OpenWeathermap API key

![image](https://github.com/hgarbeil/HiInfo/assets/9002283/57fd9af8-c122-4393-b2be-c151a98f4b50)
