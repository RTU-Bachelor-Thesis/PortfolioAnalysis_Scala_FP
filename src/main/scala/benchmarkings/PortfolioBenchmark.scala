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
  var portfolio1: Portfolio = uninitialized
  var portfolio2: Portfolio = uninitialized
  var weightMatrix: Portfolio = uninitialized

  @Setup(Level.Iteration)
  def setupPortfolio(): Unit = {
    portfolio1 = Portfolio.fillRandom(assetsCount, periodsCount, -10.0, 10.0)
    portfolio2 = Portfolio.fillRandom(assetsCount, periodsCount, -10.0, 10.0)
    weightMatrix = Portfolio.createWeightsDistribution(assetsCount, periodsCount)
  }

  @Benchmark
  def benchmarkFillRandom(): Portfolio = {
    Portfolio.fillRandom(assetsCount, periodsCount, -10.0, 10.0)
  }

  @Benchmark
  def benchmarkTranspose(): Portfolio = {
    portfolio1.transpose
  }

  @Benchmark
  def benchmarkScale(): Portfolio = {
    portfolio1.scale(Math.PI)
  }

  @Benchmark
  def benchmarkCalculateReturnsChange(): Portfolio = {
    portfolio1.calculateReturnChange
  }

  @Benchmark
  def benchmarkCombine(): Portfolio = {
    Portfolio.combine(portfolio1, portfolio2)
  }

  @Benchmark
  def benchmarkApplyWeights(): Portfolio = {
    portfolio1.applyWeights(weightMatrix)
  }

  @Benchmark
  def benchmarkFindAssetsWithinReturnRange(): Unit = {
    portfolio1.findAssetsWithinReturnRange(5, 2, 5)
  }

  @Benchmark
  def benchmarkFindMaxTotalReturn(): Double = {
    portfolio1.findMaxTotalReturn
  }
}
