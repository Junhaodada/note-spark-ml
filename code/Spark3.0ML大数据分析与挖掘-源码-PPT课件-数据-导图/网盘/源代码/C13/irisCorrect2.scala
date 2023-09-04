package C13

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Matrix
import org.apache.spark.ml.stat.{Correlation, Summarizer}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.mllib.stat.Statistics

object irisCorrect2 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("irisCorrect2")  //设置名称
      .getOrCreate()   //创建会话变量

    //隐式转换
    import spark.implicits._
    import Summarizer._

    //读取数据，第一行为列名，并且设置了自动推断数据类型。
    val data = spark.read.format("csv").option("header", "true").option("inferSchema", "true").load("./src/C13/iris.csv")


    val dataset = data.select("Sepal_Length")

    //选取setosa的五十条数据,并转换为RDD进行计算
    val setosa = dataset.where("Species = 'Iris-setosa'").rdd.map(x=>x.getDouble(0))
    val versicolor = dataset.where("Species = 'Iris-versicolor'").rdd.map(x=>x.getDouble(0))

    //计算不同数据之间的相关系数
    val correlation: Double = Statistics.corr(setosa, versicolor)

    //打印相关系数
    println("setosa和versicolor中Sepal.Length的相关系数为：" + correlation)


  }
}
