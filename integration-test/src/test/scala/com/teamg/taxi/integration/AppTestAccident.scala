package com.teamg.taxi.integration

import com.teamg.taxi.core.DefaultSimulationConfig
import com.teamg.taxi.core.api.AccidentService.AccidentRequest
import com.teamg.taxi.core.api.OrderService.OrderRequest


object AppTestAccident extends BaseApp {
  override def simulationConfig = DefaultSimulationConfig

  startGUI
  sendAccidentRequest(AccidentRequest("P", "O", 300.0))
  sendOrderRequest(OrderRequest("P", "N", "normal", "normal", "abc"))

}
