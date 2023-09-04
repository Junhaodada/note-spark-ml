package C03

import org.apache.spark.sql.SparkSession

object collect {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()//创建spark会话
      .appName("Spark SQL basic example")//设置会话名称
      .master("local") //设置本地模式
      .getOrCreate()//创建会话变量
    val rdd = spark.sparkContext.parallelize(Array(1,2,3,4))
    import spark.implicits._
    val df = rdd.toDF("id")
    val arr =  df.collectAsList()
    println(arr)
  }
}
