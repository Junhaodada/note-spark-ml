package C13

import org.apache.spark.ml.classification.{RandomForestClassificationModel, RandomForestClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{LabeledPoint, VectorAssembler}
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.stat.Summarizer
import org.apache.spark.sql.{Row, SparkSession}

object irisRFDTree {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder //创建spark会话
      .master("local") //设置本地模式
      .appName("irisRFDTree") //设置名称
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

    //转换成随机森林的输入格式
    val trainDataRdd = dataset.select($"label", $"features").map {
      case Row(label: Int, features: Vector) =>
        LabeledPoint(label.toDouble, Vectors.dense(features.toArray))
    }

    // 将数据分成训练集和测试集（30%用于测试）
    val Array(trainingData, testData) = trainDataRdd.randomSplit(Array(0.7, 0.3), seed = 1234L)

    // 建立一个决策树分类器，并设置森林中含有10颗树
    val rf = new RandomForestClassifier()
      .setLabelCol("label")
      .setFeaturesCol("features")
      .setNumTrees(10)
      .setMaxBins(16)
      .setImpurity("gini")

    // 载入训练集数据正式训练模型
    val dtcModel: RandomForestClassificationModel = rf.fit(trainingData)

    // 使用测试集作预测
    val predictions = dtcModel.transform(testData)

    // 选择一些样例进行显示
    predictions.show(5)

    // 计算测试误差
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)
    println(s"Test Error = ${(1.0 - accuracy)}")
  }
}
