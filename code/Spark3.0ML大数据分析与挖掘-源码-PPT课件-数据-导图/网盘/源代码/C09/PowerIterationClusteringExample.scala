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
package C09

// $example on$
import org.apache.spark.ml.clustering.PowerIterationClustering

import org.apache.spark.sql.SparkSession

object PowerIterationClusteringExample {
   def main(args: Array[String]): Unit = {
     val spark = SparkSession
       .builder      //创建spark会话
       .master("local")  //设置本地模式
       .appName("PowerIterationClusteringExample")  //设置名称
       .getOrCreate()   //创建会话变量

     // 创建快速迭代聚类的数据源
     val dataset = spark.createDataFrame(Seq(
       (0L, 1L, 1.0),
       (0L, 2L, 1.0),
       (1L, 2L, 1.0),
       (3L, 4L, 1.0),
       (4L, 0L, 0.1)
     )).toDF("src", "dst", "weight")

     //创建专用类
     val model = new PowerIterationClustering().
       setK(2).//设定聚类数
       setMaxIter(20).//设置迭代次数
       setInitMode("degree").//初始化算法的参数
       setWeightCol("weight")//权重列名称的参数

     // 进行数据集预测
     val prediction = model.assignClusters(dataset).select("id", "cluster")

     //  展示结果
     prediction.show(false)
     // $example off$

     spark.stop()
   }
 }
