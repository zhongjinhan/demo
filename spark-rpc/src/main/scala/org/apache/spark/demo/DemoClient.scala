package org.apache.spark.demo

import org.apache.spark.{SecurityManager, SparkConf}
import org.apache.spark.rpc.{RpcAddress, RpcEnv}
import org.apache.spark.util.RpcUtils

object DemoClient {

  def main(args: Array[String]): Unit = {
    process()
  }

  def process(): Unit ={
    val sparkConf = new SparkConf()
    val securityManager = new SecurityManager(sparkConf)

    val rpcEnv = RpcEnv.create("test-env", "", -1, sparkConf, securityManager, clientMode = true)

    val ref = rpcEnv.setupEndpointRef(RpcAddress("127.0.0.1",10000), EchoRpcEndpoint.ENDPOINT_NAME)

    0 until 10 foreach{ i =>
      val reply = ref.askSync[EchoMessage](EchoMessage("hallo"))
      println(s"message replied: $reply")
    }
    rpcEnv.stop(ref)
    rpcEnv.shutdown()
  }

}
