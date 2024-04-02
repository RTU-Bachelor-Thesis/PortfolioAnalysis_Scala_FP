package benchmarkings

import scala.compiletime.uninitialized
import org.openjdk.jmh.annotations.*
import org.code.Portfolio

import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@Fork(1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Array(Mode.AverageTime))
class PortfolioBenchmark {
  @Param(Array("10", "100", "1000"))
  var assetsCount: Int = uninitialized
  var periodsCount: Int = 12
  var originalPortfolio: Portfolio = uninitialized
  var additionalPortfolio: Portfolio = uninitialized
  var weightMatrix: Portfolio = uninitialized

  @Setup(Level.Iteration)
  def setupPortfolio(): Unit = {
    originalPortfolio = Portfolio.fillRandom(assetsCount, periodsCount, -10.0, 10.0)
    additionalPortfolio = Portfolio.fillRandom(assetsCount, periodsCount, -10.0, 10.0)
    weightMatrix = Portfolio.createWeightsDistribution(assetsCount, periodsCount)
  }

  @Benchmark
  def benchmarkFillRandom(): Portfolio = {
    Portfolio.fillRandom(assetsCount, periodsCount, -10.0, 10.0)
  }

  @Benchmark
  def benchmarkTranspose(): Portfolio = {
    originalPortfolio.transpose
  }

  @Benchmark
  def benchmarkScale(): Portfolio = {
    originalPortfolio.scale(Math.PI)
  }

  @Benchmark
  def benchmarkCalculateReturnChange(): Portfolio = {
    originalPortfolio.calculateReturnChange
  }

  @Benchmark
  def benchmarkCombine(): Portfolio = {
    Portfolio.combine(originalPortfolio, additionalPortfolio)
  }

  @Benchmark
  def benchmarkApplyWeights(): Portfolio = {
    originalPortfolio.applyWeights(weightMatrix)
  }

  @Benchmark
  def benchmarkFindAssetsWithinReturnRange(): Unit = {
    originalPortfolio.findAssetsWithinReturnRange(5, 2, 5)
  }

  @Benchmark
  def benchmarkFindMaxTotalReturn(): Double = {
    originalPortfolio.findMaxTotalReturn
  }
}
