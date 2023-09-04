package C09

/**
 * An example demonstrating k-means clustering.
 * Run with
 * {{{
 * bin/run-example ml.KMeansExample
 * }}}
 */
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.sql.SparkSession

object KMeansExample {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("K-means")  //设置名称
      .getOrCreate()   //创建会话变量

    // 读取数据
    val dataset = spark.read.format("libsvm").load("data/mllib/sample_kmeans_data.txt")

    // 训练模型，设置参数，载入训练集数据正式训练模型
    val kmeans = new KMeans().setK(3).setSeed(1L)
    val model = kmeans.fit(dataset)

    // 使用测试集作预测
    val predictions = model.transform(dataset)

    // 使用轮廓分评估模型
    val evaluator = new ClusteringEvaluator()

    val silhouette = evaluator.evaluate(predictions)
    println(s"Silhouette with squared euclidean distance = $silhouette")

    // 展示结果
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)


    spark.stop()
  }
}
// scalastyle:on println
