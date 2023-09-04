package C03

import org.apache.spark.sql.SparkSession

object randomSplit {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()//创建spark会话
      .appName("Spark SQL basic example")//设置会话名称
      .master("local") //设置本地模式
      .getOrCreate()//创建会话变量
    val df = spark.range(15).toDF()
    val dataFrames = df.randomSplit(Array(0.25, 0.75), seed = 10)
    dataFrames(0).show()
    dataFrames(1).show()
  }
}
