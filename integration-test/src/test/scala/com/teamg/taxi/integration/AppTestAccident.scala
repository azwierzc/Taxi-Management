package com.teamg.taxi.integration

import com.teamg.taxi.core.DefaultSimulationConfig
import com.teamg.taxi.core.api.AccidentService.AccidentRequest
import com.teamg.taxi.core.api.OrderService.OrderRequest


object AppTestAccident extends BaseApp {
  override def simulationConfig = DefaultSimulationConfig

  startGUI

  sendOrderRequest(OrderRequest("T", "B", "normal", "normal", "abc"))
  sendAccidentRequest(AccidentRequest("A", "C", 10.0))

}
