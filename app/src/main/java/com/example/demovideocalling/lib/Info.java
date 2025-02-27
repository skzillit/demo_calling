package com.example.demovideocalling.lib;

import com.example.demovideocalling.lib.model.DeviceInfo;

public class Info {

  public String getId() {
    return "";
  }

  public String getDisplayName() {
    return "";
  }

  public DeviceInfo getDevice() {
    return DeviceInfo.Companion.androidDevice();
  }
}
