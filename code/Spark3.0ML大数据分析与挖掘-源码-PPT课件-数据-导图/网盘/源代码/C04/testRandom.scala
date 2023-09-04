package C04

import org.apache.spark.mllib.random.RandomRDDs.normalRDD
import org.apache.spark.sql.SparkSession

object testRandom {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder //创建spark会话
      .master("local") //设置本地模式
      .appName("testRandom") //设置名称
      .getOrCreate() //创建会话变量

    val randomNum = normalRDD(spark.sparkContext, 100) 	//创建100个随机数
    randomNum.foreach(println) 			//打印数据

  }
}
