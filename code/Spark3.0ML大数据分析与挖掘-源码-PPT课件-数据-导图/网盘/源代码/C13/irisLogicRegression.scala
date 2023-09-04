package C13

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{LabeledPoint, VectorAssembler}
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.stat.Summarizer
import org.apache.spark.sql.{Row, SparkSession}

object irisLogicRegression {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder //创建spark会话
      .master("local") //设置本地模式
      .appName("irisLogicRegression") //设置名称
      .getOrCreate() //创建会话变量

    //隐式转换
    import spark.implicits._
    import Summarizer._

    //读取数据，第一行为列名，并且关闭了自动推断数据类型。
    val data = spark.read.format("csv").option("header", "true").option("inferSchema", "false").load("./src/C13/iris.csv").map(row => {
      val label = row.getString(4) match {
        case "Iris-setosa" => 0
        case "Iris-versicolor" => 1
        case "Iris-virginica" => 2
      }
      (row.getString(0).toDouble,
        row.getString(1).toDouble,
        row.getString(2).toDouble,
        row.getString(3).toDouble,
        label)
    }).toDF("Sepal_Length", "Sepal_Width", "Petal_Length", "Petal_Width", "label")

    //合并成 vector
    val assembler = new VectorAssembler()
      .setInputCols(Array("Sepal_Length", "Sepal_Width", "Petal_Length", "Petal_Width"))
      .setOutputCol("features")
    val dataset = assembler.transform(data)

    //转换成LogisticRegression的输入格式
    val trainDataRdd = dataset.select($"label", $"features").map {
      case Row(label: Int, features: Vector) =>
        LabeledPoint(label.toDouble, Vectors.dense(features.toArray))
    }

    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    // 训练模型
    val lrModel = lr.fit(trainDataRdd)


    // 打印逻辑回归的系数和截距
    println(s"Coefficients: \n${lrModel.coefficientMatrix}")
    println(s"Intercepts: \n${lrModel.interceptVector}")
  }
}
