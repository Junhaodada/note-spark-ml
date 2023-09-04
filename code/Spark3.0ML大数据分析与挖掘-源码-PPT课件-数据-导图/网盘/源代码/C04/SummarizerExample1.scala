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
package C04

// $example on$
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.stat.Summarizer
// $example off$
import org.apache.spark.sql.SparkSession

object SummarizerExample1 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("SummarizerExample1")  //设置名称
      .getOrCreate()   //创建会话变量

    import Summarizer._
    import spark.implicits._

    // 创建数据 Vector格式
    val data = Seq(
      (Vectors.dense(2.0, 3.0, 5.0), 1.0),
      (Vectors.dense(4.0, 6.0, 7.0), 2.0)
    )
    // 转换DF格式
    val df = data.toDF("features", "weight")

    // 计算曼哈顿距离 欧几里得距离 无权重列
    val (meanVal2, varianceVal2) = df.select(normL1($"features"), normL2($"features"))
      .as[(Vector, Vector)].first()

    println(s"without weight: normL1 = ${meanVal2}, normL2 = ${varianceVal2}")


    spark.stop()
  }
}
// scalastyle:on println
