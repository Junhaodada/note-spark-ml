import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
    var sp = SparkSession
      .builder()
      .master("local")
      .appName("wc app")
      .getOrCreate()

  }
}