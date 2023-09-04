/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package C06


import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

object LinearRegressionWithElasticNetExample {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("LinearRegressionWithElasticNetExample")  //设置名称
      .getOrCreate()   //创建会话变量

    // $example on$
    // 读取数据
    val training = spark.read.format("libsvm")
      .load("data/mllib/sample_linear_regression_data.txt")

    // 建立一个Estimator。并设置参数
    val lr = new LinearRegression()
      .setMaxIter(10)
      .setRegParam(0.3) //正则化参数
      .setElasticNetParam(0.8) // 使用ElasticNet回归

    // 训练模型
    val lrModel = lr.fit(training)

    // 打印一些系数（回归系数表）和截距
    println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

    // 汇总一些指标并打印结果
    val trainingSummary = lrModel.summary
    println(s"numIterations: ${trainingSummary.totalIterations}")
    println(s"objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]")
    trainingSummary.residuals.show()
    println(s"RMSE: ${trainingSummary.rootMeanSquaredError}")
    println(s"r2: ${trainingSummary.r2}")

    spark.stop()
  }
}
// scalastyle:on println
