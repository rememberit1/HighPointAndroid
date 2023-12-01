package com.method.highpoint.utils

import com.method.highpoint.model.roomdb.event.EventRoomData

interface EventsCommunicator {

    fun passEventUpdates(position: Int, removed: Boolean, eventRoomData: EventRoomData)
}