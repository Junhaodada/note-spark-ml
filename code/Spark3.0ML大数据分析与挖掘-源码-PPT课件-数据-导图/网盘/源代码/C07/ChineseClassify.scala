package C07

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.HashingTF
import org.apache.spark.ml.feature.IDF
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.feature.Tokenizer
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.{Row, SparkSession}

object ChineseClassify {
  case class RawDataRecord(category: String, text: String)
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("ChineseClassify")  //设置名称
      .getOrCreate()   //创建会话变量

    val sc = spark.sparkContext
    import spark.implicits._

    // 实现隐式转换
    val tmp = sc.textFile("data/sougou-train/").map{
      x =>
        var temp = x.split(",")
        RawDataRecord(temp(0),temp(1))
    }.toDF("category","text")

    // 将数据分成训练集和测试集（30%用于测试）
    val Array(trainingData, testData) = tmp.randomSplit(Array(0.7, 0.3), seed = 1234L)

    // 将分好的词转换为数组 Tokenizer()只能分割以空格间隔的字符串
    var tokenizer = new Tokenizer().setInputCol("text").setOutputCol("words")

    // 将每个词转换成Int型，并计算其在文档中的词频（TF）
    var hashingTF = new HashingTF().setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(500000)
    var idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")

    // 管道技术
    val pipeline = new Pipeline()
      .setStages(Array(tokenizer, hashingTF, idf))

    var idfModel = pipeline.fit(trainingData)

    var rescaledData = idfModel.transform(trainingData)

    // 转换成NaiveBayes的输入格式
    var trainDataRdd = rescaledData.select($"category",$"features").map {
      case Row(label: String, features: Vector) =>
        LabeledPoint(label.toDouble, Vectors.dense(features.toArray))
    }

    // 训练一个朴素贝叶斯模型。
    val model = new NaiveBayes()
      .fit(trainDataRdd)

    // 对测试集做同样的处理
    val testrescaledData = idfModel.transform(testData)

    var testDataRdd = testrescaledData.select($"category",$"features").map {
      case Row(label: String, features: Vector) =>
        LabeledPoint(label.toDouble, Vectors.dense(features.toArray))
    }

    // 预测结果
    val testpredictionAndLabel = model.transform(testDataRdd)
    testpredictionAndLabel.show(1)

    //测试结果评估
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")

    //测试结果准确率
    val accuracy = evaluator.evaluate(testpredictionAndLabel)
    println(s"Test set accuracy = $accuracy")
  }
}
