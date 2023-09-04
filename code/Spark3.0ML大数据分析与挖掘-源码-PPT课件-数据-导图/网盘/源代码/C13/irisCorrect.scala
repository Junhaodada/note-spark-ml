package C13

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.{Matrix, Vector}
import org.apache.spark.ml.stat.{Correlation, Summarizer}
import org.apache.spark.sql.{Row, SparkSession}

object irisCorrect {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("irisCorrect")  //设置名称
      .getOrCreate()   //创建会话变量

    //隐式转换
    import spark.implicits._
    import Summarizer._

    //读取数据，第一行为列名，并且设置了自动推断数据类型。
    val data = spark.read.format("csv").option("header", "true").option("inferSchema", "true").load("./src/C13/iris.csv")

    //合并成 vector
    val assembler = new VectorAssembler()
      .setInputCols(Array("Sepal_Length","Sepal_Width"))
      .setOutputCol("features")
    val dataset = assembler.transform(data)

    //选取setosa的五十条数据
    val setosa = dataset.where("Species = 'Iris-setosa'")

    // 计算不同数据之间的相关系数
    val Row(coeff1: Matrix) = Correlation.corr(setosa, "features").head
    println(s"Pearson correlation matrix:\n $coeff1")

  }
}
