package com.teamg.taxi.map

import com.teamg.taxi.map.Edge.Label

import scala.util.Random

object Utils {

  def createNode(id: String): Node[String] = {
    Node(id, Random.nextDouble(), Random.nextDouble())
  }

  def createEdge(first: Node[String], second: Node[String], weight: Double): Edge[String] = {
    Edge(Label(first.id + "-" + second.id), first, second, weight)
  }
}
