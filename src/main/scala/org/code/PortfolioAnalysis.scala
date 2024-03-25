package org.code

@main
def portfolioAnalysis(): Unit = {
  val assetsCount = 10
  val periodsCount = 12
  val initialPortfolio = Portfolio(Array.fill(assetsCount, periodsCount)(scala.util.Random.between(-10.0, 10.0)))

  println("\nProfitability portfolio 1:")
  initialPortfolio.print()

  val transposedPortfolio = initialPortfolio.transpose
  println("\nTransposed portfolio:")
  transposedPortfolio.print()

  val scaledPortfolio = initialPortfolio.scale(Math.PI)
  println("\nScaled portfolio:")
  scaledPortfolio.print()

  val returnsChangePortfolio = initialPortfolio.calculateReturnsChange
  println("\nChange in returns:")
  returnsChangePortfolio.print()

  val additionalPortfolio = Portfolio(Array.fill(assetsCount, periodsCount)(scala.util.Random.between(-10.0, 10.0)))
  println("\nProfitability portfolio 2:")
  additionalPortfolio.print()

  val combinedPortfolios = Portfolio.combine(initialPortfolio, additionalPortfolio)
  println("\nCombined profitability of portfolios:")
  combinedPortfolios.print()

  val weightMatrix = Portfolio.createWeightsDistribution(assetsCount, periodsCount)
  println("\nWeight matrix:")
  weightMatrix.print()

  val weightedPortfolio = initialPortfolio.applyWeights(weightMatrix)
  println("\nWeighted portfolio:")
  weightedPortfolio.print()

  val maxReturn = initialPortfolio.findMaxTotalReturn
  println(f"\nMaximum total return: $maxReturn%.2f")

  val rangeFilteredAssets = initialPortfolio.findAssetsWithinReturnRange(5, 2, 5)
  println(s"\nIndexes of assets with returns in June greater than 2 and less than 5: ${rangeFilteredAssets.mkString(", ")}")
}
