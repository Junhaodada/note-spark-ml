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
package C07

// $example on$
import org.apache.spark.ml.classification.LinearSVC
import org.apache.spark.sql.SparkSession

object LinearSVCExample {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("LinearSVCExample")  //设置名称
      .getOrCreate()   //创建会话变量


    // 加载数据
    val training = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")

    val lsvc = new LinearSVC()
      .setMaxIter(10)
      .setRegParam(0.1)

    // 训练模型
    val lsvcModel = lsvc.fit(training)

    // 打印系数和截距
    println(s"Coefficients: ${lsvcModel.coefficients} Intercept: ${lsvcModel.intercept}")

    spark.stop()
  }
}
// scalastyle:on println
