package C13

import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.stat.Summarizer
import org.apache.spark.sql.SparkSession

object irisKmeans {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder //创建spark会话
      .master("local") //设置本地模式
      .appName("irisClassificationConvert") //设置名称
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

    val kmeans = new KMeans().setFeaturesCol("features").setK(3).setMaxIter(20)

    val model = kmeans.fit(dataset)

    // 展示结果
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)
  }
}
