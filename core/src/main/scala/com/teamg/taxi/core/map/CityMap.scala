package com.teamg.taxi.core.map

import cats.Eq
import cats.implicits._
import com.teamg.taxi.core.map.Edge.Label
import scalax.collection.Graph
import scalax.collection.edge.WLUnDiEdge
import com.teamg.taxi.core.utils.Utils._

import scala.util.Random


class CityMap[ID](graph: Graph[Node[ID], WLUnDiEdge]) {

  implicit val eqFoo: Eq[ID] = Eq.fromUniversalEquals

  def minimalDistance(fromId: ID, toId: ID): Option[Double] = {
    shortestPath(fromId, toId).map(_.weight)
  }

  def edges(fromId: ID, toId: ID): Option[List[Edge[ID]]] = {
    edgesOnPath(fromId, toId)
  }

  def getCityMapElements: CityMapElements[ID] = {
    val nodes = graph.nodes.map(n => Node(n.id, n.location)).toList
    val edges = graph.edges.map(e => Edge(Label.empty, e.head.value, e.to.value, e.weight)).toList
    CityMapElements(nodes, edges)
  }

  def randomNode(): Node[ID] = {
    val nodes = graph.nodes.map(_.value).toSeq
    getRandomElement(nodes, new Random())
  }

  def getNode(id: Int): Node[ID] = {
    val nodes = graph.nodes.map(_.value).toSeq
    nodes(id)
  }


  private def edgesOnPath(fromId: ID, toId: ID): Option[List[Edge[ID]]] = {
    shortestPath(fromId, toId)
      .map(_.edges.map(e => Edge(Label.empty, e.head.value, e.to.value, e.weight))
        .toList
      )
  }

  private def shortestPath(fromId: ID, toId: ID): Option[graph.Path] = {
    for {
      nodeFrom <- graph.nodes.find(_.id === fromId)
      nodeTo <- graph.nodes.find(_.id === toId)
      shortestPath <- nodeFrom.shortestPathTo(nodeTo)
    } yield shortestPath
  }
}

object CityMap {

  def apply[ID](nodes: Set[Node[ID]], edges: List[Edge[ID]]): CityMap[ID] = {
    val graphEdges = edges.map(graphEdge)
    val graph = Graph.from(nodes, graphEdges)
    new CityMap(graph)
  }

  private def graphEdge[ID](edge: Edge[ID]): WLUnDiEdge[Node[ID]] = {
    WLUnDiEdge(edge.first, edge.second)(edge.weight, edge.label)
  }
}
