package C07

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LinearSVC
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{PCA, StandardScaler}
import org.apache.spark.sql.SparkSession

object SVMTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("SVMTest")  //设置名称
      .getOrCreate()   //创建会话变量

    // 加载数据
    val data = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")
    // 数据归一化
    val scaler = new StandardScaler()
      .setInputCol("features")
      .setOutputCol("scaledfeatures")
      .setWithMean(true)
      .setWithStd(true)
    val scalerdata = scaler.fit(data)
    val scaleddata = scalerdata.transform(data).select("label","scaledfeatures").toDF("label","features")

    // PCA降维
    val pca = new PCA()
      .setInputCol("features")
      .setOutputCol("pcafeatures")
      .setK(5)
      .fit(scaleddata)
    val pcadata = pca.transform(scaleddata).select("label","pcafeatures").toDF("label","features")

    // 划分数据集
    val Array(trainData, testData) = pcadata.randomSplit(Array(0.8, 0.2), seed = 20)

    // 创建SVM
    val lsvc = new LinearSVC()
      .setMaxIter(10)
      .setRegParam(0.1)

    // 创建pipeline
    val pipeline = new Pipeline()
      .setStages(Array(scaler, pca, lsvc))

    // 训练模型
    val lsvcmodel = pipeline.fit(trainData)

    // 验证精度
    val res = lsvcmodel.transform(testData).select("prediction","label")
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")

    val accuracy = evaluator.evaluate(res)
    println(s"Accuracy = ${accuracy}")

    spark.stop()

  }
}
