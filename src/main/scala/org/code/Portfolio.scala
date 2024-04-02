package org.code

case class Portfolio(data: Array[Array[Double]]):
  def transpose: Portfolio =
    val transposedData = Array.tabulate(data(0).length, data.length)((j, i) => data(i)(j))
    this.copy(data = transposedData)

  def scale(coefficient: Double): Portfolio =
    val scaledData = data.map(_.map(_ * coefficient))
    Portfolio(scaledData)

  def calculateReturnChange: Portfolio =
    val returnsChange = data.map(row => row.sliding(2).map {
      case Array(prev, next) => next - prev
    }.toArray)
    Portfolio(returnsChange)

  def applyWeights(weights: Portfolio): Portfolio =
    val weightedData = data.zip(weights.data).map {
      case (row1, row2) => row1.zip(row2).map { case (v1, v2) => v1 * v2 }
    }
    Portfolio(weightedData)

  def findAssetsWithinReturnRange(period: Int, min: Double, max: Double): List[Int] =
    data.indices.filter(i => data(i)(period) > min && data(i)(period) < max).toList

  def findMaxTotalReturn: Double =
    data.map(_.sum).max

  def print(): Unit =
    data.foreach(row => println(row.map(element => f"$element%8.2f").mkString(" ")))

object Portfolio:
  def fillRandom(rows: Int, columns: Int, min: Double, max: Double): Portfolio = {
    val random = scala.util.Random
    val data = Array.tabulate(rows, columns) {
      (_, _) => min + (max - min) * random.nextDouble()
    }
    Portfolio(data)
  }

  def combine(portfolio1: Portfolio, portfolio2: Portfolio): Portfolio = {
    val combinedData = portfolio1.data.zip(portfolio2.data).map {
      case (row1, row2) => row1.zip(row2).map { case (v1, v2) => v1 + v2 }
    }
    Portfolio(combinedData)
  }

  def createWeightsDistribution(assets: Int, periods: Int): Portfolio =
    val random = scala.util.Random
    val weights = Array.fill(periods, assets)(random.nextDouble())
    val normalizedWeights = weights.map(row =>
      val sum = row.sum
      row.map(_ / sum)
    )
    Portfolio(normalizedWeights).transpose
