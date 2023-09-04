object Test20 {
  //迭代器
  def main(args: Array[String]) {
    val it = Iterator("Baidu", "Google", "Runoob", "Taobao")

    while (it.hasNext){
      println(it.next())
    }
  }
}