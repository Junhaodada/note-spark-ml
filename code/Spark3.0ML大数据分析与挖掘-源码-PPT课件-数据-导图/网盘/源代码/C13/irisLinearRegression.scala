package C13

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.sql.{Row, SparkSession}

object irisLinearRegression {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("irisLinearRegression")  //设置名称
      .getOrCreate()   //创建会话变量

    //隐式转换
    import spark.implicits._

    //读取数据，第一行为列名，并且设置了自动推断数据类型。
    val data = spark.read.format("csv").option("header", "true").option("inferSchema", "true").load("./src/C13/iris.csv")

    val sepal = data.select($"Sepal_Length",$"Sepal_Width").map( {
      case Row(label: Double, features: Double) =>
        LabeledPoint(label, Vectors.dense(features))
    }).rdd.cache()

    //创建模型
//    val model = LinearRegressionWithSGD.train(parsedData, 10,0.1)
//
//    //打印回归公式
//    println("回归公式为: y = " + model.weights + " * x + " + model.intercept)

  }
}
