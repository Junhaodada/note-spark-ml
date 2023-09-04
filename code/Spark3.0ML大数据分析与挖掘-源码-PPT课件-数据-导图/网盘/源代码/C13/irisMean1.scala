package C13

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.stat.Summarizer
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.stat.Summarizer

object irisMean1 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("irisMean")  //设置名称
      .getOrCreate()   //创建会话变量

    //隐式转换
    import spark.implicits._
    import Summarizer._

    //读取数据，第一行为列名，并且设置了自动推断数据类型。
    val data = spark.read.format("csv").option("header", "true").option("inferSchema", "true").load("./src/C13/iris.csv")

    //合并成 vector
    val assembler = new VectorAssembler()
      .setInputCols(Array("Sepal_Length"))
      .setOutputCol("features")
    val dataset = assembler.transform(data)

    //选取setosa的五十条数据
    val setosa = dataset.where("Species = 'Iris-setosa'")

    //计算均值和方差
    val (meanVal2, varianceVal2) = setosa.select(mean($"features"), variance($"features"))
      .as[(Vector, Vector)].first()

    //打印均值
    println("setosa中Sepal.Length的均值为：" + {meanVal2})
    //打印方差
    println("setosa中Sepal.Length的方差为：" +  {varianceVal2})

  }

}
