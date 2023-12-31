package C06
import scala.collection.mutable.HashMap

object SGDtest {
  val data = HashMap[Int,Int]()	//创建数据集
  def getData():HashMap[Int,Int] = {//生成数据集内容
    for(i <- 1 to 50){	//创建50个数据
      data += (i -> (16*i))//写入公式y=16x
    }
    data		//返回数据集
  }

  var θ:Double = 0	//第一步假设θ为0
  var α:Double = 0.1	//设置步进系数，每次下降的幅度大小

  def sgd(x:Double,y:Double) = {//设置迭代公式
    θ = θ - α * ( (θ*x) - y)	//迭代公式
  }
  def main(args: Array[String]): Unit = {
    val dataSource = getData()	//获取数据集
    dataSource.foreach(myMap =>{//开始迭代
      sgd(myMap._1,myMap._2)//输入数据
    })
    println("最终结果θ值为 " + θ)//显示结果
  }
}
