package com.example.demovideocalling.lib

import com.example.demovideocalling.lib.model.DeviceInfo


class RoomOptions {
    // Device info.
    private var mDevice: DeviceInfo = DeviceInfo.androidDevice()

    // Whether we want to force RTC over TCP.
    var isForceTcp: Boolean = false
        private set

    // Whether we want to produce audio/video.
    var isProduce: Boolean = true
        private set

    // Whether we should consume.
    var isConsume: Boolean = true
        private set

    // Whether we want DataChannels.
    var isUseDataChannel: Boolean = false
        private set

    fun setDevice(device: DeviceInfo): RoomOptions {
        this.mDevice = device
        return this
    }

    fun setForceTcp(forceTcp: Boolean): RoomOptions {
        this.isForceTcp = forceTcp
        return this
    }

    fun setProduce(produce: Boolean): RoomOptions {
        this.isProduce = produce
        return this
    }

    fun setConsume(consume: Boolean): RoomOptions {
        this.isConsume = consume
        return this
    }

    fun setUseDataChannel(useDataChannel: Boolean): RoomOptions {
        this.isUseDataChannel = useDataChannel
        return this
    }

    val device: DeviceInfo
        get() = mDevice
}