package org.apache.spark.demo

import org.apache.spark.rpc.{RpcCallContext, RpcEndpoint, RpcEnv}

class EchoRpcEndpoint(rpc: RpcEnv) extends RpcEndpoint {

  override val rpcEnv: RpcEnv = rpc

  private var inc: Int = 0

  override def onStart(): Unit = {
    println(s"${this.getClass.getName} started")
  }

  override def onStop(): Unit = {
    println(s"${this.getClass.getName} stopped")
  }

  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case EchoMessage(msg) =>
      inc = inc + 1
      println(s"messaged received:$msg")
      context.reply(EchoMessage(s"$msg ${(0 until inc).map(_ =>"+").mkString("")}"))
  }

}

object EchoRpcEndpoint {
  val ENDPOINT_NAME = "echo"
}
