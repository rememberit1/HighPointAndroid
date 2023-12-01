package com.method.highpoint.utils

import com.method.highpoint.model.mymarket.MyMarketResponse
import com.method.highpoint.model.roomdb.exhibitor.ExhibitorRoomData

interface ExhibitorsCommunicator {

    fun passExhibitorUpdates(position: Int, exhibitorRoomData: ExhibitorRoomData, myMarketResponse: MyMarketResponse)
}