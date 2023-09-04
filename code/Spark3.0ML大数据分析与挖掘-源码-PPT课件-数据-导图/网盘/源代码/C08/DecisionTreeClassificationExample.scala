package C08

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.spark.sql.SparkSession

object DecisionTreeClassificationExample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder                //创建spark会话
      .master("local")        //设置本地模式
      .appName("DecisionTreeClassificationExample")   //设置名称
      .getOrCreate()          //创建会话变量

    // 读取文件，装载数据到spark dataframe 格式中
    val data = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")

    // 搜索标签，添加元数据到标签列
    // 对整个数据集包括索引的全部标签都要适应拟合
    val labelIndexer = new StringIndexer()
      .setInputCol("label")
      .setOutputCol("indexedLabel")
      .fit(data)
    // 自动识别分类特征，并对其进行索引
    val featureIndexer = new VectorIndexer()
      .setInputCol("features") // 设置输入输出参数
      .setOutputCol("indexedFeatures")
      .setMaxCategories(4) // 具有多于4个不同值的特性被视为连续特征
      .fit(data)

    // 按照7：3的比例进行拆分数据，70%作为训练集，30%作为测试集。
    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

    // 建立一个决策树分类器
    val dt = new DecisionTreeClassifier()
      .setLabelCol("indexedLabel")
      .setFeaturesCol("indexedFeatures")

    // 将索引标签转换回原始标签
    val labelConverter = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(labelIndexer.labelsArray(0))

    // 把索引和决策树链接（组合)到一个管道（工作流）之中
    val pipeline = new Pipeline()
      .setStages(Array(labelIndexer, featureIndexer, dt, labelConverter))

    // 载入训练集数据正式训练模型
    val model = pipeline.fit(trainingData)

    // 使用测试集作预测
    val predictions = model.transform(testData)

    // 选择一些样例进行显示
    predictions.select("predictedLabel", "label", "features").show(5)

    // 计算测试误差
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("indexedLabel")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)
    println(s"Test Error = ${(1.0 - accuracy)}")

    val treeModel = model.stages(2).asInstanceOf[DecisionTreeClassificationModel]
    println(s"Learned classification tree model:\n ${treeModel.toDebugString}")
    // $example off$

    spark.stop()
  }
}
