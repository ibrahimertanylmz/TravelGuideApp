package com.turkcell.travelguideapp.bll

import com.turkcell.travelguideapp.dal.TravelGuideOperation
import com.turkcell.travelguideapp.model.Visitation

object VisitationLogic {
    private val listVisitation = ArrayList<Visitation>()

    fun addVisitation(dbOperation: TravelGuideOperation, visitation: Visitation) {
        dbOperation.addVisitation(visitation)
    }
}