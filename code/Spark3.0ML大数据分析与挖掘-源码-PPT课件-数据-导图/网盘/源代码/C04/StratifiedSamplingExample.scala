package C04

import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.PairRDDFunctions
import org.apache.spark.sql.DataFrameStatFunctions
object StratifiedSamplingExample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("StratifiedSamplingExample")  //设置名称
      .getOrCreate()   //创建会话变量

    import spark.implicits._

    val data =
      Seq((1, 1.0), (1, 1.0), (1, 1.0), (2, 1.0), (2, 1.0), (3, 1.0))

    val stat = data.toDF().rdd.keyBy(_.getInt(0))

    // 确定每一组的抽样分数
    val fractions = Map(1 -> 1.0,2 -> 0.6, 3 -> 0.3)

    // 得到每一组的近似抽样
    val approxSample1 = stat.sampleByKey(withReplacement = false, fractions = fractions)

    println(s"approxSample size is ${approxSample1.collect().size}")
    approxSample1.collect().foreach(println)
//
//
//    val stat1 = data.toDF("keyColumn","value1")
//
//    // specify the exact fraction desired from each key
//    val fractions1 = Map(1 -> 0.1, 2 -> 0.6, 3 -> 0.3)
//
//    // Get an approximate sample from each stratum
//    val approxSample = stat1.stat.sampleBy("keyColumn", fractions1, 1)
//
//    println(s"approxSample size is ${approxSample.collect().size}")
//    approxSample.collect().foreach(println)
//
//    import org.apache.spark.sql.Row
//    import org.apache.spark.sql.functions.struct
//
//    val df = spark.createDataFrame(Seq(("Bob", 17), ("Alice", 10), ("Nico", 8), ("Bob", 17),
//      ("Alice", 10))).toDF("name", "age")
//    val fractions2 = Map(Row("Alice", 10) -> 0.3, Row("Nico", 8) -> 1.0)
//    df.stat.sampleBy(struct($"name", $"age"), fractions2, 36L).show()
  }
}
