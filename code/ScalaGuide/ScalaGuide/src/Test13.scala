object Test13 {
  //方法与函数
  def main(args: Array[String]) {
    println( "Returned Value : " + addInt(5,7) );
    printMe();
  }
  def addInt( a:Int, b:Int ) : Int = {
    var sum:Int = 0
    sum = a + b

    return sum
  }
  //如果方法没有返回值，可以返回为 Unit，这个类似于 Java 的 void, 实例如下：
  def printMe( ) : Unit = {
    println("Hello, Scala!")
  }

}