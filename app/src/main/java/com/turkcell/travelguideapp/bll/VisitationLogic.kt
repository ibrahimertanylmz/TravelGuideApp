package com.turkcell.travelguideapp.bll

import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.model.Visitation

object VisitationLogic {
    val listVisitation = ArrayList<Visitation>()

    fun addVisitation(dbOperation: TravelGuideOperation, visitation: Visitation, placeId: Int) {
        dbOperation.addVisitation(visitation, placeId)
    }
}