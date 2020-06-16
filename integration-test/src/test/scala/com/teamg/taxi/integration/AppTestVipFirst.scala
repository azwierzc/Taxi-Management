package com.teamg.taxi.integration

import com.teamg.taxi.core.DefaultSimulationConfig
import com.teamg.taxi.core.api.OrderService.OrderRequest

object AppTestVipFirst extends BaseApp {
  override def simulationConfig = DefaultSimulationConfig

  startGUI

  sendOrderRequest(OrderRequest("H", "F", "normal", "normal", "abc"))
  Thread.sleep(5000)
  sendOrderRequest(OrderRequest("K", "J", "vip", "normal", "abc"))
}
