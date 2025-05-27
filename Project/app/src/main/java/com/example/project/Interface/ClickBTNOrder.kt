package com.example.project.Interface

import com.example.projectmanage.Model.Entity.OrderDetail

interface ClickBTNOrder {
    fun clickSuccess(id: String)
    fun clickError(id: String)
    fun clickInfo(item: OrderDetail)
    fun clickEvaluate(id:String)
}