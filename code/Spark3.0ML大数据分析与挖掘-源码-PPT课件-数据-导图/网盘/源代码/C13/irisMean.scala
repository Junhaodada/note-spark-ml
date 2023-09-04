package C13

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.stat.Summarizer
import org.apache.spark.sql.functions._

object irisMean {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("irisMean")  //设置名称
      .getOrCreate()   //创建会话变量

    import spark.implicits._

    val data = spark.read.format("csv").option("header", "true").option("inferSchema", "true").load("./src/C13/iris.csv")
    data.printSchema()
    data.show(5)


    val setosa = data.where("Species = 'Iris-setosa'")
    setosa.agg(mean($"Sepal_Length") as "mean_Sepal_Length",variance($"Sepal_Length") as "variance_Sepal_Length").show()
  }
}
