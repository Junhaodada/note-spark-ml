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

package C09


import org.apache.spark.ml.clustering.GaussianMixture

import org.apache.spark.sql.SparkSession


object GaussianMixtureExample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("GaussianMixtureExample")  //设置名称
      .getOrCreate()   //创建会话变量

    // 读取数据
    val dataset = spark.read.format("libsvm").load("data/mllib/sample_kmeans_data.txt")

    // 训练 Gaussian Mixture Model，并设置参数
    val gmm = new GaussianMixture()
      .setK(2)
    val model = gmm.fit(dataset)

    // 逐个打印单个模型
    for (i <- 0 until model.getK) {
      println(s"Gaussian $i:\nweight=${model.weights(i)}\n" +
          s"mu=${model.gaussians(i).mean}\nsigma=\n${model.gaussians(i).cov}\n")
    }


    spark.stop()
  }
}

