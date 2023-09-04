object Test11 {
  def main(args: Array[String]) {
    var a = 10;
    // 无限循环
    while( true ){
      println( "a 的值为 : " + a );
    }
  }
  //while 循环	运行一系列语句，如果条件为true，会重复运行，直到条件变为false。
  //do...while 循环	类似 while 语句区别在于判断循环条件之前，先执行一次循环的代码块。
  //for 循环	用来重复执行一系列语句直到达成特定条件达成，一般通过在每次循环完成后增加计数器的值来实现。
}